# Kernel – Work Breakdown (5 parts, 5 team members)

The Product Owner prioritises the backlog and accepts work. The five developers each own one part of Kernel. Sprints are 2 weeks. IDs match the user stories in [scrum.md](scrum.md) and the board in [kanban.md](kanban.md).

## 1. The five parts

| Part | Owner | Scope | Stories | Points |
|---|---|---|---|---|
| **1. Accounts, Profile and Admin** | Member A | Register, login, sessions, risk quiz, budget and goal input, learning hub, admin panel | US-01, US-03, US-04, US-15, US-19 | 19 |
| **2. Wallet and Payments** | Member B | Wallet, test-mode funding, transactions, failed-run notices, price alerts, practice mode | US-02, US-11, US-14, US-16 | 18 |
| **3. Recommendation Engine** | Member C | Asset catalog, market data, allocation strategies, "Why this?" reasons, ethical filter, rebalancing | US-05, US-18, US-12 | 24 |
| **4. Portfolio and Goals** | Member D | Accept a recommendation, portfolio and holdings view, savings goals, dividend tracker | US-06, US-07, US-13, US-17 | 21 |
| **5. Automation and Deployment** | Member E | Repo skeleton, CI, database migrations, auto-invest plans, scheduler, pause/resume/cancel, deployment, final demo | US-08, US-09, US-10, US-20 | 27 |

Total: 109 points, about 22 per person. Replace "Member A–E" with real names.

## 2. What each part builds (technical)

| Part | Back end | Front end | Database tables |
|---|---|---|---|
| 1 | Spring Security + JWT, `UserService`, `RiskProfileService`, admin endpoints | Register, login, quiz, budget form, learning pages, admin screens | `users`, `risk_profiles` |
| 2 | `Wallet.credit/debit`, `PaymentGateway` interface + Paystack test impl, `AlertService`, `Notifier` | Fund wallet, balance, history, alerts | `wallets`, `transactions`, `price_alerts` |
| 3 | `AllocationStrategy` (Conservative, Balanced, Growth), `MarketDataProvider`, `RecommendationService`, `RebalanceService` | Recommendation screen with reasons | `assets`, `recommendations`, `recommended_items` |
| 4 | `PortfolioService`, holdings, goals, dividends | Portfolio dashboard, goals, dividends | `portfolios`, `holdings`, `goals` |
| 5 | `InvestPlan`, `PlanScheduler`, execute simulated orders, CI/CD, migrations | Plan setup and management screens | `invest_plans` + all migrations |

## 3. Sprint 1 plan (2 weeks)

Goal: a working sign-up → fund wallet → risk quiz path, with the foundations for everything else.

| Member | Sprint 1 deliverable | Done when |
|---|---|---|
| A | US-01 register/login/session and US-03 risk quiz | A new user can register, log in, take the quiz and see a saved category |
| B | US-02 wallet funding (test mode) with transaction history | Paying in test mode raises the balance by the exact amount; duplicates credited once |
| C | Foundations for US-05: `AllocationStrategy` and `MarketDataProvider` interfaces, seeded asset catalog, sample NGX data, one working strategy with unit tests | Given a score and budget, the strategy returns weights summing to 100% |
| D | Foundations for US-07: portfolio and holdings tables, portfolio API, dashboard screen with mock data | The dashboard renders holdings from the API |
| E | Project skeleton, database migrations from the ER diagram, CI on pull requests, test deployment, `PlanScheduler` skeleton | `main` builds, tests run and a deployed test URL is live |

## 4. Shared contracts (agree in the first 2 days)

| Contract | Owner | Used by |
|---|---|---|
| REST API shape (paths, request/response JSON, error format) | Member E | All |
| Database schema and migrations | Member E | All |
| `User`/JWT claims (who is logged in) | Member A | All |
| `Wallet` interface: `getBalance`, `credit`, `debit` | Member B | C, D, E |
| `Recommendation` and `RecommendedItem` data shape | Member C | A, D |
| `Portfolio` and `Holding` data shape | Member D | C, E |

Rule: change a contract only by pull request that every affected member approves.

## 5. Dependencies

| Part | Needs from | When |
|---|---|---|
| 1 Accounts | Part 5 skeleton and migrations | Day 1–2 |
| 2 Wallet | Part 1 login to protect endpoints | Day 3 (use a stub until then) |
| 3 Engine | Part 1 quiz result; Part 5 migrations | Sprint 2 |
| 4 Portfolio | Part 3 recommendation shape; Part 2 wallet | Sprint 2–3 |
| 5 Automation | Parts 2, 3, 4 | Sprint 4 |

Mock the missing piece (stub service or JSON file) so nobody is blocked.

## 6. Ways of working in the sprint

- Daily Scrum, 15 minutes.
- Each member works on their own branch: `feature/US-05-recommendations`.
- Every pull request needs one reviewer from a different part.
- Members pair on cross-part contracts.
- Sprint review on the last day: each member demos their part to the Product Owner.
- The Product Owner accepts a story only when its acceptance criteria and the Definition of Done in [scrum.md](scrum.md) are met.
