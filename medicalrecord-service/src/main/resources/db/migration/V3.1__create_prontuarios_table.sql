CREATE TABLE IF NOT EXISTS medical_record (
  id UUID PRIMARY KEY,
  appointment_id UUID NOT NULL,
  patient_id UUID NOT NULL,
  diagnosis TEXT,
  prescription TEXT,
  consultation_date TIMESTAMP,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_medical_record_patient_id ON medical_record(patient_id);
CREATE INDEX IF NOT EXISTS idx_medical_record_appointment_id ON medical_record(appointment_id);
CREATE INDEX IF NOT EXISTS idx_medical_record_created_at ON medical_record(created_at);
