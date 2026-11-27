package com.kernel.automation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/** Runs against the real schema (Flyway on H2): proves the entity mapping and the "due plans" query. */
@SpringBootTest
@ActiveProfiles("test")
class InvestPlanRepositoryTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 2, 12, 0);

    @Autowired InvestPlanRepository plans;
    @Autowired InvestPlanService service;
    @Autowired JdbcTemplate jdbc;

    private Long portfolioId;

    @BeforeEach
    void createPortfolio() {
        jdbc.update("insert into users (name, email, password_hash) values ('Test', 'plan-test@example.com', 'x')");
        Long userId = jdbc.queryForObject("select id from users where email = 'plan-test@example.com'", Long.class);
        jdbc.update("insert into portfolios (user_id, name) values (?, 'Test portfolio')", userId);
        portfolioId = jdbc.queryForObject("select id from portfolios where user_id = ?", Long.class, userId);
    }

    @AfterEach
    void cleanUp() {
        jdbc.update("delete from invest_plans");
        jdbc.update("delete from portfolios");
        jdbc.update("delete from users where email = 'plan-test@example.com'");
    }

    @Test
    void savesAndReloadsAPlan() {
        InvestPlan saved = service.create(portfolioId, new BigDecimal("2500.50"), Frequency.MONTHLY, NOW);

        InvestPlan loaded = plans.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getAmount()).isEqualByComparingTo("2500.50");
        assertThat(loaded.getFrequency()).isEqualTo(Frequency.MONTHLY);
        assertThat(loaded.getStatus()).isEqualTo(PlanStatus.ACTIVE);
        assertThat(loaded.getNextRun()).isEqualTo(NOW);
    }

    @Test
    void findsOnlyActivePlansThatAreDue() {
        InvestPlan dueNow = service.create(portfolioId, BigDecimal.TEN, Frequency.WEEKLY, NOW.minusDays(1));
        InvestPlan later = service.create(portfolioId, BigDecimal.TEN, Frequency.WEEKLY, NOW.plusDays(1));
        InvestPlan paused = service.create(portfolioId, BigDecimal.TEN, Frequency.WEEKLY, NOW.minusDays(1));
        service.pause(paused.getId());

        List<InvestPlan> due = plans.findByStatusAndNextRunLessThanEqual(PlanStatus.ACTIVE, NOW);

        assertThat(due).extracting(InvestPlan::getId)
                .contains(dueNow.getId())
                .doesNotContain(later.getId(), paused.getId());
    }

    @Test
    void statusChangesArePersisted() {
        InvestPlan plan = service.create(portfolioId, BigDecimal.TEN, Frequency.WEEKLY, NOW);
        service.pause(plan.getId());
        assertThat(plans.findById(plan.getId()).orElseThrow().getStatus()).isEqualTo(PlanStatus.PAUSED);
        service.cancel(plan.getId());
        assertThat(plans.findById(plan.getId()).orElseThrow().getStatus()).isEqualTo(PlanStatus.CANCELLED);
    }
}
