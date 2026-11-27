export type Frequency = "WEEKLY" | "MONTHLY";
export type PlanStatus = "ACTIVE" | "PAUSED" | "SUSPENDED" | "COMPLETED" | "CANCELLED";

export interface Asset {
  id: number;
  symbol: string;
  name: string;
  assetClass: "NG_STOCK" | "US_STOCK" | "ETF" | "FIXED_INCOME";
  sector: string;
  exchange: string;
  currency: string;
  lastPrice: number;
  dividendYield: number;
  riskRating: number;
}

export interface Plan {
  id: number;
  portfolioId: number;
  amount: number;
  frequency: Frequency;
  nextRun: string;
  status: PlanStatus;
  failedRuns: number;
}

export interface NewPlan {
  portfolioId: number;
  amount: number;
  frequency: Frequency;
  firstRun: string;
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(path, {
    ...init,
    headers: { "Content-Type": "application/json", ...init?.headers },
  });
  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const body = await res.json();
      message = body.message ?? body.error ?? message;
    } catch {
      /* body was not JSON; keep the status message */
    }
    throw new Error(message);
  }
  return res.json() as Promise<T>;
}

export const createPlan = (p: NewPlan) =>
  request<Plan>("/api/plans", { method: "POST", body: JSON.stringify(p) });

export const getPlan = (id: number) => request<Plan>(`/api/plans/${id}`);

export const changePlan = (id: number, action: "pause" | "resume" | "cancel") =>
  request<Plan>(`/api/plans/${id}/${action}`, { method: "POST" });
