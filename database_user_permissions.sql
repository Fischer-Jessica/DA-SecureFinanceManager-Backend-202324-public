REVOKE CREATE ON SCHEMA public FROM PUBLIC;

CREATE ROLE backend WITH PASSWORD 'SECURE_PASSWORD';

GRANT CONNECT ON DATABASE secure_finance_manager TO backend;

GRANT USAGE ON SCHEMA public TO backend;

GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA public TO backend;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO backend;

ALTER ROLE backend WITH LOGIN;

ALTER USER backend WITH SUPERUSER;