package com.kernel.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** A tradable asset: stock, ETF or fixed-income product. */
@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(name = "asset_class", nullable = false)
    private String assetClass;

    private String sector;
    private String exchange;

    @Column(nullable = false)
    private String currency;

    @Column(name = "last_price", nullable = false)
    private BigDecimal lastPrice;

    @Column(name = "dividend_yield", nullable = false)
    private BigDecimal dividendYield;

    @Column(name = "risk_rating", nullable = false)
    private int riskRating;

    protected Asset() {} // required by JPA

    public Asset(String symbol, String name, String assetClass, String sector, String exchange,
                 String currency, BigDecimal lastPrice, BigDecimal dividendYield, int riskRating) {
        this.symbol = symbol;
        this.name = name;
        this.assetClass = assetClass;
        this.sector = sector;
        this.exchange = exchange;
        this.currency = currency;
        this.lastPrice = lastPrice;
        this.dividendYield = dividendYield;
        this.riskRating = riskRating;
    }

    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getAssetClass() { return assetClass; }
    public String getSector() { return sector; }
    public String getExchange() { return exchange; }
    public String getCurrency() { return currency; }
    public BigDecimal getLastPrice() { return lastPrice; }
    public BigDecimal getDividendYield() { return dividendYield; }
    public int getRiskRating() { return riskRating; }

    public void updatePrice(BigDecimal newPrice) { this.lastPrice = newPrice; }
}
