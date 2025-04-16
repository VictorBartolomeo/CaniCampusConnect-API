INSERT INTO user (firstname, lastname, email, password, phone)
VALUES ('Alice', 'Smith', 'alice.smith@email.com', 'password123', '0611111111'),
       ('Bob', 'Johnson', 'bob.johnson@email.com', 'securepass', '0622222222'),
       ('Charlie', 'Brown', 'charlie.brown@email.com', 'charliepass', '0633333333'),
       ('Diana', 'Miller', 'diana.miller@email.com', 'dianapass', '0644444444'),
       ('Ethan', 'Davis', 'ethan.davis@email.com', 'ethanpass', '0655555555');

INSERT INTO owner (user_id, is_active, address, registration_date)
VALUES (3, false,  '789 Pine Ln','2023-03-10'),
       (4, true,  '101 Elm Rd','2023-04-20'),
       (5, true,  '202 Maple Dr','2023-05-05');

INSERT INTO club_owner (user_id)
VALUES (1);

INSERT INTO coach (user_id, acaced_number, is_active, registration_date)
VALUES (2, 'OEACHK90', true, '2025-04-16');


