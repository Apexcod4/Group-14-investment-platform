"use client";

import { useCallback, useEffect, useState } from "react";
import { changePlan, createPlan, getPlan, type Frequency, type Plan, type PlanStatus } from "@/lib/api";
import { naira, when } from "@/lib/format";
import { validatePlan } from "@/lib/plan-rules";

const STORAGE_KEY = "kernel.planIds";

const STATUS_LABEL: Record<PlanStatus, string> = {
  ACTIVE: "Active",
  PAUSED: "Paused",
  SUSPENDED: "Suspended",
  COMPLETED: "Completed",
  CANCELLED: "Cancelled",
};

const FREQ_LABEL: Record<Frequency, string> = { WEEKLY: "every week", MONTHLY: "every month" };

function readIds(): number[] {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) ?? "[]");
  } catch {
    return [];
  }
}

function saveIds(ids: number[]) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(ids));
  } catch {
    /* storage unavailable; the list just won't survive a refresh */
  }
}

export default function PlansPage() {
  const [plans, setPlans] = useState<Plan[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  const [portfolioId, setPortfolioId] = useState("1");
  const [amount, setAmount] = useState("");
  const [frequency, setFrequency] = useState<Frequency>("WEEKLY");
  const [firstRun, setFirstRun] = useState("");

  const refresh = useCallback(async () => {
    const ids = readIds();
    const results = await Promise.allSettled(ids.map(getPlan));
    setPlans(results.flatMap((r) => (r.status === "fulfilled" ? [r.value] : [])));
    setLoading(false);
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  async function onCreate(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    const amt = Number(amount);
    const problem = validatePlan(amt, new Date(firstRun));
    if (problem) return setError(problem);

    setBusy(true);
    try {
      const plan = await createPlan({
        portfolioId: Number(portfolioId),
        amount: amt,
        frequency,
        firstRun,
      });
      saveIds([...readIds(), plan.id]);
      setAmount("");
      await refresh();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Couldn't create the plan.");
    } finally {
      setBusy(false);
    }
  }

  async function onChange(id: number, action: "pause" | "resume" | "cancel") {
    setError(null);
    try {
      await changePlan(id, action);
      await refresh();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Couldn't update the plan.");
    }
  }

  return (
    <>
      <h1>Auto-invest plans</h1>
      <p className="lede">Set an amount and a schedule. Kernel invests it for you each time it comes due.</p>

      <div className="split">
        <form onSubmit={onCreate} className="form" aria-labelledby="new-plan">
          <h2 id="new-plan">New plan</h2>

          <label>
            Amount (₦)
            <input type="number" inputMode="decimal" min="0" step="0.01" required value={amount} onChange={(e) => setAmount(e.target.value)} />
          </label>

          <label>
            How often
            <select value={frequency} onChange={(e) => setFrequency(e.target.value as Frequency)}>
              <option value="WEEKLY">Every week</option>
              <option value="MONTHLY">Every month</option>
            </select>
          </label>

          <label>
            First investment
            <input type="datetime-local" required value={firstRun} onChange={(e) => setFirstRun(e.target.value)} />
          </label>

          <label>
            Portfolio number
            <input type="number" min="1" required value={portfolioId} onChange={(e) => setPortfolioId(e.target.value)} />
            <span className="hint">Until sign-in exists, enter the portfolio this plan belongs to.</span>
          </label>

          <button type="submit" disabled={busy}>
            {busy ? "Creating…" : "Create plan"}
          </button>
        </form>

        <section aria-labelledby="your-plans">
          <h2 id="your-plans">Your plans</h2>

          {error && (
            <p className="notice" role="alert">
              {error}
            </p>
          )}

          {loading && <p className="muted">Loading…</p>}
          {!loading && plans.length === 0 && <p className="muted">No plans yet. Fill in the form to create your first one.</p>}

          <ul className="plans">
            {plans.map((p) => (
              <li key={p.id}>
                <div className="plan-head">
                  <span className="amount">{naira(p.amount)}</span>
                  <span className={`pill ${p.status.toLowerCase()}`}>{STATUS_LABEL[p.status]}</span>
                </div>
                <p className="muted">
                  {FREQ_LABEL[p.frequency]} · portfolio {p.portfolioId}
                </p>
                {(p.status === "ACTIVE" || p.status === "PAUSED" || p.status === "SUSPENDED") && (
                  <p>Next investment: {when(p.nextRun)}</p>
                )}
                {p.status === "SUSPENDED" && (
                  <p className="muted">Paused after {p.failedRuns} failed runs. Resume it once the wallet has enough money.</p>
                )}
                <div className="actions">
                  {p.status === "ACTIVE" && <button className="ghost" onClick={() => onChange(p.id, "pause")}>Pause</button>}
                  {(p.status === "PAUSED" || p.status === "SUSPENDED") && (
                    <button className="ghost" onClick={() => onChange(p.id, "resume")}>Resume</button>
                  )}
                  {(p.status === "ACTIVE" || p.status === "PAUSED" || p.status === "SUSPENDED") && (
                    <button className="ghost danger" onClick={() => onChange(p.id, "cancel")}>Cancel plan</button>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </section>
      </div>
    </>
  );
}
