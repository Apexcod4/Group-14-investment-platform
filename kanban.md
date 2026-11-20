# Kernel – Kanban Board

Board for Group 14. Move a card by cutting its line into the next column and committing. IDs match the user stories in [scrum.md](scrum.md).

**Flow:** Backlog → To Do → In Progress → In Review → Testing → Done

**WIP limits:** In Progress ≤ 3 per person · In Review ≤ 4 · Testing ≤ 4
**Card format:** `[ID] Title · owner · points`
**Done means:** the Definition of Done in [scrum.md](scrum.md) is met.

---

## Done

- [x] [DOC-01] Project proposal (Kernel) · team
- [x] [DOC-02] Full UML design, 12 diagrams · team
- [x] [DOC-03] Scrum plan (`scrum.md`) · team
- [x] [DOC-04] README and `.gitignore` · team
- [x] [DOC-05] GitHub repository created · team

## Testing

_Empty_

## In Review

_Empty_

## In Progress

_Empty_

## To Do – Sprint 0 (setup)

- [ ] [S0-01] Fill in team names, roles and owners in `scrum.md` and `README.md` · all
- [ ] [S0-02] Create `backend/` Spring Boot project (Java 21, Maven, Security, JPA, PostgreSQL) · _TBD_
- [ ] [S0-03] Create `frontend/` Next.js project with base layout · _TBD_
- [ ] [S0-04] Set up PostgreSQL schema from the ER diagram (migrations) · _TBD_
- [ ] [S0-05] Add `.env.example` for back end and front end · _TBD_
- [ ] [S0-06] Branch rules: protect `main`, require pull request review · _TBD_
- [ ] [S0-07] Set up CI to build and run tests on every pull request · _TBD_
- [ ] [S0-08] Create sprint board in GitHub Projects (optional mirror of this file) · _TBD_

## To Do – Sprint 1 (sign up, log in, fund wallet)

- [ ] [US-01] Register and log in securely · _TBD_ · 5
- [ ] [US-02] Fund wallet with test-mode payment · _TBD_ · 5
- [ ] [US-03] Risk quiz and scoring · _TBD_ · 3

## Backlog – Must

- [ ] [US-04] Enter budget, goal and horizon · 3
- [ ] [US-05] Recommended portfolio with "Why this?" reasons · 13
- [ ] [US-06] Accept a recommendation and see it as my portfolio · 8
- [ ] [US-07] View portfolio value and holdings · 5
- [ ] [US-08] Set up auto-invest plan (weekly or monthly) · 8
- [ ] [US-09] Scheduler runs due plans and records transactions · 8
- [ ] [US-20] Deploy, final report and demo · 8

## Backlog – Should

- [ ] [US-10] Pause, resume or cancel a plan · 3
- [ ] [US-11] Notify when a run fails for low funds · 3
- [ ] [US-12] Auto-rebalance when drift exceeds 5 points · 8
- [ ] [US-13] Savings goals and progress · 5
- [ ] [US-19] Admin: manage assets, view users and transactions · 5

## Backlog – Could

- [ ] [US-14] Price alerts · 5
- [ ] [US-15] Learning hub articles · 3
- [ ] [US-16] Practice mode with virtual money · 5
- [ ] [US-17] Dividend tracker · 3
- [ ] [US-18] Ethical filter for recommendations · 3

---

## How to use this board

1. Pull a card from **To Do** into **In Progress** and put your name on it. Respect the WIP limits.
2. Create a branch named `feature/US-05-recommendations` and work there.
3. Open a pull request. Move the card to **In Review**.
4. After approval and merge, move the card to **Testing** until acceptance criteria are checked.
5. Move to **Done** only when the Definition of Done is met.
6. At each Sprint Planning, move that sprint's stories from **Backlog** to **To Do**.
7. Blocked? Add `🚫 blocked: reason` to the card and raise it at the Daily Scrum.
