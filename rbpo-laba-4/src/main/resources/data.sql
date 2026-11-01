TRUNCATE TABLE participation_reports, volunteer_responses, volunteer_tasks, volunteer_campaigns, users RESTART IDENTITY CASCADE;

INSERT INTO users (name, email, password, role, is_active, created_at) VALUES
('Координатор Иван', 'dodik1@yunix.software', '$2a$10$3WK2KR5bfzYnoDl2U3AO9u/2P2aARxoibwkKYFPHMHfMCfQQfhr/e', 'COORDINATOR', 1, NOW()),
('Волонтер Петр', 'dodik2@yunix.software', '$2a$10$3WK2KR5bfzYnoDl2U3AO9u/2P2aARxoibwkKYFPHMHfMCfQQfhr/e', 'VOLUNTEER', 1, NOW()),
('Волонтер Анна', 'dodik3@yunix.software', '$2a$10$3WK2KR5bfzYnoDl2U3AO9u/2P2aARxoibwkKYFPHMHfMCfQQfhr/e', 'VOLUNTEER', 1, NOW()),
('Заблокированный', 'dodik4@yunix.software', '$2a$10$3WK2KR5bfzYnoDl2U3AO9u/2P2aARxoibwkKYFPHMHfMCfQQfhr/e', 'VOLUNTEER', 0, NOW());

INSERT INTO volunteer_campaigns (title, start_date, end_date, coordinator_id, status, created_at) VALUES
('Помощь Ярославу', '2026-10-01', '2026-10-30', 1, 'ACTIVE', NOW()),
('Очистка леса', '2026-11-01', '2026-11-05', 1, 'PLANNED', NOW());

INSERT INTO volunteer_tasks (campaign_id, title, required_participants, start_date, duration_hours, created_at) VALUES
(1, 'Сдать лабу', 2, '2026-10-20 10:00:00', 4, NOW()),
(1, 'чизас чилл бармалда', 3, '2026-10-21 11:00:00', 3, NOW());

INSERT INTO volunteer_responses (task_id, volunteer_id, status, created_at) VALUES
(1, 2, 'REGISTERED', NOW()),
(2, 3, 'REGISTERED', NOW());

INSERT INTO participation_reports (response_id, actual_hours, confirmation_status, created_at) VALUES
(1, 4, 'PENDING', NOW());
