package com.kernel.automation;

/**
 * Buys the assets for one plan run. This is the hand-off to the other parts of Kernel:
 *
 * <ul>
 *   <li>Part 2 (wallet): check the balance and debit {@code plan.getAmount()}.
 *   <li>Part 3 (engine): split the amount across the portfolio's target weights.
 *   <li>Part 4 (portfolio): add the bought units to the holdings.
 * </ul>
 *
 * <p>Return {@link ExecutionResult#INSUFFICIENT_FUNDS} when the wallet cannot cover the plan.
 * Throw an exception only for unexpected errors: the run is rolled back and retried on the next tick.
 */
public interface PlanExecutor {

    ExecutionResult execute(InvestPlan plan);
}
