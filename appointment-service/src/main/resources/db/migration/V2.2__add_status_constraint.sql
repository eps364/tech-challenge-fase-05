-- Add CHECK constraint to enforce valid appointment status values
ALTER TABLE appointment
  ADD CONSTRAINT chk_appointment_status
    CHECK (status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW'));
