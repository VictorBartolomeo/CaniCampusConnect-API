INSERT INTO user (firstname, lastname, email, password, phone)
VALUES ('Alice', 'Smith', 'alice.smith@email.com', 'password123', '0611111111'),
       ('Bob', 'Johnson', 'bob.johnson@email.com', 'securepass', '0622222222'),
       ('Charlie', 'Brown', 'charlie.brown@email.com', 'charliepass', '0633333333'),
       ('Diana', 'Miller', 'diana.miller@email.com', 'dianapass', '0644444444'),
       ('Ethan', 'Davis', 'ethan.davis@email.com', 'ethanpass', '0655555555');

INSERT INTO owner (user_id, is_active, address, registration_date)
VALUES (1, true,  '123 Main St','2023-01-01'),
       (2, true,  '456 Oak Ave','2023-02-15'),
       (3, false,  '789 Pine Ln','2023-03-10'),

INSERT INTO breed (name)
VALUES ('Golden Retriever'),
       ('German Shepherd'),
       ('Poodle'),
       ('Bulldog'),
       ('Beagle');

INSERT INTO department (name)
VALUES ('Moselle');

INSERT INTO club (name, address, phone, email, department_id)
VALUES ('Dog Club', '22 rue Lothaire', '0601010101', 'dogclub@email.com', 1);

INSERT INTO dog (name, birth_date, is_male, is_sociable, is_in_heat, is_crossbreed, chip_number, breed_id)
VALUES ('Buddy', '2020-05-10', true, true, false, false, '123456789012345', 1),
       ('Lucy', '2021-03-15', false, true, false, false, '987654321098765', 2),
       ('Max', '2019-11-20', true, true, false, false, '112233445566778', 3),
       ('Daisy', '2022-01-05', false, true, false, true, '887766554433221', 4),
       ('Rocky', '2020-08-25', true, false, false, false, '135792468013579', 5);

INSERT INTO coach (user_id, acaced_number, is_active, registration_date)
VALUES (4, '123456', true, '2023-01-01'),
       (5, '789012', true,'2023-02-15'),


INSERT INTO age_range (min_age, max_age)
VALUES (0, 6),
       (6, 12),
       (12, 36),
       (36, 512);

INSERT INTO course_type (name, description, age_range_id)
VALUES ('Basic Obedience', 'Fundamental commands for all dogs.', 3),
       ('Advanced Obedience', 'Building on basic obedience with more complex commands.', 3),
       ('Agility Training', 'Navigating obstacle courses for fun and exercise.', 3),
       ('Puppy Socialization', 'Early socialization for puppies.', 1),
       ('Behavior Modification', 'Addressing specific behavioral issues.', 3),
       ('Trick Training', 'Teaching fun and impressive tricks.', 3),
       ('Junior Agility', 'Agility training for younger dogs.', 2),
       ('Senior Care', 'Specialized care for senior dogs.', 4);

INSERT INTO course (title, description, start_datetime, end_datetime, max_capacity, reserved_capacity, price,
                    coach_id, course_type_id)
VALUES ('Beginner Basics', 'Introduction to basic obedience.', '2024-07-01 10:00:00', '2024-07-01 11:00:00', 10, 5,
        150.00, 1, 1),
       ('Sit, Stay, Come', 'Mastering the essential commands.', '2024-07-02 14:00:00', '2024-07-02 15:00:00', 8, 3,
        120.00, 2, 1),
       ('Leash Manners', 'Walking politely on a leash.', '2024-07-03 16:00:00', '2024-07-03 17:00:00', 12, 7, 100.00,
        3, 1),
       ('Recall Mastery', 'Reliable recall in various environments.', '2024-07-04 09:00:00', '2024-07-04 10:00:00', 6,
        2, 180.00, 2, 2),
       ('Advanced Heel', 'Perfecting the heel command.', '2024-07-05 11:00:00', '2024-07-05 12:00:00', 10, 6, 140.00,
        3, 2),
       ('Off-Leash Control', 'Maintaining control without a leash.', '2024-07-06 13:00:00', '2024-07-06 14:00:00', 8, 4,
        220.00, 1, 2),
       ('Agility Fun', 'Introduction to agility obstacles.', '2024-07-07 15:00:00', '2024-07-07 16:00:00', 10, 5,
        160.00, 2, 3),
       ('Agility Skills', 'Improving agility skills and speed.', '2024-07-08 17:00:00', '2024-07-08 18:00:00', 12, 8,
        200.00, 3, 3),
       ('Puppy Playtime', 'Safe and supervised puppy play.', '2024-07-09 10:00:00', '2024-07-09 11:00:00', 15, 10,
        80.00, 1, 4),
       ('Puppy Socialization Class', 'Exposing puppies to new sights, sounds, and dogs.', '2024-07-10 14:00:00',
        '2024-07-10 15:00:00', 10, 6, 120.00, 1, 4),
       ('Barking Solutions', 'Reducing excessive barking.', '2024-07-11 16:00:00', '2024-07-11 17:00:00', 8, 4, 130.00,
        1, 5),
       ('Jumping Solutions', 'Reducing jumping on people.', '2024-07-12 09:00:00', '2024-07-12 10:00:00', 10, 5, 130.00,
        2, 5),
       ('Trickster 101', 'Introduction to trick training.', '2024-07-13 11:00:00', '2024-07-13 12:00:00', 12, 7, 110.00,
        3, 6),
       ('Advanced Tricks', 'Learning more complex tricks.', '2024-07-14 13:00:00', '2024-07-14 14:00:00', 8, 3, 150.00,
        2, 6),
       ('Junior Agility Fun', 'Introduction to agility for junior dogs.', '2024-07-15 15:00:00', '2024-07-15 16:00:00',
        10, 6, 140.00, 2, 7),
       ('Senior Gentle Walks', 'Gentle walks and exercises for senior dogs.', '2024-07-16 17:00:00',
        '2024-07-16 18:00:00', 8, 4, 90.00, 1, 8);
