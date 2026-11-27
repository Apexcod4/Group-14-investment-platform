package com.kernel.automation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionOperations;

/**
 * Runs every auto-invest plan that is due.
 *
 * <p>Each plan runs in its own transaction, so one failure never undoes or blocks the others.
 * Only created when {@code kernel.automation.enabled=true}, which needs a real {@link PlanExecutor} bean.
 */
@Service
@ConditionalOnProperty(name = "kernel.automation.enabled", havingValue = "true")
public class PlanRunService {

    private static final Logger log = LoggerFactory.getLogger(PlanRunService.class);

    private enum Outcome { SUCCEEDED, FAILED, SKIPPED }

    private final InvestPlanRepository plans;
    private final PlanExecutor executor;
    private final TransactionOperations tx;
    private final ApplicationEventPublisher events;
    private final Clock clock;

    public PlanRunService(InvestPlanRepository plans, PlanExecutor executor, TransactionOperations tx,
                          ApplicationEventPublisher events, Clock clock) {
        this.plans = plans;
        this.executor = executor;
        this.tx = tx;
        this.events = events;
        this.clock = clock;
    }

    public RunSummary runDuePlans() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Long> dueIds = plans.findByStatusAndNextRunLessThanEqual(PlanStatus.ACTIVE, now).stream()
                .map(InvestPlan::getId)
                .toList();

        int succeeded = 0;
        int failed = 0;
        int errors = 0;
        for (Long id : dueIds) {
            try {
                Outcome outcome = tx.execute(status -> runOne(id, now));
                if (outcome == Outcome.SUCCEEDED) {
                    succeeded++;
                } else if (outcome == Outcome.FAILED) {
                    failed++;
                }
            } catch (RuntimeException e) {
                errors++;
                log.error("Plan {} could not be run and will be retried on the next pass", id, e);
            }
        }
        return new RunSummary(dueIds.size(), succeeded, failed, errors);
    }

    private Outcome runOne(Long id, LocalDateTime now) {
        InvestPlan plan = plans.findById(id).orElse(null);
        // Re-check inside the transaction: the investor may have paused or cancelled it meanwhile.
        if (plan == null || plan.getStatus() != PlanStatus.ACTIVE || plan.getNextRun().isAfter(now)) {
            return Outcome.SKIPPED;
        }

        ExecutionResult result = executor.execute(plan);
        if (result == ExecutionResult.SUCCESS) {
            plan.recordSuccess();
            plans.save(plan);
            return Outcome.SUCCEEDED;
        }

        plan.recordFailure();
        plans.save(plan);
        events.publishEvent(new PlanRunFailedEvent(
                plan.getId(), plan.getFailedRuns(), plan.getStatus() == PlanStatus.SUSPENDED));
        return Outcome.FAILED;
    }
}
