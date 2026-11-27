package com.kernel.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Offline market data read from {@code data/sample-prices.csv}.
 *
 * <p>The values are illustrative placeholders for development and demos.
 * They are NOT real market prices.
 */
@Component
public class SampleDataProvider implements MarketDataProvider {

    private static final String RESOURCE = "/data/sample-prices.csv";

    private final Map<String, SampleAsset> bySymbol = new LinkedHashMap<>();
    private final Instant loadedAt = Instant.now();

    public SampleDataProvider() {
        load();
    }

    @Override
    public Optional<Quote> getQuote(String symbol) {
        SampleAsset a = bySymbol.get(symbol == null ? "" : symbol.toUpperCase());
        if (a == null) {
            return Optional.empty();
        }
        return Optional.of(new Quote(a.symbol(), a.price(), a.currency(), loadedAt));
    }

    /** Every asset in the sample file, in file order. */
    public List<SampleAsset> catalog() {
        return List.copyOf(bySymbol.values());
    }

    private void load() {
        try (InputStream in = SampleDataProvider.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing resource " + RESOURCE);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            boolean header = true;
            List<String> problems = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                if (header) { // first real line is the column header
                    header = false;
                    continue;
                }
                String[] c = line.split(",", -1);
                if (c.length != 9) {
                    problems.add("bad row: " + line);
                    continue;
                }
                SampleAsset a = new SampleAsset(
                        c[0].trim().toUpperCase(), c[1].trim(), c[2].trim(), c[3].trim(),
                        c[4].trim(), c[5].trim(), new BigDecimal(c[6].trim()),
                        new BigDecimal(c[7].trim()), Integer.parseInt(c[8].trim()));
                bySymbol.put(a.symbol(), a);
            }
            if (!problems.isEmpty()) {
                throw new IllegalStateException("Invalid sample data: " + problems);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
