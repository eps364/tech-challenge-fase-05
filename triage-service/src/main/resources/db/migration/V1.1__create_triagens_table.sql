CREATE TABLE IF NOT EXISTS triage (
  id UUID PRIMARY KEY,
  patient_id UUID NOT NULL,
  risk_level VARCHAR(10) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_triage_patient_id ON triage(patient_id);
CREATE INDEX IF NOT EXISTS idx_triage_risk_level ON triage(risk_level);
