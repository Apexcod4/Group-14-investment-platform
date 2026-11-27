package com.kernel.asset;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Read-only asset catalog, used by the front end and by the recommendation engine. */
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetRepository assets;

    public AssetController(AssetRepository assets) {
        this.assets = assets;
    }

    @GetMapping
    public List<Asset> list(@RequestParam(required = false) String assetClass) {
        return assetClass == null ? assets.findAll() : assets.findByAssetClass(assetClass);
    }
}
