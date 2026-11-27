package com.kernel.marketdata;

import java.math.BigDecimal;

/** One row of data/sample-prices.csv. */
public record SampleAsset(
        String symbol,
        String name,
        String assetClass,
        String sector,
        String exchange,
        String currency,
        BigDecimal price,
        BigDecimal dividendYield,
        int riskRating) {}
