# Kernel back end

Spring Boot 3, Java 21, PostgreSQL, Flyway.

## Run locally

```bash
# 1. database (from the repo root)
docker compose up -d

# 2. API (from this folder)
mvn spring-boot:run
```

Check it: <http://localhost:8080/api/health> and <http://localhost:8080/api/assets>.

## Test

```bash
mvn verify
```

Tests run on an in-memory H2 database (profile `test`), so no PostgreSQL is needed.

## Package layout (one owner per feature)

| Package | Owner | Purpose |
|---|---|---|
| `com.kernel.common` | shared | health check, shared helpers |
| `com.kernel.asset` | Part 3 | asset catalog and seeding |
| `com.kernel.marketdata` | Part 3 | `MarketDataProvider` and providers |
| `com.kernel.auth` | Part 1 | users, login, JWT (to add) |
| `com.kernel.wallet` | Part 2 | wallet, payments, transactions (to add) |
| `com.kernel.portfolio` | Part 4 | portfolios, holdings, goals (to add) |
| `com.kernel.automation` | Part 5 | invest plans and the scheduler (to add) |

## Database changes

Never edit an applied migration. Add a new file in `src/main/resources/db/migration`
named `V2__what_changed.sql`, `V3__...` and so on. Hibernate only validates the schema,
Flyway owns it.

## Market data

`src/main/resources/data/sample-prices.csv` holds illustrative sample prices.
They are placeholders, not real market data. To add a real source, implement
`MarketDataProvider` and mark the new class `@Primary`.
