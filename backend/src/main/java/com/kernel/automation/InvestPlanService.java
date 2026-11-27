package com.kernel.automation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Investor-facing actions on plans: create, view, pause, resume, cancel. */
@Service
@Transactional
public class InvestPlanService {

    private final InvestPlanRepository plans;

    public InvestPlanService(InvestPlanRepository plans) {
        this.plans = plans;
    }

    public InvestPlan create(Long portfolioId, BigDecimal amount, Frequency frequency, LocalDateTime firstRun) {
        return plans.save(InvestPlan.create(portfolioId, amount, frequency, firstRun));
    }

    @Transactional(readOnly = true)
    public InvestPlan get(Long id) {
        return plans.findById(id).orElseThrow(() -> new NoSuchElementException("Plan " + id + " not found"));
    }

    public InvestPlan pause(Long id) {
        InvestPlan plan = get(id);
        plan.pause();
        return plan;
    }

    public InvestPlan resume(Long id) {
        InvestPlan plan = get(id);
        plan.resume();
        return plan;
    }

    public InvestPlan cancel(Long id) {
        InvestPlan plan = get(id);
        plan.cancel();
        return plan;
    }
}
