import type { Metadata } from "next";
import Link from "next/link";
import "@fontsource-variable/bricolage-grotesque";
import "@fontsource-variable/instrument-sans";
import "./globals.css";

export const metadata: Metadata = {
  title: "Kernel",
  description: "Tell Kernel what you can invest. It builds the portfolio and keeps it going.",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>
        <header className="bar">
          <Link href="/" className="wordmark">
            Kernel
          </Link>
          <nav aria-label="Main">
            <Link href="/">Assets</Link>
            <Link href="/plans">Auto-invest plans</Link>
          </nav>
        </header>
        <main>{children}</main>
        <footer>Simulated trading only. Nothing here is investment advice.</footer>
      </body>
    </html>
  );
}
