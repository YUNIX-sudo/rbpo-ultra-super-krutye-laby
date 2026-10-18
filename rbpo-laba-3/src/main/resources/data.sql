-- Тестовые данные для лабораторной 3
INSERT INTO users (name, email, role, created_at) VALUES 
('Координатор Иван', 'ivan@vol.org', 'COORDINATOR', NOW()),
('Волонтер Петр', 'petr@vol.org', 'VOLUNTEER', NOW()),
('Волонтер Анна', 'anna@vol.org', 'VOLUNTEER', NOW());

INSERT INTO volunteer_campaigns (title, start_date, end_date, coordinator_id, status, created_at) VALUES
('Помощь приюту', '2026-10-01', '2026-10-30', 1, 'ACTIVE', NOW()),
('Очистка леса', '2026-11-01', '2026-11-05', 1, 'PLANNED', NOW());

INSERT INTO volunteer_tasks (campaign_id, title, required_participants, start_date, duration_hours, created_at) VALUES
(1, 'Кормление собак', 2, '2026-10-20 10:00:00', 4, NOW()),
(1, 'Уборка вольеров', 3, '2026-10-21 11:00:00', 3, NOW());

INSERT INTO volunteer_responses (task_id, volunteer_id, status, created_at) VALUES
(1, 2, 'REGISTERED', NOW()),
(2, 3, 'REGISTERED', NOW());

INSERT INTO participation_reports (response_id, actual_hours, confirmation_status, created_at) VALUES
(1, 4, 'PENDING', NOW());
