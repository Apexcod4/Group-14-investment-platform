// The browser calls /api/* on the Next.js server, which forwards to Spring Boot.
// This avoids CORS setup on the back end. Change BACKEND_URL when deploying.
const backend = process.env.BACKEND_URL ?? "http://localhost:8080";

/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [{ source: "/api/:path*", destination: `${backend}/api/:path*` }];
  },
};

export default nextConfig;
