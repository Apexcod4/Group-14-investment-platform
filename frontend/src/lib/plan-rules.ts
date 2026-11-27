/**
 * Rules a new auto-invest plan must meet before the form sends it to the server.
 * The server checks too (InvestPlan.create), but checking here tells the user
 * what to fix without a round trip.
 *
 * Return a short, plain-language message when the input is not acceptable,
 * or null when it is fine.
 */
export function validatePlan(amount: number, firstRun: Date): string | null {
  // TODO (your decision): decide what makes a plan acceptable. See the notes in chat.
  return null;
}
