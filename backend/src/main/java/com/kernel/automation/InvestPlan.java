package com.kernel.automation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A recurring investment for one portfolio.
 *
 * <p>All state changes go through the methods below so a plan can never reach an illegal state
 * (for example, a cancelled plan cannot be resumed). Times are UTC.
 */
@Entity
@Table(name = "invest_plans")
public class InvestPlan {

    /** After this many failed runs in a row the plan is suspended. */
    public static final int MAX_FAILED_RUNS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false)
    private Long portfolioId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @Column(name = "next_run", nullable = false)
    private LocalDateTime nextRun;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanStatus status;

    @Column(name = "failed_runs", nullable = false)
    private int failedRuns;

    protected InvestPlan() {} // required by JPA

    private InvestPlan(Long portfolioId, BigDecimal amount, Frequency frequency, LocalDateTime firstRun) {
        this.portfolioId = portfolioId;
        this.amount = amount;
        this.frequency = frequency;
        this.nextRun = firstRun;
        this.status = PlanStatus.ACTIVE;
        this.failedRuns = 0;
    }

    /** Creates a new ACTIVE plan. */
    public static InvestPlan create(Long portfolioId, BigDecimal amount, Frequency frequency, LocalDateTime firstRun) {
        if (portfolioId == null) {
            throw new IllegalArgumentException("portfolioId is required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }
        if (frequency == null) {
            throw new IllegalArgumentException("frequency is required");
        }
        if (firstRun == null) {
            throw new IllegalArgumentException("firstRun is required");
        }
        return new InvestPlan(portfolioId, amount, frequency, firstRun);
    }

    // ---- investor actions ----

    /** ACTIVE to PAUSED. */
    public void pause() {
        requireStatus("pause", PlanStatus.ACTIVE);
        status = PlanStatus.PAUSED;
    }

    /** PAUSED or SUSPENDED to ACTIVE. The failure count starts again from zero. */
    public void resume() {
        requireStatus("resume", PlanStatus.PAUSED, PlanStatus.SUSPENDED);
        status = PlanStatus.ACTIVE;
        failedRuns = 0;
    }

    /** Any non-final state to CANCELLED. */
    public void cancel() {
        if (status.isTerminal()) {
            throw new IllegalStateException("Cannot cancel a plan that is " + status);
        }
        status = PlanStatus.CANCELLED;
    }

    // ---- results of a scheduled run ----

    /** A run worked: clear the failure count and schedule the next run. */
    public void recordSuccess() {
        requireStatus("record a success for", PlanStatus.ACTIVE);
        failedRuns = 0;
        nextRun = frequency.next(nextRun);
    }

    /**
     * A run failed (for example, not enough money in the wallet). The next run moves to the
     * next period so the plan does not retry on every scheduler tick. After
     * {@link #MAX_FAILED_RUNS} failures in a row the plan is SUSPENDED.
     */
    public void recordFailure() {
        requireStatus("record a failure for", PlanStatus.ACTIVE);
        failedRuns++;
        nextRun = frequency.next(nextRun);
        if (failedRuns >= MAX_FAILED_RUNS) {
            status = PlanStatus.SUSPENDED;
        }
    }

    /** ACTIVE to COMPLETED, for plans that reach an end date (not used yet). */
    public void complete() {
        requireStatus("complete", PlanStatus.ACTIVE);
        status = PlanStatus.COMPLETED;
    }

    private void requireStatus(String action, PlanStatus... allowed) {
        for (PlanStatus s : allowed) {
            if (status == s) {
                return;
            }
        }
        throw new IllegalStateException("Cannot " + action + " a plan that is " + status);
    }

    public Long getId() { return id; }
    public Long getPortfolioId() { return portfolioId; }
    public BigDecimal getAmount() { return amount; }
    public Frequency getFrequency() { return frequency; }
    public LocalDateTime getNextRun() { return nextRun; }
    public PlanStatus getStatus() { return status; }
    public int getFailedRuns() { return failedRuns; }
}
