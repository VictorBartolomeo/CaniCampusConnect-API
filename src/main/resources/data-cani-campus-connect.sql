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

create view Owners as
select `o`.`is_active`         AS `is_active`,
       `o`.`registration_date` AS `registration_date`,
       `o`.`user_id`           AS `user_id`,
       `o`.`address`           AS `address`,
       `u`.`email`             AS `email`,
       `u`.`firstname`         AS `firstname`,
       `u`.`lastname`          AS `lastname`,
       `u`.`phone`             AS `phone`
from (`CaniCampusConnect`.`owner` `o` left join `CaniCampusConnect`.`user` `u` on ((`u`.`user_id` = `o`.`user_id`)));


CREATE VIEW Coaches AS
SELECT
    c.acaced_number AS acaced_number,
    c.is_active AS is_active,
    c.registration_date AS registration_date,
    c.user_id AS user_id,
    u.email AS email,
    u.firstname AS firstname,
    u.lastname AS lastname,
    u.phone AS phone
FROM coach c
         LEFT JOIN CaniCampusConnect.user u ON u.user_id = c.user_id;

CREATE VIEW ClubOwners AS
SELECT
    co.user_id AS user_id,
    u.email AS email,
    u.firstname AS firstname,
    u.lastname AS lastname,
    u.phone AS phone
FROM club_owner co
         LEFT JOIN CaniCampusConnect.user u ON u.user_id = co.user_id;

-- TODO Demander à Franck pourquoi le register avec les infos du coach me crée juste un user et non pas un coach, comment valider cela

-- Insert common dog breeds
INSERT INTO breed (name)
VALUES ('Labrador Retriever'),
       ('German Shepherd'),
       ('Golden Retriever'),
       ('French Bulldog'),
       ('Bulldog'),
       ('Poodle'),
       ('Beagle'),
       ('Rottweiler'),
       ('Yorkshire Terrier'),
       ('Boxer'),
       ('Dachshund'),
       ('Siberian Husky'),
       ('Great Dane'),
       ('Doberman Pinscher'),
       ('Shih Tzu'),
       ('Chihuahua'),
       ('Border Collie'),
       ('Cavalier King Charles Spaniel'),
       ('Australian Shepherd'),
       ('Bernese Mountain Dog');

-- Insert dogs (linked to owners with user_id 3, 4, and 5)
INSERT INTO dog (name, birth_date, is_male, is_sociable, is_in_heat, is_crossbreed, chip_number, user_id)
VALUES ('Max', '2020-05-15', true, true, false, false, 'CHIP123456', 3),
       ('Bella', '2019-08-22', false, true, false, false, 'CHIP789012', 3),
       ('Charlie', '2021-02-10', true, false, false, true, 'CHIP345678', 4),
       ('Luna', '2018-11-30', false, true, true, false, 'CHIP901234', 4),
       ('Cooper', '2022-01-05', true, true, false, false, 'CHIP567890', 5),
       ('Lucy', '2020-07-18', false, true, false, true, 'CHIP234567', 5);

-- Insert dog_breed relationships
INSERT INTO dog_breed (dog_id, breed_id)
VALUES (1, 1),  -- Max is a Labrador Retriever
       (2, 3),  -- Bella is a Golden Retriever
       (3, 2),  -- Charlie is a German Shepherd
       (3, 5),  -- Charlie is also part Bulldog (crossbreed)
       (4, 6),  -- Luna is a Poodle
       (5, 8),  -- Cooper is a Rottweiler
       (6, 10), -- Lucy is a Boxer
       (6, 16); -- Lucy is also part Chihuahua (crossbreed)

-- Insert dog weights
INSERT INTO dog_weight (measurement_date, weight_value, unit, dog_id)
VALUES ('2023-01-10', 25.5, 'KILOGRAM', 1),  -- Max's weight on Jan 10, 2023
       ('2023-03-15', 26.2, 'KILOGRAM', 1),  -- Max's weight on Mar 15, 2023
       ('2023-06-20', 26.8, 'KILOGRAM', 1),  -- Max's weight on Jun 20, 2023
       ('2023-09-25', 27.1, 'KILOGRAM', 1),  -- Max's weight on Sep 25, 2023
       ('2023-01-12', 22.3, 'KILOGRAM', 2),  -- Bella's weight on Jan 12, 2023
       ('2023-05-18', 22.8, 'KILOGRAM', 2),  -- Bella's weight on May 18, 2023
       ('2023-09-22', 23.1, 'KILOGRAM', 2),  -- Bella's weight on Sep 22, 2023
       ('2023-02-05', 30.5, 'KILOGRAM', 3),  -- Charlie's weight on Feb 5, 2023
       ('2023-08-15', 32.0, 'KILOGRAM', 3),  -- Charlie's weight on Aug 15, 2023
       ('2023-03-10', 8.2, 'KILOGRAM', 4),   -- Luna's weight on Mar 10, 2023
       ('2023-07-20', 8.5, 'KILOGRAM', 4),   -- Luna's weight on Jul 20, 2023
       ('2023-11-05', 8.7, 'KILOGRAM', 4),   -- Luna's weight on Nov 5, 2023
       ('2023-04-15', 40.2, 'KILOGRAM', 5),  -- Cooper's weight on Apr 15, 2023
       ('2023-10-25', 41.5, 'KILOGRAM', 5),  -- Cooper's weight on Oct 25, 2023
       ('2023-01-30', 20.5, 'KILOGRAM', 6),  -- Lucy's weight on Jan 30, 2023
       ('2023-06-10', 21.2, 'KILOGRAM', 6),  -- Lucy's weight on Jun 10, 2023
       ('2023-12-05', 21.8, 'KILOGRAM', 6);  -- Lucy's weight on Dec 5, 2023
