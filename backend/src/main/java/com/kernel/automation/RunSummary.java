package com.kernel.automation;

/** Result of one scheduler pass. */
public record RunSummary(int due, int succeeded, int failed, int errors) {}
