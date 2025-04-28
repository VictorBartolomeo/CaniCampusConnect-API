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

create view OwnersV as
select `o`.`is_active`         AS `is_active`,
       `o`.`registration_date` AS `registration_date`,
       `o`.`user_id`           AS `user_id`,
       `o`.`address`           AS `address`,
       `u`.`email`             AS `email`,
       `u`.`firstname`         AS `firstname`,
       `u`.`lastname`          AS `lastname`,
       `u`.`phone`             AS `phone`
from (`CaniCampusConnect`.`owner` `o` left join `CaniCampusConnect`.`user` `u` on ((`u`.`user_id` = `o`.`user_id`)));


CREATE VIEW CoachesV AS
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

CREATE VIEW ClubOwnersV AS
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
INSERT INTO dog (name, birth_date, gender, chip_number, user_id)
VALUES ('Max', '2020-05-15', 'MALE', 'CHIP123456', 3),
       ('Bella', '2019-08-22', 'FEMALE', 'CHIP789012', 3),
       ('Charlie', '2021-02-10', 'STERILIZED_MALE', 'CHIP345678', 4),
       ('Luna', '2018-11-30', 'FEMALE', 'CHIP901234', 4),
       ('Cooper', '2022-01-05', 'MALE', 'CHIP567890', 5),
       ('Lucy', '2020-07-18', 'STERILIZED_FEMALE', 'CHIP234567', 5);

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
VALUES ('2023-01-10', 25.55, 'KILOGRAM', 1),  -- Max's weight on Jan 10, 2023
       ('2023-03-15', 26.25, 'KILOGRAM', 1),  -- Max's weight on Mar 15, 2023
       ('2023-06-20', 26.85, 'KILOGRAM', 1),  -- Max's weight on Jun 20, 2023
       ('2023-09-25', 27.15, 'KILOGRAM', 1),  -- Max's weight on Sep 25, 2023
       ('2023-01-12', 22.30, 'KILOGRAM', 2),  -- Bella's weight on Jan 12, 2023
       ('2023-05-18', 22.80, 'KILOGRAM', 2),  -- Bella's weight on May 18, 2023
       ('2023-09-22', 23.10,  'KILOGRAM', 2),  -- Bella's weight on Sep 22, 2023
       ('2023-02-05', 30.55, 'KILOGRAM', 3),  -- Charlie's weight on Feb 5, 2023
       ('2023-08-15', 32.00, 'KILOGRAM', 3),  -- Charlie's weight on Aug 15, 2023
       ('2023-03-10', 8.25,  'KILOGRAM', 4),   -- Luna's weight on Mar 10, 2023
       ('2023-07-20', 8.50,  'KILOGRAM', 4),   -- Luna's weight on Jul 20, 2023
       ('2023-11-05', 8.75, 'KILOGRAM', 4),   -- Luna's weight on Nov 5, 2023
       ('2023-04-15', 40.25, 'KILOGRAM', 5),  -- Cooper's weight on Apr 15, 2023
       ('2023-10-25', 41.55, 'KILOGRAM', 5),  -- Cooper's weight on Oct 25, 2023
       ('2023-01-30', 20.50, 'KILOGRAM', 6),  -- Lucy's weight on Jan 30, 2023
       ('2023-06-10', 21.20, 'KILOGRAM', 6),  -- Lucy's weight on Jun 10, 2023
       ('2023-12-05', 21.80, 'KILOGRAM', 6);  -- Lucy's weight on Dec 5, 2023

-- Insert common dog vaccines with their renewal periods
INSERT INTO vaccine (vaccine_name, renew_delay)
VALUES ('Rabies', '2023-01-01'), -- Rabies vaccine, renewed every 3 years
       ('DHPP (Distemper, Hepatitis, Parainfluenza, Parvovirus)', '2023-01-01'), -- DHPP vaccine, renewed annually
       ('Bordetella (Kennel Cough)', '2023-01-01'), -- Bordetella vaccine, renewed every 6 months
       ('Leptospirosis', '2023-01-01'), -- Leptospirosis vaccine, renewed annually
       ('Lyme Disease', '2023-01-01'), -- Lyme disease vaccine, renewed annually
       ('Canine Influenza', '2023-01-01'), -- Canine influenza vaccine, renewed annually
       ('Coronavirus', '2023-01-01'); -- Coronavirus vaccine, renewed annually

-- Insert vaccination records for dogs
INSERT INTO vaccination (vaccination_date, reminder_date, batch_number, veterinarian, dog_id, vaccine_id)
VALUES ('2023-01-15', '2026-01-15', 'RAB123456', 'Dr. Smith', 1, 1), -- Max's rabies vaccination
       ('2023-02-10', '2024-02-10', 'DHPP789012', 'Dr. Johnson', 1, 2), -- Max's DHPP vaccination
       ('2023-03-05', '2023-09-05', 'BOR345678', 'Dr. Smith', 1, 3), -- Max's Bordetella vaccination
       ('2023-01-20', '2026-01-20', 'RAB567890', 'Dr. Wilson', 2, 1), -- Bella's rabies vaccination
       ('2023-02-15', '2024-02-15', 'DHPP123456', 'Dr. Wilson', 2, 2), -- Bella's DHPP vaccination
       ('2023-02-01', '2026-02-01', 'RAB234567', 'Dr. Brown', 3, 1), -- Charlie's rabies vaccination
       ('2023-03-10', '2024-03-10', 'DHPP345678', 'Dr. Brown', 3, 2), -- Charlie's DHPP vaccination
       ('2023-04-05', '2023-10-05', 'BOR456789', 'Dr. Brown', 3, 3), -- Charlie's Bordetella vaccination
       ('2023-01-25', '2026-01-25', 'RAB345678', 'Dr. Davis', 4, 1), -- Luna's rabies vaccination
       ('2023-02-20', '2024-02-20', 'DHPP456789', 'Dr. Davis', 4, 2), -- Luna's DHPP vaccination
       ('2023-03-15', '2024-03-15', 'LEP567890', 'Dr. Davis', 4, 4), -- Luna's Leptospirosis vaccination
       ('2023-02-05', '2026-02-05', 'RAB456789', 'Dr. Miller', 5, 1), -- Cooper's rabies vaccination
       ('2023-03-01', '2024-03-01', 'DHPP567890', 'Dr. Miller', 5, 2), -- Cooper's DHPP vaccination
       ('2023-04-10', '2024-04-10', 'LYM678901', 'Dr. Miller', 5, 5), -- Cooper's Lyme disease vaccination
       ('2023-01-30', '2026-01-30', 'RAB678901', 'Dr. Wilson', 6, 1), -- Lucy's rabies vaccination
       ('2023-02-25', '2024-02-25', 'DHPP678901', 'Dr. Wilson', 6, 2), -- Lucy's DHPP vaccination
       ('2023-03-20', '2023-09-20', 'BOR789012', 'Dr. Wilson', 6, 3); -- Lucy's Bordetella vaccination

-- Insert medication treatments for dogs
INSERT INTO medication_treatment (medication_name, dosage, frequency, start_date, end_date, treatment_reason, dog_id)
VALUES 
    -- Max's treatments
    ('Amoxicillin', '250mg', '08:00:00', '2023-02-15', '2023-02-25', 'Ear infection', 1),
    ('Prednisone', '10mg', '12:00:00', '2023-05-10', '2023-05-20', 'Allergic reaction', 1),
    ('Heartworm preventative', '1 tablet', '09:00:00', '2023-06-01', NULL, 'Monthly heartworm prevention', 1),

    -- Bella's treatments
    ('Carprofen', '75mg', '08:00:00', '2023-03-20', '2023-04-03', 'Joint pain', 2),
    ('Insulin', '10 units', '07:00:00', '2023-07-15', NULL, 'Diabetes management', 2),

    -- Charlie's treatments
    ('Metronidazole', '500mg', '12:00:00', '2023-01-10', '2023-01-20', 'Digestive issues', 3),
    ('Apoquel', '5.4mg', '18:00:00', '2023-04-05', NULL, 'Chronic skin allergy', 3),
    ('Eye drops', '2 drops', '08:00:00', '2023-08-10', '2023-08-24', 'Conjunctivitis', 3),

    -- Luna's treatments
    ('Cephalexin', '300mg', '08:00:00', '2023-02-05', '2023-02-19', 'Skin infection', 4),
    ('Tramadol', '50mg', '12:00:00', '2023-05-15', '2023-05-22', 'Post-surgery pain management', 4),

    -- Cooper's treatments
    ('Fluconazole', '100mg', '08:00:00', '2023-03-10', '2023-03-24', 'Fungal infection', 5),
    ('Phenobarbital', '30mg', '08:00:00', '2023-06-20', NULL, 'Seizure management', 5),
    ('Furosemide', '20mg', '12:00:00', '2023-09-05', NULL, 'Heart condition', 5),

    -- Lucy's treatments
    ('Doxycycline', '100mg', '08:00:00', '2023-01-05', '2023-01-19', 'Tick-borne disease', 6),
    ('Thyroid medication', '0.5mg', '08:00:00', '2023-04-10', NULL, 'Hypothyroidism', 6),
    ('Antihistamine', '10mg', '12:00:00', '2023-07-15', '2023-08-15', 'Seasonal allergies', 6);
