INSERT INTO event_locations(id, name, address, capacity, description)
VALUES (1,'City Hall', '123 Main St, Cityville',500,
        'Large hall in the center of the city, perfect for conferences and large events.'),
       (2,'Riverside Park', '456 River Rd, Rivercity',2000,
        'Outdoor park located by the river, ideal for concerts and outdoor festivals.');

INSERT INTO  users (id,login,password,age,role)
VALUES (1,'admin','$2b$12$RDnNPvF6ZiW6mGYJfF0px.1E1UWUOhupsv3yvkm0RfT3xh1Yhm0Cm',30,'ROLE_ADMIN'),
       (2,'user','$2b$12$RDnNPvF6ZiW6mGYJfF0px.1E1UWUOhupsv3yvkm0RfT3xh1Yhm0Cm',25,'ROLE_USER'),
       (3,'bob','$2b$12$RDnNPvF6ZiW6mGYJfF0px.1E1UWUOhupsv3yvkm0RfT3xh1Yhm0Cm',35,'ROLE_USER');