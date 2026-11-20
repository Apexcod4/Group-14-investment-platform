# Kernel – Scrum Plan

**Course:** CPE434 – Computer Software Engineering I
**Project:** Kernel, a micro-investing web app that recommends and automates investments
**Process model:** Scrum (incremental, iterative delivery) with 2-week sprints
**Duration:** 12 weeks = 6 sprints

---

## 1. Product vision

> For young Nigerians who want to start investing but don't know what to buy, Kernel is a micro-investing platform that takes a budget, goal and risk level, recommends a diversified portfolio with plain-language reasons, and automates the investing. Unlike a plain savings app, Kernel explains *why* it recommends each asset.

The prototype uses **simulated trading** only.

---

## 2. Scrum team and roles

| Role | Name | Responsibility |
|---|---|---|
| Product Owner | _TBD_ | Owns and orders the product backlog, accepts or rejects work at each review |
| Scrum Master | _TBD_ | Runs the events, removes blockers, protects the sprint, rotates each 2 sprints |
| Developer (Back end) | _TBD_ | Java Spring Boot services, database, scheduler |
| Developer (Front end) | _TBD_ | React / Next.js screens |
| Developer (QA / Docs) | _TBD_ | Test plans, JUnit/Postman tests, documentation, UML upkeep |

> Replace the _TBD_ names with the group members. Every member codes, and every member tests.

---

## 3. Scrum events

| Event | When | Length | Purpose |
|---|---|---|---|
| Sprint Planning | Day 1 of each sprint | 1.5 h | Choose the sprint goal and the backlog items to deliver |
| Daily Scrum | Every day | 15 min (chat or call) | What I did, what I'll do, what blocks me |
| Sprint Review | Last day of sprint | 1 h | Demo the working increment to the lecturer or stakeholders and collect feedback |
| Sprint Retrospective | After the review | 45 min | What went well, what to improve, one action for next sprint |
| Backlog Refinement | Mid-sprint | 45 min | Break down and estimate upcoming stories |

---

## 4. Scrum artifacts

- **Product Backlog:** the ordered list of everything Kernel needs (section 6).
- **Sprint Backlog:** the stories chosen for the current sprint plus their tasks.
- **Increment:** the working, tested software at the end of each sprint.
- **Sprint board columns:** `To Do` → `In Progress` → `In Review` → `Testing` → `Done`.

### Definition of Ready (a story can enter a sprint when)
- It has a user story, acceptance criteria and a point estimate.
- It is small enough to finish in one sprint.
- Its dependencies are known.

### Definition of Done (a story is Done when)
- Code is merged to `main` through a reviewed pull request.
- Unit tests pass (JUnit/Mockito) and the feature works end to end.
- Acceptance criteria are met and the Product Owner has checked them.
- No known critical bugs remain.
- Relevant UML diagrams and docs are updated.
- It is deployed to the test environment.

---

## 5. Estimation

- Story points use the Fibonacci scale: 1, 2, 3, 5, 8, 13.
- Estimate with Planning Poker during refinement.
- Starting velocity assumption: **20 points per sprint**, to be corrected after Sprint 1 using the real number.

---

## 6. Product backlog (prioritised)

| ID | User story | Priority | Points |
|---|---|---|---|
| US-01 | As a user, I can register and log in securely so my account is protected | Must | 5 |
| US-02 | As an investor, I can fund my wallet (test-mode payment) so I have money to invest | Must | 5 |
| US-03 | As an investor, I can take a risk quiz so Kernel knows my risk level | Must | 3 |
| US-04 | As an investor, I can enter my budget, goal and horizon so I get a suitable plan | Must | 3 |
| US-05 | As an investor, I get a recommended portfolio with a "Why this?" reason per asset | Must | 13 |
| US-06 | As an investor, I can accept a recommendation and see it as my portfolio | Must | 8 |
| US-07 | As an investor, I can view my portfolio value and holdings | Must | 5 |
| US-08 | As an investor, I can set up an auto-invest plan (weekly or monthly) | Must | 8 |
| US-09 | As the system, I run due plans and record transactions automatically | Must | 8 |
| US-10 | As an investor, I can pause, resume or cancel a plan | Should | 3 |
| US-11 | As an investor, I get notified when a run fails for low funds | Should | 3 |
| US-12 | As an investor, my portfolio is rebalanced automatically when it drifts above 5 points | Should | 8 |
| US-13 | As an investor, I can set savings goals and track progress | Should | 5 |
| US-14 | As an investor, I can set price alerts on assets | Could | 5 |
| US-15 | As an investor, I can read short learning articles on investing | Could | 3 |
| US-16 | As an investor, I can use practice mode with virtual money | Could | 5 |
| US-17 | As an investor, I can see a dividend tracker | Could | 3 |
| US-18 | As an investor, I can apply an ethical filter to recommendations | Could | 3 |
| US-19 | As an admin, I can manage assets and view users and transactions | Should | 5 |
| US-20 | As a team, we deploy the app and write the final report and demo | Must | 8 |

### Acceptance criteria examples

**US-05 Recommendation**
- Given a budget, risk level and horizon, the system returns a portfolio whose weights sum to 100%.
- Each recommended asset has a plain-language reason.
- The same inputs always give the same output.
- If market data is unavailable, cached prices are used and a warning is shown.

**US-09 Run due plans**
- A plan with `nextRun <= now` and enough wallet funds creates a SETTLED transaction and advances `nextRun`.
- With insufficient funds, `failedRuns` increases and the investor is notified.
- After 3 failed runs the plan becomes SUSPENDED.

---

## 7. Release and sprint plan

| Sprint | Weeks | Sprint goal | Stories | Points |
|---|---|---|---|---|
| 0 | 1 | Setup: repo, branching, CI, database schema, project skeleton, UML finalised | – | – |
| 1 | 2–3 | Users can sign up, log in and fund a wallet | US-01, US-02, US-03 | 13 |
| 2 | 4–5 | Users get a recommended portfolio with reasons | US-04, US-05 | 16 |
| 3 | 6–7 | Users can accept a plan and view their portfolio | US-06, US-07, US-19 | 18 |
| 4 | 8–9 | Automation works: auto-invest plans run on schedule | US-08, US-09, US-10, US-11 | 22 |
| 5 | 10–11 | Rebalancing, goals and extras | US-12, US-13, US-14, plus one of US-15–US-18 | 20 |
| 6 | 12 | Harden, test, deploy, document and demo | US-20, bug fixes | 8+ |

Sprint 0 is a shorter setup sprint, so the 12 weeks are covered by Sprint 0 plus six delivery sprints with the final one shortened. Adjust to your lecturer's calendar.

**Releases**
- **MVP (end of Sprint 3):** sign up, fund, recommend, accept, view portfolio.
- **Full prototype (end of Sprint 5):** MVP plus automation, rebalancing, goals and alerts.

---

## 8. Sprint 1 backlog (example, fill in at planning)

**Sprint goal:** Users can sign up, log in and fund a wallet.

| Task | Story | Owner | Est. (h) | Status |
|---|---|---|---|---|
| Design `users` and `wallets` tables | US-01 | _TBD_ | 3 | To Do |
| Spring Security + JWT login endpoint | US-01 | _TBD_ | 8 | To Do |
| Register and login screens | US-01 | _TBD_ | 6 | To Do |
| Wallet `credit()` / `debit()` with validation | US-02 | _TBD_ | 4 | To Do |
| Paystack test-mode integration | US-02 | _TBD_ | 8 | To Do |
| Risk quiz form and scoring | US-03 | _TBD_ | 6 | To Do |
| Unit tests for auth, wallet, scoring | All | _TBD_ | 6 | To Do |

---

## 9. Tracking progress

- **Sprint burndown:** story points remaining per day.
- **Velocity:** points completed per sprint, used to plan the next one.
- **Board:** a Kanban board (GitHub Projects or Trello) with the columns in section 4.
- **Communication:** a group chat for daily updates and a weekly meeting with the lecturer if required.

---

## 10. Risks and mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| Market data API limits or fails | Recommendations unavailable | Cache prices; keep a sample NGX dataset as a fallback |
| Payment test mode behaves differently | Wallet funding breaks | Hide payments behind a `PaymentGateway` interface and mock it in tests |
| Team member unavailable (exams, power, data) | Sprint slips | Pair on every story; keep stories small; hold a buffer in Sprint 5 |
| Scope creep from extra features | MVP delayed | Only "Could" items enter after all "Must" items are done |
| Floating-point errors in money maths | Wrong balances | Use `BigDecimal` in Java and `decimal` in the database |
| Regulatory concerns (SEC Nigeria, NDPA) | Cannot launch for real | Prototype is simulated; document the compliance path |

---

## 11. Team working agreements

- Branch per story, pull request required, at least one reviewer.
- Commit messages are short and describe the change.
- No story is "Done" until it meets the Definition of Done.
- Daily Scrum attendance is expected; send an update in the chat if you miss it.
- Disagreements go to the Product Owner for priority and the Scrum Master for process.

---

## 12. Sprint review and retrospective log

| Sprint | Goal met? | Points done | What went well | What to improve | Action for next sprint |
|---|---|---|---|---|---|
| 1 | | | | | |
| 2 | | | | | |
| 3 | | | | | |
| 4 | | | | | |
| 5 | | | | | |
| 6 | | | | | |
