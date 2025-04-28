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
