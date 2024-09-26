

INSERT INTO event_locations(id, name, address, capacity, description)
VALUES (1,'City Hall', '123 Main St, Cityville',500,
        'Large hall in the center of the city, perfect for conferences and large events.'),
       (2,'Riverside Park', '456 River Rd, Rivercity',2000,
        'Outdoor park located by the river, ideal for concerts and outdoor festivals.');
SELECT setval('event_locations_id_seq', (SELECT MAX(id) FROM event_locations));


INSERT INTO  users (id,login,password,age,role)
-- password: password
VALUES (1,'admin','$2a$10$PfPR4cwDfdHOCaIm4OXdVeZ.qvs6NdPOgDyiesLTn/HjV9w2ofSVa',30,'ROLE_ADMIN'),
       (2,'user','$2a$10$PfPR4cwDfdHOCaIm4OXdVeZ.qvs6NdPOgDyiesLTn/HjV9w2ofSVa',25,'ROLE_USER');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
