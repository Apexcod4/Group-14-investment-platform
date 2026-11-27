package com.kernel;

import static org.assertj.core.api.Assertions.assertThat;

import com.kernel.asset.AssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Boots the whole app on H2: proves Flyway migrations, JPA mapping and the seeder all work. */
@SpringBootTest
@ActiveProfiles("test")
class KernelApplicationTests {

    @Autowired
    AssetRepository assets;

    @Test
    void contextLoadsAndAssetsAreSeeded() {
        assertThat(assets.count()).isGreaterThanOrEqualTo(20);
        assertThat(assets.findBySymbol("DANGCEM")).isPresent();
    }
}
