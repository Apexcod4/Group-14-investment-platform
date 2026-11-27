import type { Asset } from "@/lib/api";
import { naira } from "@/lib/format";

export const dynamic = "force-dynamic";

const BACKEND = process.env.BACKEND_URL ?? "http://localhost:8080";

const GROUPS: { key: Asset["assetClass"]; label: string }[] = [
  { key: "NG_STOCK", label: "Nigerian stocks" },
  { key: "US_STOCK", label: "US stocks" },
  { key: "ETF", label: "ETFs" },
  { key: "FIXED_INCOME", label: "Fixed income" },
];

async function loadAssets(): Promise<Asset[] | null> {
  try {
    const res = await fetch(`${BACKEND}/api/assets`, { cache: "no-store" });
    return res.ok ? ((await res.json()) as Asset[]) : null;
  } catch {
    return null;
  }
}

function Risk({ level }: { level: number }) {
  return (
    <span className="risk" role="img" aria-label={`Risk ${level} out of 5`}>
      {[1, 2, 3, 4, 5].map((i) => (
        <i key={i} className={i <= level ? "on" : ""} />
      ))}
    </span>
  );
}

export default async function AssetsPage() {
  const assets = await loadAssets();

  return (
    <>
      <h1>What Kernel can invest in</h1>
      <p className="lede">Sample assets for the prototype. Prices are placeholders, not live market data.</p>

      {!assets && (
        <p className="notice" role="alert">
          The server isn&apos;t answering. Start the back end (<code>mvn spring-boot:run</code> in <code>backend/</code>) and
          refresh.
        </p>
      )}

      {assets &&
        GROUPS.map(({ key, label }) => {
          const rows = assets.filter((a) => a.assetClass === key);
          if (rows.length === 0) return null;
          return (
            <section key={key} aria-labelledby={key}>
              <h2 id={key}>{label}</h2>
              <div className="scroll">
                <table>
                  <thead>
                    <tr>
                      <th scope="col">Name</th>
                      <th scope="col" className="num">Price</th>
                      <th scope="col" className="num">Dividend yield</th>
                      <th scope="col">Risk</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rows.map((a) => (
                      <tr key={a.id}>
                        <td>
                          <strong>{a.symbol}</strong> <span className="muted">{a.name}</span>
                        </td>
                        <td className="num">{a.currency === "NGN" ? naira(a.lastPrice) : `${a.currency} ${a.lastPrice.toFixed(2)}`}</td>
                        <td className="num">{(a.dividendYield * 100).toFixed(1)}%</td>
                        <td>
                          <Risk level={a.riskRating} />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          );
        })}
    </>
  );
}
