CREATE TABLE IF NOT EXISTS appointment (
  id UUID PRIMARY KEY,
  triage_id UUID NOT NULL,
  patient_id UUID NOT NULL,
  professional_id UUID,
  date_time TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_appointment_patient_id ON appointment(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointment_date_time ON appointment(date_time);
CREATE INDEX IF NOT EXISTS idx_appointment_status ON appointment(status);
