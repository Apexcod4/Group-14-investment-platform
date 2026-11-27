package com.kernel.automation;

import java.time.LocalDateTime;

/** How often an auto-invest plan runs. */
public enum Frequency {
    WEEKLY,
    MONTHLY;

    /** The run after {@code previous}. */
    public LocalDateTime next(LocalDateTime previous) {
        return switch (this) {
            case WEEKLY -> previous.plusWeeks(1);
            case MONTHLY -> nextMonthly(previous);
        };
    }

    /**
     * TODO (your decision): what should "monthly" mean for a plan that starts on the 29th, 30th or 31st?
     *
     * <p>Right now this uses {@code plusMonths(1)}. Java clamps to the end of a short month, but the
     * date then drifts: Jan 31 becomes Feb 28, then Mar 28, then Apr 28 and never returns to the 31st.
     * See the notes in the pull request or chat for the options.
     */
    static LocalDateTime nextMonthly(LocalDateTime previous) {
        return previous.plusMonths(1);
    }
}
