# Distributed URL Shortener

Recruiter-ready, **free-tier** URL shortener with Redis cache and RabbitMQ click analytics.

Spring Boot 3 / Java 21 monorepo: `shortener-service` + `analytics-service`, shared Postgres (`urls` + `clicks`).

## Architecture

```
Client
  │
  ├─ POST   /api/shorten          ─┐
  ├─ DELETE /api/{code} + token    │  shortener-service (Spring Boot)
  └─ GET    /r/{code}  ────────────┤
         │                         │
         │  Redis (Upstash)        │  cache hit → 302
         │  cache miss → Postgres  │  fail-open async publish
         │                         ▼
         │                    RabbitMQ (CloudAMQP)
         │                         │  ClickEvent
         │                         ▼
         │                  analytics-service
         │                         │
         └─────────────┬───────────┘
                       ▼
              Supabase Postgres (shared)
                 urls  |  clicks
```

Demo uses **one** Supabase project and two tables. Split urls/clicks into separate DBs later for production scale.

## Code design: Base62(DB id) rejected

We **do not** encode the numeric primary key as Base62.

| Approach | Why rejected / chosen |
|---|---|
| `Base62(id)` | Leaks growth, enumerable, couples public codes to insert order |
| **Random Base62 `code` (7–8 chars)** | Opaque, unique index + retry on collision; `id` stays internal |

`owner_token` (secure random) authorizes `DELETE`; never treat codes as secrets alone.

## API contracts (Day 1 stubs → 501)

| Method | Path | Service | Notes |
|---|---|---|---|
| `POST` | `/api/shorten` | shortener | `{ "url", "ttlDays"? }` → `{ "code", "shortUrl", "ownerToken", "expiresAt" }` |
| `GET` | `/r/{code}` | shortener | 302; never block on MQ; 404 if missing/expired |
| `DELETE` | `/api/{code}` | shortener | Header `X-Owner-Token`; evict Redis |
| `GET` | `/analytics/{code}` | analytics | Totals + recent clicks |

ClickEvent (RabbitMQ): `{ "eventId", "code", "clickedAt", "ipHash", "userAgent" }` — publish **async / fail-open**; consumer idempotent via `UNIQUE(event_id)`.

DDL: see [`db/schema.sql`](db/schema.sql).

## Free-tier accounts

| Service | Plan | Use | Notes |
|---|---|---|---|
| **Supabase** | Free | Postgres | Idle projects **pause**; ~500 MB; use **pooler** URI |
| **Upstash Redis** | Free | Cache + rate limit | Short TTLs; no heavy scans |
| **CloudAMQP** | Little Lemur | ClickEvent queue | **1 connection per service**; durable queue; prefetch ≤10 |
| **Railway** | Free/Hobby trial | Host both services | Cold starts; sleep on idle |
| **GitHub** | Free | Monorepo + Actions | Stay within free minutes |

No paid add-ons, no paid custom domains, no paid CI runners.

### Pause / cold-start demo tips

- **Supabase pause:** wake the project (dashboard or health hit) ~1 minute before demos; prefer pooler (transaction mode) to stay under connection limits.
- **Railway cold start:** dual services may take 30–60s after sleep; warm `/actuator/health` on both.
- **Local fallback:** `docker compose up` for Postgres + Redis + RabbitMQ when cloud sleeps.

## Repo layout

```
url-shortener/
├── shortener-service/     # Spring Boot 3 / Java 21
├── analytics-service/     # Spring Boot 3 / Java 21
├── db/schema.sql          # shared urls + clicks DDL
├── docker-compose.yml     # local Postgres, Redis, RabbitMQ
├── .github/workflows/ci.yml
├── .env.example           # keys only — no secrets
├── docs/                  # architecture diagrams later
└── README.md
```

## Local quickstart (Day 1)

```bash
cp .env.example .env
docker compose up -d
# Apply schema is automatic via compose init; or: psql $DATABASE_URL -f db/schema.sql

cd shortener-service && mvn spring-boot:run
cd analytics-service && mvn spring-boot:run
```

Day 1 controllers return **501** placeholders. Wiring lands Days 2–7 per the build plan.

## License

MIT (or as chosen by the repo owner).
