package com.kernel.automation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class FrequencyTest {

    @Test
    void weeklyAddsSevenDays() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 2, 9, 0);
        assertThat(Frequency.WEEKLY.next(start)).isEqualTo(LocalDateTime.of(2026, 3, 9, 9, 0));
    }

    @Test
    void monthlyKeepsTheSameDayForOrdinaryDates() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 15, 9, 0);
        assertThat(Frequency.MONTHLY.next(start)).isEqualTo(LocalDateTime.of(2026, 2, 15, 9, 0));
    }

    @Test
    void monthlyDoesNotSkipPastTheEndOfAShortMonth() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 31, 9, 0);
        LocalDateTime next = Frequency.MONTHLY.next(start);
        assertThat(next.getMonthValue()).isEqualTo(2); // February, not March
    }
}
