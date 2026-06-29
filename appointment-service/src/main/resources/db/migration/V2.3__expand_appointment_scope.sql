ALTER TABLE appointment
  ALTER COLUMN triage_id DROP NOT NULL;

ALTER TABLE appointment
  ADD COLUMN IF NOT EXISTS appointment_type VARCHAR(20) NOT NULL DEFAULT 'CONSULTATION',
  ADD COLUMN IF NOT EXISTS service_name VARCHAR(120) NOT NULL DEFAULT 'Consulta clinica',
  ADD COLUMN IF NOT EXISTS facility_name VARCHAR(160) NOT NULL DEFAULT 'UBS Central',
  ADD COLUMN IF NOT EXISTS preparation_notes TEXT,
  ADD COLUMN IF NOT EXISTS patient_notification TEXT,
  ADD COLUMN IF NOT EXISTS last_notified_at TIMESTAMP,
  ADD COLUMN IF NOT EXISTS rescheduled_from TIMESTAMP,
  ADD COLUMN IF NOT EXISTS cancellation_reason TEXT;

ALTER TABLE appointment
  ADD CONSTRAINT chk_appointment_type
    CHECK (appointment_type IN ('CONSULTATION', 'EXAM'));

CREATE INDEX IF NOT EXISTS idx_appointment_type_service
  ON appointment(appointment_type, service_name);

CREATE INDEX IF NOT EXISTS idx_appointment_status_type_service_date
  ON appointment(status, appointment_type, service_name, date_time);

CREATE TABLE IF NOT EXISTS patient (
  id UUID PRIMARY KEY,
  full_name VARCHAR(160) NOT NULL,
  email VARCHAR(160) NOT NULL,
  phone VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointment_offer (
  id UUID PRIMARY KEY,
  opened_appointment_id UUID NOT NULL,
  candidate_appointment_id UUID NOT NULL,
  candidate_patient_id UUID NOT NULL,
  offered_date_time TIMESTAMP NOT NULL,
  original_date_time TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  responded_at TIMESTAMP,
  CONSTRAINT chk_appointment_offer_status
    CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED'))
);

CREATE INDEX IF NOT EXISTS idx_appointment_offer_patient_status
  ON appointment_offer(candidate_patient_id, status);
