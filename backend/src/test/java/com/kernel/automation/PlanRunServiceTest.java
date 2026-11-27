package com.kernel.automation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionOperations;

class PlanRunServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 2, 12, 0);
    private final Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private InvestPlanRepository repo;
    private PlanExecutor executor;
    private ApplicationEventPublisher events;
    private PlanRunService service;

    @BeforeEach
    void setUp() {
        repo = mock(InvestPlanRepository.class);
        executor = mock(PlanExecutor.class);
        events = mock(ApplicationEventPublisher.class);
        service = new PlanRunService(repo, executor, TransactionOperations.withoutTransaction(), events, clock);
    }

    private InvestPlan duePlan(long id) {
        InvestPlan plan = InvestPlan.create(10L, new BigDecimal("5000"), Frequency.WEEKLY, NOW.minusHours(1));
        ReflectionTestUtils.setField(plan, "id", id);
        when(repo.findById(id)).thenReturn(Optional.of(plan));
        return plan;
    }

    private void due(InvestPlan... plans) {
        when(repo.findByStatusAndNextRunLessThanEqual(PlanStatus.ACTIVE, NOW)).thenReturn(List.of(plans));
    }

    @Test
    void successfulRunAdvancesTheNextRun() {
        InvestPlan plan = duePlan(1);
        due(plan);
        when(executor.execute(plan)).thenReturn(ExecutionResult.SUCCESS);

        RunSummary summary = service.runDuePlans();

        assertThat(summary).isEqualTo(new RunSummary(1, 1, 0, 0));
        assertThat(plan.getNextRun()).isEqualTo(NOW.minusHours(1).plusWeeks(1));
        verify(repo).save(plan);
        verify(events, never()).publishEvent(any(Object.class));
    }

    @Test
    void insufficientFundsCountsAFailureAndPublishesAnEvent() {
        InvestPlan plan = duePlan(1);
        due(plan);
        when(executor.execute(plan)).thenReturn(ExecutionResult.INSUFFICIENT_FUNDS);

        RunSummary summary = service.runDuePlans();

        assertThat(summary).isEqualTo(new RunSummary(1, 0, 1, 0));
        assertThat(plan.getFailedRuns()).isEqualTo(1);
        ArgumentCaptor<Object> event = ArgumentCaptor.forClass(Object.class);
        verify(events).publishEvent(event.capture());
        assertThat(event.getValue()).isEqualTo(new PlanRunFailedEvent(1L, 1, false));
    }

    @Test
    void thirdFailureSuspendsThePlanAndSaysSoInTheEvent() {
        InvestPlan plan = duePlan(1);
        plan.recordFailure();
        plan.recordFailure();
        ReflectionTestUtils.setField(plan, "nextRun", NOW.minusHours(1)); // due again
        due(plan);
        when(executor.execute(plan)).thenReturn(ExecutionResult.INSUFFICIENT_FUNDS);

        service.runDuePlans();

        assertThat(plan.getStatus()).isEqualTo(PlanStatus.SUSPENDED);
        ArgumentCaptor<Object> event = ArgumentCaptor.forClass(Object.class);
        verify(events).publishEvent(event.capture());
        assertThat(event.getValue()).isEqualTo(new PlanRunFailedEvent(1L, 3, true));
    }

    @Test
    void planPausedAfterBeingListedIsSkipped() {
        InvestPlan plan = duePlan(1);
        due(plan);
        plan.pause(); // investor paused it between the query and the run

        RunSummary summary = service.runDuePlans();

        assertThat(summary).isEqualTo(new RunSummary(1, 0, 0, 0));
        verify(executor, never()).execute(any());
    }

    @Test
    void oneBrokenPlanDoesNotStopTheOthers() {
        InvestPlan broken = duePlan(1);
        InvestPlan good = duePlan(2);
        due(broken, good);
        when(executor.execute(broken)).thenThrow(new RuntimeException("market data down"));
        when(executor.execute(good)).thenReturn(ExecutionResult.SUCCESS);

        RunSummary summary = service.runDuePlans();

        assertThat(summary).isEqualTo(new RunSummary(2, 1, 0, 1));
        assertThat(broken.getFailedRuns()).isZero(); // an error is not a failed run
    }

    @Test
    void nothingDueDoesNothing() {
        due();
        assertThat(service.runDuePlans()).isEqualTo(new RunSummary(0, 0, 0, 0));
        verify(executor, never()).execute(any());
    }
}
