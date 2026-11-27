package com.kernel.automation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class InvestPlanTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 3, 2, 9, 0);

    private InvestPlan newPlan() {
        return InvestPlan.create(1L, new BigDecimal("5000"), Frequency.WEEKLY, START);
    }

    @Test
    void newPlanIsActiveWithNoFailures() {
        InvestPlan plan = newPlan();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
        assertThat(plan.getFailedRuns()).isZero();
        assertThat(plan.getNextRun()).isEqualTo(START);
    }

    @Test
    void rejectsInvalidInput() {
        assertThatThrownBy(() -> InvestPlan.create(null, BigDecimal.TEN, Frequency.WEEKLY, START))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InvestPlan.create(1L, BigDecimal.ZERO, Frequency.WEEKLY, START))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InvestPlan.create(1L, new BigDecimal("-1"), Frequency.WEEKLY, START))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InvestPlan.create(1L, BigDecimal.TEN, null, START))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InvestPlan.create(1L, BigDecimal.TEN, Frequency.WEEKLY, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void successMovesToTheNextRunAndClearsFailures() {
        InvestPlan plan = newPlan();
        plan.recordFailure();
        plan.recordSuccess();
        assertThat(plan.getFailedRuns()).isZero();
        assertThat(plan.getNextRun()).isEqualTo(START.plusWeeks(2)); // failure moved it once, success again
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
    }

    @Test
    void threeFailuresInARowSuspendThePlan() {
        InvestPlan plan = newPlan();
        plan.recordFailure();
        plan.recordFailure();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
        plan.recordFailure();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.SUSPENDED);
        assertThat(plan.getFailedRuns()).isEqualTo(InvestPlan.MAX_FAILED_RUNS);
    }

    @Test
    void aSuccessBetweenFailuresResetsTheCount() {
        InvestPlan plan = newPlan();
        plan.recordFailure();
        plan.recordFailure();
        plan.recordSuccess();
        plan.recordFailure();
        plan.recordFailure();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
    }

    @Test
    void pauseAndResume() {
        InvestPlan plan = newPlan();
        plan.pause();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.PAUSED);
        plan.resume();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
    }

    @Test
    void resumingASuspendedPlanClearsFailures() {
        InvestPlan plan = newPlan();
        plan.recordFailure();
        plan.recordFailure();
        plan.recordFailure();
        plan.resume();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
        assertThat(plan.getFailedRuns()).isZero();
    }

    @Test
    void cannotPauseOrResumeInTheWrongState() {
        InvestPlan plan = newPlan();
        assertThatThrownBy(plan::resume).isInstanceOf(IllegalStateException.class); // already active
        plan.pause();
        assertThatThrownBy(plan::pause).isInstanceOf(IllegalStateException.class); // already paused
    }

    @Test
    void cancelledPlanIsFinal() {
        InvestPlan plan = newPlan();
        plan.cancel();
        assertThat(plan.getStatus()).isEqualTo(PlanStatus.CANCELLED);
        assertThatThrownBy(plan::resume).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(plan::pause).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(plan::cancel).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void pausedPlanCannotRecordRunResults() {
        InvestPlan plan = newPlan();
        plan.pause();
        assertThatThrownBy(plan::recordSuccess).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(plan::recordFailure).isInstanceOf(IllegalStateException.class);
    }
}
