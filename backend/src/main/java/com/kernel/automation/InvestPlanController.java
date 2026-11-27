package com.kernel.automation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for auto-invest plans.
 *
 * <p>TODO (after Part 1 adds login): only let the logged-in investor see and change plans of
 * their own portfolios.
 */
@RestController
@RequestMapping("/api/plans")
public class InvestPlanController {

    public record CreatePlanRequest(
            @NotNull Long portfolioId,
            @NotNull @Positive BigDecimal amount,
            @NotNull Frequency frequency,
            @NotNull LocalDateTime firstRun) {}

    public record PlanResponse(Long id, Long portfolioId, BigDecimal amount, Frequency frequency,
                               LocalDateTime nextRun, PlanStatus status, int failedRuns) {
        static PlanResponse from(InvestPlan p) {
            return new PlanResponse(p.getId(), p.getPortfolioId(), p.getAmount(), p.getFrequency(),
                    p.getNextRun(), p.getStatus(), p.getFailedRuns());
        }
    }

    private final InvestPlanService service;

    public InvestPlanController(InvestPlanService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponse create(@Valid @RequestBody CreatePlanRequest r) {
        return PlanResponse.from(service.create(r.portfolioId(), r.amount(), r.frequency(), r.firstRun()));
    }

    @GetMapping("/{id}")
    public PlanResponse get(@PathVariable Long id) {
        return PlanResponse.from(service.get(id));
    }

    @PostMapping("/{id}/pause")
    public PlanResponse pause(@PathVariable Long id) {
        return PlanResponse.from(service.pause(id));
    }

    @PostMapping("/{id}/resume")
    public PlanResponse resume(@PathVariable Long id) {
        return PlanResponse.from(service.resume(id));
    }

    @PostMapping("/{id}/cancel")
    public PlanResponse cancel(@PathVariable Long id) {
        return PlanResponse.from(service.cancel(id));
    }
}
