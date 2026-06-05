-- Add CHECK constraint to enforce valid Manchester Protocol risk levels
ALTER TABLE triage
  ADD CONSTRAINT chk_triage_risk_level
    CHECK (risk_level IN ('RED', 'ORANGE', 'YELLOW', 'GREEN', 'BLUE'));
