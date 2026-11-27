package com.kernel.automation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestPlanRepository extends JpaRepository<InvestPlan, Long> {

    /** Plans of the given status whose next run is at or before {@code time}. */
    List<InvestPlan> findByStatusAndNextRunLessThanEqual(PlanStatus status, LocalDateTime time);

    List<InvestPlan> findByPortfolioId(Long portfolioId);
}
