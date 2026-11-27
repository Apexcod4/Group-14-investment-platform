package com.kernel.marketdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class SampleDataProviderTest {

    private final SampleDataProvider provider = new SampleDataProvider();

    @Test
    void loadsTheSampleCatalog() {
        assertThat(provider.catalog()).hasSizeGreaterThanOrEqualTo(20);
    }

    @Test
    void returnsAQuoteForAKnownSymbolIgnoringCase() {
        Quote q = provider.getQuote("gtco").orElseThrow();
        assertThat(q.symbol()).isEqualTo("GTCO");
        assertThat(q.currency()).isEqualTo("NGN");
        assertThat(q.price()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void returnsEmptyForAnUnknownSymbol() {
        assertThat(provider.getQuote("NOPE")).isEmpty();
        assertThat(provider.getQuote(null)).isEmpty();
    }

    @Test
    void getQuotesSkipsUnknownSymbols() {
        List<Quote> quotes = provider.getQuotes(List.of("AAPL", "NOPE", "MTNN"));
        assertThat(quotes).extracting(Quote::symbol).containsExactly("AAPL", "MTNN");
    }

    @Test
    void coversEveryAssetClassTheEngineNeeds() {
        assertThat(provider.catalog()).extracting(SampleAsset::assetClass)
                .contains("NG_STOCK", "US_STOCK", "ETF", "FIXED_INCOME");
    }
}
