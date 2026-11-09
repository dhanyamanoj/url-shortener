-- Shared Supabase / local Postgres DDL (Day 1 contract)
-- Codes are random Base62 (7–8 chars); numeric id is internal only — never Base62(id).

CREATE TABLE IF NOT EXISTS urls (
  id            BIGSERIAL PRIMARY KEY,
  code          VARCHAR(8)  NOT NULL,
  long_url      TEXT        NOT NULL,
  owner_token   VARCHAR(64) NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at    TIMESTAMPTZ NULL,
  UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS clicks (
  id              BIGSERIAL PRIMARY KEY,
  code            VARCHAR(8)  NOT NULL,
  clicked_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  ip_hash         VARCHAR(64) NULL,
  user_agent      TEXT        NULL,
  event_id        UUID        NOT NULL,  -- idempotency for at-least-once MQ
  UNIQUE (event_id)
);

CREATE INDEX IF NOT EXISTS idx_clicks_code_clicked_at ON clicks (code, clicked_at DESC);
