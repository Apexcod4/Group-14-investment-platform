package com.kernel.asset;

import com.kernel.marketdata.SampleAsset;
import com.kernel.marketdata.SampleDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Fills the assets table from the sample file on first start.
 * Does nothing when the table already has rows, so it is safe to restart.
 */
@Component
public class AssetSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AssetSeeder.class);

    private final AssetRepository assets;
    private final SampleDataProvider sample;

    public AssetSeeder(AssetRepository assets, SampleDataProvider sample) {
        this.assets = assets;
        this.sample = sample;
    }

    @Override
    public void run(String... args) {
        if (assets.count() > 0) {
            return;
        }
        for (SampleAsset s : sample.catalog()) {
            assets.save(new Asset(s.symbol(), s.name(), s.assetClass(), s.sector(), s.exchange(),
                    s.currency(), s.price(), s.dividendYield(), s.riskRating()));
        }
        log.info("Seeded {} sample assets", sample.catalog().size());
    }
}
