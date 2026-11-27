package com.kernel.automation;

/** Lifecycle of an auto-invest plan (see docs/uml/09-state-invest-plan.png). */
public enum PlanStatus {
    ACTIVE,
    PAUSED,
    SUSPENDED,
    COMPLETED,
    CANCELLED;

    /** COMPLETED and CANCELLED are final: no further change is allowed. */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
