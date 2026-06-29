INSERT INTO patient (id, full_name, email, phone)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Ana Souza', 'ana.souza@example.com', '+55 11 90000-0001'),
  ('22222222-2222-2222-2222-222222222222', 'Bruno Lima', 'bruno.lima@example.com', '+55 11 90000-0002'),
  ('33333333-3333-3333-3333-333333333333', 'Carla Santos', 'carla.santos@example.com', '+55 11 90000-0003'),
  ('44444444-4444-4444-4444-444444444444', 'Diego Martins', 'diego.martins@example.com', '+55 11 90000-0004'),
  ('55555555-5555-5555-5555-555555555555', 'Elisa Rocha', 'elisa.rocha@example.com', '+55 11 90000-0005')
ON CONFLICT (id) DO NOTHING;

INSERT INTO appointment (
  id,
  patient_id,
  professional_id,
  date_time,
  status,
  appointment_type,
  service_name,
  facility_name,
  preparation_notes,
  patient_notification,
  last_notified_at,
  created_at
)
VALUES
  (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    '11111111-1111-1111-1111-111111111111',
    '99999999-9999-9999-9999-999999999991',
    CURRENT_TIMESTAMP + INTERVAL '1 day',
    'CONFIRMED',
    'EXAM',
    'Hemograma completo',
    'UBS Central',
    'Jejum de 8 horas. Beber agua normalmente.',
    'Agendamento confirmado para exame de Hemograma completo em UBS Central. Preparo: Jejum de 8 horas. Beber agua normalmente.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
    '22222222-2222-2222-2222-222222222222',
    '99999999-9999-9999-9999-999999999992',
    CURRENT_TIMESTAMP + INTERVAL '3 days',
    'CONFIRMED',
    'CONSULTATION',
    'Clinica geral',
    'UBS Vila Nova',
    NULL,
    'Agendamento confirmado para consulta de Clinica geral em UBS Vila Nova.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'cccccccc-cccc-cccc-cccc-ccccccccccc3',
    '33333333-3333-3333-3333-333333333333',
    '99999999-9999-9999-9999-999999999993',
    CURRENT_TIMESTAMP + INTERVAL '10 days',
    'CONFIRMED',
    'EXAM',
    'Ultrassom abdominal',
    'Centro de Diagnostico Norte',
    'Jejum de 6 horas e chegar 20 minutos antes.',
    'Agendamento confirmado para exame de Ultrassom abdominal em Centro de Diagnostico Norte. Preparo: Jejum de 6 horas e chegar 20 minutos antes.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'dddddddd-dddd-dddd-dddd-ddddddddddd4',
    '44444444-4444-4444-4444-444444444444',
    '99999999-9999-9999-9999-999999999991',
    CURRENT_TIMESTAMP + INTERVAL '15 days',
    'CONFIRMED',
    'EXAM',
    'Hemograma completo',
    'UBS Central',
    'Jejum de 8 horas. Beber agua normalmente.',
    'Agendamento confirmado para exame de Hemograma completo em UBS Central. Preparo: Jejum de 8 horas. Beber agua normalmente.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
    '55555555-5555-5555-5555-555555555555',
    '99999999-9999-9999-9999-999999999995',
    CURRENT_TIMESTAMP + INTERVAL '20 days',
    'CONFIRMED',
    'CONSULTATION',
    'Cardiologia',
    'Ambulatorio Municipal',
    'Trazer exames recentes e lista de medicamentos em uso.',
    'Agendamento confirmado para consulta de Cardiologia em Ambulatorio Municipal. Preparo: Trazer exames recentes e lista de medicamentos em uso.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  )
ON CONFLICT (id) DO NOTHING;
