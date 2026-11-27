package com.kernel.automation;

/**
 * Published when a scheduled run fails. The notification code (Part 2) listens for this
 * and tells the investor to top up their wallet, so automation does not depend on it.
 */
public record PlanRunFailedEvent(Long planId, int failedRuns, boolean suspended) {}
