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

Each card is one feature. It has 3 user stories, and each story has 3 acceptance criteria.

### Card 1 · [US-01] Accounts and security · _TBD_ · 5 pts

**1.1 Register**
As a new user, I want to create an account so that I can start investing.
- [ ] Registration needs a name, a valid email and a password of at least 8 characters with a letter and a number.
- [ ] An email that is already registered is rejected with a clear message.
- [ ] A successful registration creates the investor account and an empty wallet with a ₦0.00 balance.

**1.2 Log in**
As a registered user, I want to log in so that I can reach my account.
- [ ] Correct email and password return a signed token and open the dashboard.
- [ ] Wrong credentials show one generic error that does not say which field was wrong.
- [ ] After 5 failed attempts the account is locked for 15 minutes.

**1.3 Session and logout**
As a logged-in user, I want my session protected so that nobody else can use my account.
- [ ] Protected pages and API routes redirect or return 401 when there is no valid token.
- [ ] A token expires after 30 minutes of inactivity and the user is asked to log in again.
- [ ] Logging out ends the session and the old token can no longer be used.

### Card 2 · [US-02] Wallet funding · _TBD_ · 5 pts

**2.1 Fund wallet**
As an investor, I want to add money to my wallet so that I have funds to invest.
- [ ] I can enter an amount (minimum ₦100) and pay through the payment gateway in test mode.
- [ ] After a successful payment the wallet balance increases by exactly the amount paid.
- [ ] Every funding attempt creates a transaction record with a unique reference.

**2.2 Balance and history**
As an investor, I want to see my balance and past transactions so that I know where my money is.
- [ ] The balance is shown in naira with 2 decimal places and updates right after a payment.
- [ ] The transaction list shows date, type, amount and status (PENDING, SETTLED, FAILED), newest first.
- [ ] I can only see my own wallet and transactions.

**2.3 Failed and duplicate payments**
As an investor, I want failed or repeated payments handled safely so that I never lose or gain money by mistake.
- [ ] A declined or cancelled payment is marked FAILED and the balance does not change.
- [ ] If the gateway sends the same payment confirmation twice, the wallet is credited only once.
- [ ] I see a clear message explaining what happened and what to do next.

### Card 3 · [US-03] Risk profile quiz · _TBD_ · 3 pts

**3.1 Take the quiz**
As an investor, I want to answer a short quiz so that Kernel understands how much risk I can handle.
- [ ] The quiz has 5 to 7 multiple-choice questions, shown one at a time or on one page.
- [ ] I cannot submit until every question has an answer.
- [ ] I can go back and change an answer before I submit.

**3.2 Score and category**
As an investor, I want to get a risk category so that my recommendations match my comfort level.
- [ ] Each answer carries a fixed number of points and the total score is calculated by the system.
- [ ] The score maps to CONSERVATIVE, BALANCED or GROWTH using fixed ranges, and the same answers always give the same result.
- [ ] The result is saved to my profile with the date it was taken.

**3.3 View and retake**
As an investor, I want to see and update my result so that it stays accurate as my situation changes.
- [ ] My category and a plain-language explanation are shown on my profile.
- [ ] Retaking the quiz creates a new result, the newest one is used for recommendations, and old results are kept.
- [ ] A user who has not taken the quiz is prompted to take it before requesting a recommendation.

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
