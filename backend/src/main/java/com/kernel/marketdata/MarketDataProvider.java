package com.kernel.marketdata;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for market prices (see the class diagram).
 *
 * <p>Implementations: {@link SampleDataProvider} (default, offline).
 * Planned: FinnhubProvider for US stocks, an NGX provider for Nigerian stocks.
 * Services depend only on this interface, so a provider can be swapped without
 * changing RecommendationService or the portfolio code.
 */
public interface MarketDataProvider {

    /** Latest known price for a symbol, or empty if the symbol is unknown. */
    Optional<Quote> getQuote(String symbol);

    /** Latest known prices for several symbols. Unknown symbols are skipped. */
    default List<Quote> getQuotes(Collection<String> symbols) {
        return symbols.stream().map(this::getQuote).flatMap(Optional::stream).toList();
    }
}
