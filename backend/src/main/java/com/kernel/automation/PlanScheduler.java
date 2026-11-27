package com.kernel.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Triggers {@link PlanRunService} on a schedule (default: every hour on the hour).
 *
 * <p>Switched off by default. Turn it on with {@code kernel.automation.enabled=true} once a real
 * {@link PlanExecutor} exists. Note for a real deployment: with two copies of the app running,
 * both would run the same plans; a database lock would be needed.
 */
@Component
@ConditionalOnProperty(name = "kernel.automation.enabled", havingValue = "true")
public class PlanScheduler {

    private static final Logger log = LoggerFactory.getLogger(PlanScheduler.class);

    private final PlanRunService runService;

    public PlanScheduler(PlanRunService runService) {
        this.runService = runService;
    }

    @Scheduled(cron = "${kernel.automation.cron:0 0 * * * *}")
    public void run() {
        RunSummary s = runService.runDuePlans();
        log.info("Plan run finished: due={}, succeeded={}, failed={}, errors={}",
                s.due(), s.succeeded(), s.failed(), s.errors());
    }
}
