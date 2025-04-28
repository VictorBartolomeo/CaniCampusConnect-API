INSERT INTO user (firstname, lastname, email, password, phone)
VALUES ('Admin', 'Admin', 'admin@admin.com', 'password123', '0611111111'),
       ('Coach', 'Coach', 'coach@coach.com', 'securepass', '0622222222'),
       ('Propriétaire1', 'Utilisateur1', 'owner3@user.com', 'charliepass', '0633333333'),
       ('Propriétaire2', 'Utilisateur2', 'owner4@user.com', 'dianapass', '0644444444'),
       ('Propriétaire3', 'Utilisateur3', 'owner5@user.com', 'ethanpass', '0655555555');

INSERT INTO owner (user_id, is_active, address, registration_date)
VALUES (3, false,  '789 Rue des Pins','2023-03-10'),
       (4, true,  '101 Avenue des Ormes','2023-04-20'),
       (5, true,  '202 Boulevard des Érables','2023-05-05');

INSERT INTO club_owner (user_id)
VALUES (1);

-- Insert clubs (linked to club_owner with user_id 1)
INSERT INTO club (name, address, user_id)
VALUES ('Les moustaches Messines', '123 Rue des Chiens, Paris', 1);

INSERT INTO coach (user_id, acaced_number, is_active, registration_date)
VALUES (2, 'OEACHK90', true, '2025-04-16');

-- Insert age ranges for course types
INSERT INTO age_range (min_age, max_age)
VALUES (0, 12),    -- 0-12 months (puppies)
       (13, 36),   -- 13-36 months (young dogs)
       (37, 84),   -- 37-84 months (adult dogs)
       (85, 999);  -- 85+ months (senior dogs)

-- Insert course types
INSERT INTO course_type (name, description, age_range_id)
VALUES ('Socialisation des Chiots', 'Socialisation de base et introduction à l''éducation pour chiots', 1),
       ('Obéissance de Base', 'Commandes fondamentales et éducation comportementale pour tous les chiens', 2),
       ('Obéissance Avancée', 'Commandes complexes et entraînement sans laisse pour chiens bien éduqués', 3),
       ('Entraînement d''Agilité', 'Parcours d''obstacles pour chiens sportifs', 2),
       ('Formation de Chien Thérapeutique', 'Formation pour chiens destinés à devenir des animaux thérapeutiques certifiés', 3),
       ('Bien-être des Chiens Seniors', 'Exercices doux et stimulation mentale pour chiens âgés', 4);

-- Insert courses
INSERT INTO course (title, description, start_datetime, end_datetime, max_capacity, user_id, club_id, course_type_id)
VALUES 
    ('Bases pour Chiots - Session Printemps', 'Introduction aux commandes de base et socialisation pour chiots', '2023-04-15 10:00:00', '2023-04-15 11:30:00', 8, 2, 1, 1),
    ('Obéissance Intermédiaire - Weekend', 'Formation complémentaire pour chiens ayant terminé l''obéissance de base', '2023-04-16 14:00:00', '2023-04-16 16:00:00', 10, 2, 1, 2),
    ('Agilité pour Débutants', 'Introduction aux équipements d''agilité et techniques de base', '2023-04-22 09:00:00', '2023-04-22 11:00:00', 6, 2, 1, 4),
    ('Atelier Commandes Avancées', 'Atelier intensif d''une journée pour commandes et comportements avancés', '2023-05-06 13:00:00', '2023-05-06 17:00:00', 8, 2, 1, 3),
    ('Préparation Chien Thérapeutique', 'Préparation à la certification de chien thérapeutique', '2023-05-13 10:00:00', '2023-05-13 12:00:00', 5, 2, 1, 5),
    ('Enrichissement pour Chiens Seniors', 'Stimulation mentale et exercices doux pour chiens âgés', '2023-05-20 11:00:00', '2023-05-20 12:00:00', 6, 2, 1, 6);

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
       ('Berger Allemand'),
       ('Golden Retriever'),
       ('Bouledogue Français'),
       ('Bouledogue'),
       ('Caniche'),
       ('Beagle'),
       ('Rottweiler'),
       ('Yorkshire Terrier'),
       ('Boxer'),
       ('Teckel'),
       ('Husky Sibérien'),
       ('Dogue Allemand'),
       ('Dobermann'),
       ('Shih Tzu'),
       ('Chihuahua'),
       ('Border Collie'),
       ('Cavalier King Charles'),
       ('Berger Australien'),
       ('Bouvier Bernois');

-- Insert dogs (linked to owners with user_id 3, 4, and 5)
INSERT INTO dog (name, birth_date, gender, chip_number, user_id)
VALUES ('Rex', '2020-05-15', 'MALE', 'CHIP123456', 3),
       ('Nala', '2019-08-22', 'FEMALE', 'CHIP789012', 3),
       ('Oscar', '2021-02-10', 'STERILIZED_MALE', 'CHIP345678', 4),
       ('Lola', '2018-11-30', 'FEMALE', 'CHIP901234', 4),
       ('Filou', '2022-01-05', 'MALE', 'CHIP567890', 5),
       ('Mia', '2020-07-18', 'STERILIZED_FEMALE', 'CHIP234567', 5);

-- Insert dog_breed relationships
INSERT INTO dog_breed (dog_id, breed_id)
VALUES (1, 1),  -- Rex est un Labrador Retriever
       (2, 3),  -- Nala est un Golden Retriever
       (3, 2),  -- Oscar est un Berger Allemand
       (3, 5),  -- Oscar est aussi en partie Bulldog (croisé)
       (4, 6),  -- Lola est un Caniche
       (5, 8),  -- Filou est un Rottweiler
       (6, 10), -- Mia est un Boxer
       (6, 16); -- Mia est aussi en partie Chihuahua (croisé)

-- Insert dog weights
INSERT INTO dog_weight (measurement_date, weight_value, unit, dog_id)
VALUES ('2023-01-10', 25.55, 'KILOGRAM', 1),  -- Poids de Rex le 10 Jan 2023
       ('2023-03-15', 26.25, 'KILOGRAM', 1),  -- Poids de Rex le 15 Mar 2023
       ('2023-06-20', 26.85, 'KILOGRAM', 1),  -- Poids de Rex le 20 Juin 2023
       ('2023-09-25', 27.15, 'KILOGRAM', 1),  -- Poids de Rex le 25 Sep 2023
       ('2023-01-12', 22.30, 'KILOGRAM', 2),  -- Poids de Nala le 12 Jan 2023
       ('2023-05-18', 22.80, 'KILOGRAM', 2),  -- Poids de Nala le 18 Mai 2023
       ('2023-09-22', 23.10,  'KILOGRAM', 2),  -- Poids de Nala le 22 Sep 2023
       ('2023-02-05', 30.55, 'KILOGRAM', 3),  -- Poids d'Oscar le 5 Fév 2023
       ('2023-08-15', 32.00, 'KILOGRAM', 3),  -- Poids d'Oscar le 15 Août 2023
       ('2023-03-10', 8.25,  'KILOGRAM', 4),   -- Poids de Lola le 10 Mar 2023
       ('2023-07-20', 8.50,  'KILOGRAM', 4),   -- Poids de Lola le 20 Juil 2023
       ('2023-11-05', 8.75, 'KILOGRAM', 4),   -- Poids de Lola le 5 Nov 2023
       ('2023-04-15', 40.25, 'KILOGRAM', 5),  -- Poids de Filou le 15 Avr 2023
       ('2023-10-25', 41.55, 'KILOGRAM', 5),  -- Poids de Filou le 25 Oct 2023
       ('2023-01-30', 20.50, 'KILOGRAM', 6),  -- Poids de Mia le 30 Jan 2023
       ('2023-06-10', 21.20, 'KILOGRAM', 6),  -- Poids de Mia le 10 Juin 2023
       ('2023-12-05', 21.80, 'KILOGRAM', 6);  -- Poids de Mia le 5 Déc 2023

-- Insert common dog vaccines with their renewal periods
INSERT INTO vaccine (vaccine_name, renew_delay)
VALUES ('Rage', '2023-01-01'), -- Vaccin contre la rage, renouvelé tous les 3 ans
       ('CHPPIL (Carré, Hépatite, Parvovirose, Parainfluenza, Leptospirose)', '2023-01-01'), -- Vaccin CHPPIL, renouvelé annuellement
       ('Bordetella (Toux du Chenil)', '2023-01-01'), -- Vaccin Bordetella, renouvelé tous les 6 mois
       ('Leptospirose', '2023-01-01'), -- Vaccin contre la leptospirose, renouvelé annuellement
       ('Maladie de Lyme', '2023-01-01'), -- Vaccin contre la maladie de Lyme, renouvelé annuellement
       ('Grippe Canine', '2023-01-01'), -- Vaccin contre la grippe canine, renouvelé annuellement
       ('Coronavirus', '2023-01-01'); -- Vaccin contre le coronavirus, renouvelé annuellement

-- Insert vaccination records for dogs
INSERT INTO vaccination (vaccination_date, reminder_date, batch_number, veterinarian, dog_id, vaccine_id)
VALUES ('2023-01-15', '2026-01-15', 'RAB123456', 'Dr. Dupont', 1, 1), -- Vaccination contre la rage de Rex
       ('2023-02-10', '2024-02-10', 'DHPP789012', 'Dr. Martin', 1, 2), -- Vaccination CHPPIL de Rex
       ('2023-03-05', '2023-09-05', 'BOR345678', 'Dr. Dupont', 1, 3), -- Vaccination Bordetella de Rex
       ('2023-01-20', '2026-01-20', 'RAB567890', 'Dr. Bernard', 2, 1), -- Vaccination contre la rage de Nala
       ('2023-02-15', '2024-02-15', 'DHPP123456', 'Dr. Bernard', 2, 2), -- Vaccination CHPPIL de Nala
       ('2023-02-01', '2026-02-01', 'RAB234567', 'Dr. Petit', 3, 1), -- Vaccination contre la rage d'Oscar
       ('2023-03-10', '2024-03-10', 'DHPP345678', 'Dr. Petit', 3, 2), -- Vaccination CHPPIL d'Oscar
       ('2023-04-05', '2023-10-05', 'BOR456789', 'Dr. Petit', 3, 3), -- Vaccination Bordetella d'Oscar
       ('2023-01-25', '2026-01-25', 'RAB345678', 'Dr. Moreau', 4, 1), -- Vaccination contre la rage de Lola
       ('2023-02-20', '2024-02-20', 'DHPP456789', 'Dr. Moreau', 4, 2), -- Vaccination CHPPIL de Lola
       ('2023-03-15', '2024-03-15', 'LEP567890', 'Dr. Moreau', 4, 4), -- Vaccination contre la leptospirose de Lola
       ('2023-02-05', '2026-02-05', 'RAB456789', 'Dr. Leroy', 5, 1), -- Vaccination contre la rage de Filou
       ('2023-03-01', '2024-03-01', 'DHPP567890', 'Dr. Leroy', 5, 2), -- Vaccination CHPPIL de Filou
       ('2023-04-10', '2024-04-10', 'LYM678901', 'Dr. Leroy', 5, 5), -- Vaccination contre la maladie de Lyme de Filou
       ('2023-01-30', '2026-01-30', 'RAB678901', 'Dr. Bernard', 6, 1), -- Vaccination contre la rage de Mia
       ('2023-02-25', '2024-02-25', 'DHPP678901', 'Dr. Bernard', 6, 2), -- Vaccination CHPPIL de Mia
       ('2023-03-20', '2023-09-20', 'BOR789012', 'Dr. Bernard', 6, 3); -- Vaccination Bordetella de Mia

-- Insert medication treatments for dogs
INSERT INTO medication_treatment (medication_name, dosage, frequency, start_date, end_date, treatment_reason, dog_id)
VALUES 
    -- Traitements de Rex
    ('Amoxicilline', '250mg', '08:00:00', '2023-02-15', '2023-02-25', 'Infection de l''oreille', 1),
    ('Prednisolone', '10mg', '12:00:00', '2023-05-10', '2023-05-20', 'Réaction allergique', 1),
    ('Préventif contre les vers du cœur', '1 comprimé', '09:00:00', '2023-06-01', NULL, 'Prévention mensuelle contre les vers du cœur', 1),

    -- Traitements de Nala
    ('Carprofène', '75mg', '08:00:00', '2023-03-20', '2023-04-03', 'Douleur articulaire', 2),
    ('Insuline', '10 unités', '07:00:00', '2023-07-15', NULL, 'Gestion du diabète', 2),

    -- Traitements d'Oscar
    ('Métronidazole', '500mg', '12:00:00', '2023-01-10', '2023-01-20', 'Problèmes digestifs', 3),
    ('Apoquel', '5.4mg', '18:00:00', '2023-04-05', NULL, 'Allergie cutanée chronique', 3),
    ('Gouttes oculaires', '2 gouttes', '08:00:00', '2023-08-10', '2023-08-24', 'Conjonctivite', 3),

    -- Traitements de Lola
    ('Céfalexine', '300mg', '08:00:00', '2023-02-05', '2023-02-19', 'Infection cutanée', 4),
    ('Tramadol', '50mg', '12:00:00', '2023-05-15', '2023-05-22', 'Gestion de la douleur post-chirurgicale', 4),

    -- Traitements de Filou
    ('Fluconazole', '100mg', '08:00:00', '2023-03-10', '2023-03-24', 'Infection fongique', 5),
    ('Phénobarbital', '30mg', '08:00:00', '2023-06-20', NULL, 'Gestion des crises d''épilepsie', 5),
    ('Furosémide', '20mg', '12:00:00', '2023-09-05', NULL, 'Problème cardiaque', 5),

    -- Traitements de Mia
    ('Doxycycline', '100mg', '08:00:00', '2023-01-05', '2023-01-19', 'Maladie transmise par les tiques', 6),
    ('Médicament pour la thyroïde', '0.5mg', '08:00:00', '2023-04-10', NULL, 'Hypothyroïdie', 6),
    ('Antihistaminique', '10mg', '12:00:00', '2023-07-15', '2023-08-15', 'Allergies saisonnières', 6);

-- Insert registrations for dogs in courses
INSERT INTO registration (registration_date, status, dog_id, course_id)
VALUES 
    -- Inscriptions de Rex (dog_id 1)
    ('2023-03-15 09:30:00', 'CONFIRMED', 1, 1),  -- Rex inscrit à Bases pour Chiots
    ('2023-04-01 14:15:00', 'CONFIRMED', 1, 3),  -- Rex inscrit à Agilité pour Débutants
    ('2023-04-25 10:45:00', 'PENDING', 1, 4),    -- Rex en attente pour Atelier Commandes Avancées

    -- Inscriptions de Nala (dog_id 2)
    ('2023-03-20 11:00:00', 'CONFIRMED', 2, 2),  -- Nala inscrite à Obéissance Intermédiaire
    ('2023-04-05 16:30:00', 'CANCELLED', 2, 3),  -- Nala a annulé Agilité pour Débutants
    ('2023-05-01 09:15:00', 'CONFIRMED', 2, 5),  -- Nala inscrite à Préparation Chien Thérapeutique

    -- Inscriptions d'Oscar (dog_id 3)
    ('2023-04-02 10:00:00', 'CONFIRMED', 3, 2),  -- Oscar inscrit à Obéissance Intermédiaire
    ('2023-04-28 15:45:00', 'CONFIRMED', 3, 4),  -- Oscar inscrit à Atelier Commandes Avancées

    -- Inscriptions de Lola (dog_id 4)
    ('2023-03-25 13:30:00', 'CONFIRMED', 4, 1),  -- Lola inscrite à Bases pour Chiots
    ('2023-05-10 11:15:00', 'PENDING', 4, 5),    -- Lola en attente pour Préparation Chien Thérapeutique

    -- Inscriptions de Filou (dog_id 5)
    ('2023-04-10 09:45:00', 'CONFIRMED', 5, 3),  -- Filou inscrit à Agilité pour Débutants
    ('2023-05-15 14:00:00', 'CONFIRMED', 5, 6),  -- Filou inscrit à Enrichissement pour Chiens Seniors

    -- Inscriptions de Mia (dog_id 6)
    ('2023-04-15 10:30:00', 'CONFIRMED', 6, 2),  -- Mia inscrite à Obéissance Intermédiaire
    ('2023-05-05 13:45:00', 'CANCELLED', 6, 4),  -- Mia a annulé Atelier Commandes Avancées
    ('2023-05-18 11:30:00', 'CONFIRMED', 6, 6);  -- Mia inscrite à Enrichissement pour Chiens Seniors

-- Insert veterinary visits for dogs
INSERT INTO veterinary_visit (visit_date, diagnosis, reason_for_visit, treatment, veterinarian, dog_id)
VALUES 
    -- Visites de Rex
    ('2023-02-10', 'Otite externe à l''oreille droite', 'Grattement d''oreille et secouement de tête', 'Prescription d''Amoxicilline pour 10 jours et solution de nettoyage auriculaire', 'Dr. Dupont', 1),
    ('2023-05-05', 'Réaction allergique à une piqûre d''insecte', 'Gonflement du museau et urticaire', 'Administration d''injection d''antihistaminique, prescription de Prednisolone', 'Dr. Martin', 1),
    ('2023-09-15', 'Bilan annuel, tous les systèmes normaux', 'Examen de routine', 'Mise à jour des vaccinations, test de dirofilariose négatif', 'Dr. Dupont', 1),

    -- Visites de Nala
    ('2023-03-15', 'Arthrose des articulations de la hanche', 'Difficulté à se lever et boiterie', 'Prescription de Carprofène pour la gestion de la douleur, recommandation de gestion du poids', 'Dr. Bernard', 2),
    ('2023-07-10', 'Diabète sucré', 'Augmentation de la soif, miction et perte de poids', 'Début de l''insulinothérapie, changements alimentaires recommandés', 'Dr. Bernard', 2),
    ('2023-11-20', 'Suivi du diabète', 'Suivi pour la gestion du diabète', 'Ajustement de la posologie d''insuline, amélioration des niveaux de glucose sanguin', 'Dr. Bernard', 2),

    -- Visites d'Oscar
    ('2023-01-05', 'Gastro-entérite', 'Vomissements et diarrhée depuis 2 jours', 'Prescription de Métronidazole, recommandation d''un régime fade pendant 5 jours', 'Dr. Petit', 3),
    ('2023-04-01', 'Dermatite atopique', 'Démangeaisons persistantes et rougeurs sur la peau', 'Début d''Apoquel, recommandation de shampooing hypoallergénique', 'Dr. Petit', 3),
    ('2023-08-05', 'Conjonctivite', 'Yeux rouges, gonflés avec écoulement', 'Prescription de gouttes antibiotiques pour les yeux, collier élisabéthain pour éviter les grattements', 'Dr. Moreau', 3),

    -- Visites de Lola
    ('2023-02-01', 'Infection bactérienne de la peau', 'Points chauds et perte de poils sur le dos', 'Prescription de Céfalexine, bains médicamenteux deux fois par semaine', 'Dr. Moreau', 4),
    ('2023-05-10', 'Déchirure du ligament croisé', 'Boiterie soudaine de la patte arrière droite', 'Réparation chirurgicale effectuée, prescription de Tramadol pour la douleur', 'Dr. Leroy', 4),
    ('2023-06-01', 'Suivi post-chirurgical', 'Surveillance de la cicatrisation du site chirurgical', 'Retrait des sutures, exercices de rééducation recommandés', 'Dr. Leroy', 4),

    -- Visites de Filou
    ('2023-03-05', 'Infection fongique de la peau', 'Lésions circulaires sur la peau avec perte de poils', 'Prescription de Fluconazole, crème antifongique topique', 'Dr. Leroy', 5),
    ('2023-06-15', 'Épilepsie idiopathique', 'Deux épisodes de crises signalés', 'Début de Phénobarbital, analyses sanguines pour surveiller la fonction hépatique', 'Dr. Dupont', 5),
    ('2023-09-01', 'Insuffisance cardiaque congestive', 'Toux et intolérance à l''exercice', 'Prescription de Furosémide, recommandation d''activité restreinte', 'Dr. Martin', 5),

    -- Visites de Mia
    ('2023-01-01', 'Maladie de Lyme', 'Boiterie de plusieurs pattes, fièvre', 'Prescription de Doxycycline, repos recommandé', 'Dr. Bernard', 6),
    ('2023-04-05', 'Hypothyroïdie', 'Prise de poids, léthargie, amincissement des poils', 'Début de médicament pour la thyroïde, suivi dans 1 mois', 'Dr. Bernard', 6),
    ('2023-07-10', 'Allergies saisonnières', 'Démangeaisons, éternuements et yeux larmoyants', 'Prescription d''antihistaminique, recommandation de temps limité à l''extérieur pendant les jours à forte concentration de pollen', 'Dr. Moreau', 6);
