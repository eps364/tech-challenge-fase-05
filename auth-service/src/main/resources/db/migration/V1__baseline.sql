-- Auth-service baseline: user management is delegated to Keycloak.
-- This migration establishes the Flyway baseline for this database.
-- Token blacklisting is handled by Redis (no SQL table needed).
SELECT 1;
