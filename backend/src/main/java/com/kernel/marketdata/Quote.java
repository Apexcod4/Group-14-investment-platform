package com.kernel.marketdata;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * A price for one asset at one moment.
 * Money is always BigDecimal, never double.
 */
public record Quote(String symbol, BigDecimal price, String currency, Instant asOf) {}
