--liquibase formatted sql

--changeset pjatera:1
INSERT INTO event_locations(id, name, address, capacity, description)
VALUES (1, 'City Hall', '123 Main St, Cityville', 500,
        'Large hall in the center of the city, perfect for conferences and large events.'),
       (2, 'Riverside Park', '456 River Rd, Rivercity', 2000,
        'Outdoor park located by the river, ideal for concerts and outdoor festivals.');
SELECT setval('event_locations_id_seq', (SELECT MAX(id) FROM event_locations));

--changeset pjatera:2
INSERT INTO users (id, login, password, age, role)

VALUES (1, 'admin', '$2a$10$PfPR4cwDfdHOCaIm4OXdVeZ.qvs6NdPOgDyiesLTn/HjV9w2ofSVa', 30, 'ROLE_ADMIN'),
       (2, 'user123', '$2a$10$PfPR4cwDfdHOCaIm4OXdVeZ.qvs6NdPOgDyiesLTn/HjV9w2ofSVa', 25, 'ROLE_USER'),
       (3, 'user345', '$2a$10$PfPR4cwDfdHOCaIm4OXdVeZ.qvs6NdPOgDyiesLTn/HjV9w2ofSVa', 28, 'ROLE_USER');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

--changeset pjatera:3
INSERT INTO events(id, name, owner_id, max_places, date, cost, duration, location_id, status)
VALUES (1, 'Full-Stack Web Development Bootcamp', 2,
        50, '2024-10-13 22:07:00', 500,
        3, 2, 'WAIT_START'),
       (2, 'Advanced Digital Marketing Workshop', 2, 30, '2025-12-05 10:00:00',
        300, 180, 1, 'WAIT_START');

SELECT setval('events_id_seq', (SELECT MAX(id) FROM events));
--changeset pjatera:4
INSERT INTO events_users (id, event_id, user_id)
VALUES (1, 1, 3),
       (2, 2, 3);

SELECT setval('events_users_id_seq', (SELECT MAX(id) FROM events_users));