INSERT INTO user (firstname, lastname, email, password, phone)
VALUES ('PrénomAdmin', 'NomAdmin', 'admin@admin.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0611111111'),
       ('PrénomCoach', 'NomCoach', 'coach@coach.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0622222222'),
       ('Propriétaire1', 'Utilisateur1', 'owner3@user.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0633333333'),
       ('Propriétaire2', 'Utilisateur2', 'owner4@user.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0644444444'),
       ('Propriétaire3', 'Utilisateur3', 'owner5@user.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0655555555');

INSERT INTO club_owner (user_id)
VALUES (1);

INSERT INTO coach (user_id, acaced_number, is_active, registration_date)
VALUES (2, 'OEACHK90', true, '2025-04-16');

INSERT INTO owner (user_id, is_active, address, registration_date)
VALUES (3, false, '789 Rue des Pins', '2023-03-10'),
       (4, true, '101 Avenue des Ormes', '2023-04-20'),
       (5, true, '202 Boulevard des Érables', '2023-05-05');

INSERT INTO club (name, address, user_id)
VALUES ('Les moustaches Messines', '123 Rue des Chiens, Paris', 1);

INSERT INTO age_range (min_age, max_age)
VALUES (0, 12),  -- 0-12 mois (chiots)
       (13, 36), -- 13-36 mois (jeune chien)
       (37, 84), -- 37-84 mois (adulte)
       (85, 999); -- 85+ mois (senior)

INSERT INTO course_type (name, description, age_range_id)
VALUES ('Socialisation des Chiots', 'Socialisation de base et introduction à l''éducation pour chiots', 1),
       ('Obéissance de Base', 'Commandes fondamentales et éducation comportementale pour tous les chiens', 2),
       ('Obéissance Avancée', 'Commandes complexes et entraînement sans laisse pour chiens bien éduqués', 3),
       ('Entraînement d''Agilité', 'Parcours d''obstacles pour chiens sportifs', 2),
       ('Formation de Chien Thérapeutique',
        'Formation pour chiens destinés à devenir des animaux thérapeutiques certifiés', 3),
       ('Bien-être des Chiens Seniors', 'Exercices doux et stimulation mentale pour chiens âgés', 4);

INSERT INTO course (title, description, start_datetime, end_datetime, max_capacity, user_id, club_id, course_type_id)
VALUES ('Chiots - Éveil et Socialisation', 'On va apprendre à votre chiot les bases de l''éducation et le faire jouer avec d''autres pour une bonne socialisation',
        '2025-05-15 10:00:00', '2025-05-15 11:30:00', 8, 2, 1, 1),
       ('Obéissance du quotidien',
        'Pour les chiens qui connaissent déjà quelques bases, on va travailler sur les situations de tous les jours', '2025-05-22 14:00:00',
        '2025-05-22 16:00:00', 10, 2, 1, 2),
       ('Découverte de l''agilité', 'Venez vous amuser avec votre chien sur différents obstacles, parfait pour renforcer votre complicité',
        '2025-06-05 09:00:00', '2025-06-05 11:00:00', 6, 2, 1, 4),
       ('Trucs et astuces avancés', 'Une journée pour apprendre des techniques qui impressionneront vos amis et faciliteront votre vie',
        '2025-06-18 13:00:00', '2025-06-18 17:00:00', 8, 2, 1, 3),
       ('Formation chien visiteur', 'Préparation pour les chiens qui aiment le contact et pourraient rendre visite aux personnes isolées',
        '2025-07-10 10:00:00', '2025-07-10 12:00:00', 5, 2, 1, 5),
       ('Bien-être des seniors', 'Activités douces et stimulantes adaptées à nos compagnons plus âgés',
        '2025-07-25 11:00:00', '2025-07-25 12:00:00', 6, 2, 1, 6),
       ('Chiots d''été', 'Venez avec votre boule de poils pour lui apprendre les bases tout en s''amusant',
        '2025-08-12 10:00:00', '2025-08-12 11:30:00', 8, 2, 1, 1),
       ('Perfectionnement obéissance',
        'Pour les chiens qui ont déjà de bonnes bases, on va aller plus loin ensemble', '2025-09-05 14:00:00',
        '2025-09-05 17:00:00', 6, 2, 1, 3),
       ('Parcours d''agilité', 'On va travailler sur des parcours plus complexes pour les chiens qui ont déjà découvert l''agilité',
        '2025-09-20 09:00:00', '2025-09-20 11:30:00', 5, 2, 1, 4),
       ('Préparation concours', 'Vous voulez participer à des compétitions? On vous donne toutes les clés pour réussir',
        '2025-10-15 13:00:00', '2025-10-15 16:00:00', 4, 2, 1, 3),
       ('Jeux d''intelligence canine', 'Des activités ludiques pour stimuler le cerveau de votre chien et renforcer votre lien',
        '2025-11-08 10:00:00', '2025-11-08 12:00:00', 8, 2, 1, 2),
       ('Rappel en toutes situations', 'Fini les angoisses au parc! Apprenez à avoir un rappel fiable même avec des distractions',
        '2025-12-06 14:00:00', '2025-12-06 16:00:00', 6, 2, 1, 2),
       ('Balades éducatives', 'On sort en groupe pour mettre en pratique les apprentissages dans un environnement réel',
        '2026-01-18 13:30:00', '2026-01-18 15:30:00', 8, 2, 1, 2),
       ('Initiation au pistage', 'Découvrez comment utiliser le flair incroyable de votre chien pour des activités stimulantes',
        '2026-02-22 10:00:00', '2026-02-22 12:00:00', 5, 2, 1, 3),
       ('Chiots d''hiver', 'Session spéciale pour les nouveaux chiots: éducation de base et jeux de socialisation',
        '2026-03-15 10:00:00', '2026-03-15 11:30:00', 8, 2, 1, 1),
       ('Sports canins ludiques', 'Découverte de différentes activités sportives à faire avec votre chien pour le fun',
        '2026-04-05 14:00:00', '2026-04-05 16:30:00', 6, 2, 1, 4);

INSERT INTO breed (name)
VALUES ('Berger Australien'),
       ('Golden Retriever'),
       ('Staffordshire Bull Terrier'),
       ('Berger Belge'),                       -- Note: Souvent dominé par le Malinois mais regroupe les 4 variétés
       ('Labrador Retriever'),
       ('Berger Allemand'),
       ('Setter Anglais'),
       ('Épagneul Breton'),
       ('Beagle'),
       ('Cavalier King Charles Spaniel'),
       ('Bouledogue Français'),
       ('Cocker Spaniel Anglais'),
       ('Teckel'),                             -- Regroupe les différentes tailles/poils
       ('Yorkshire Terrier'),
       ('Cane Corso'),
       ('Husky de Sibérie'),
       ('Shih Tzu'),
       ('American Staffordshire Terrier'),
       ('Chihuahua'),
       ('Jack Russell Terrier'),
       ('Border Collie'),
       ('Pointer Anglais'),                    -- Souvent appelé simplement Pointer
       ('Dogue Allemand'),
       ('Rottweiler'),
       ('Berger Blanc Suisse'),
       ('Samoyède'),
       ('West Highland White Terrier'),        -- Westie
       ('Basset Hound'),
       ('Dalmatien'),
       ('Akita Inu'),
       ('Shiba Inu'),
       ('Bichon Frisé'),
       ('Bichon Maltais'),
       ('Lhassa Apso'),
       ('Shar Pei'),
       ('Chow Chow'),
       ('Dobermann'),
       ('Leonberg'),
       ('Terre-Neuve'),
       ('Bouvier Bernois'),
       ('Saint-Bernard'),
       ('Welsh Corgi Pembroke'),
       ('Setter Irlandais Rouge'),
       ('Braque Allemand'),
       ('Braque de Weimar'),
       ('Griffon Korthals'),
       ('Braque Français'),                    -- Type Gascogne et Pyrénées
       ('Barbet'),
       ('Coton de Tuléar'),
       ('Spitz Allemand'),                     -- Regroupe Nain (Pomeranian), Petit, Moyen, Grand, Loup
       ('Pékinois'),
       ('Carlin'),
       ('Bull Terrier'),
       ('Fox Terrier'),                        -- Poil Lisse et Poil Dur
       ('Scottish Terrier'),
       ('Schnauzer'),                          -- Nain, Moyen, Géant
       ('Airedale Terrier'),
       ('Berger des Pyrénées'),                -- Face Rase et Poil Long
       ('Colley à poil long'),                 -- Collie
       ('Whippet'),
       ('Lévrier Afghan'),
       ('Greyhound'),                          -- Lévrier Anglais
       ('Borzoi'),                             -- Lévrier Russe
       ('Basenji'),
       ('Rhodesian Ridgeback'),
       ('Dogue de Bordeaux'),
       ('Bullmastiff'),
       ('Mastiff'),                            -- Old English Mastiff
       ('Pinscher Nain'),
       ('Chien d''eau Portugais'),
       ('Lagotto Romagnolo'),
       ('Berger Picard'),
       ('Beauceron'),                          -- Berger de Beauce
       ('Berger Hollandais'),
       ('Komondor'),
       ('Kuvasz'),
       ('Mâtin des Pyrénées'),                 -- Chien de Montagne des Pyrénées
       ('Mâtin Espagnol'),
       ('Dogue du Tibet'),
       ('Fila Brasileiro'),
       ('Akita Américain'),
       ('Malamute de l''Alaska'),
       ('Groenlandais'),                       -- Chien du Groenland
       ('Chien Finnois de Laponie'),
       ('Spitz Finlandais'),
       ('Spitz des Visigoths'),                -- Vallhund Suédois
       ('Chien Norvégien de Macareux'),        -- Lundehund
       ('Elkhound Norvégien'),                 -- Chien d'élan Norvégien
       ('Eurasier'),
       ('Hovawart'),
       ('Landseer'),
       ('Retriever à poil plat'),              -- Flat-Coated Retriever
       ('Retriever de la Baie de Chesapeake'), -- Chesapeake Bay Retriever
       ('Retriever à poil bouclé'),            -- Curly-Coated Retriever
       ('Retriever de la Nouvelle-Écosse'),    -- Nova Scotia Duck Tolling Retriever
       ('Épagneul d''eau Irlandais'),
       ('Épagneul du Tibet'),                  -- Tibetan Spaniel
       ('Épagneul Japonais'),                  -- Chin
       ('Épagneul King Charles'),              -- différent du Cavalier
       ('Épagneul Papillon'),                  -- Continental Toy Spaniel Papillon
       ('Épagneul Phalène'),                   -- Continental Toy Spaniel Phalène
       ('Petit Chien Lion'),
       ('Griffon Belge'),
       ('Griffon Bruxellois'),
       ('Petit Brabançon'),
       ('Terrier Irlandais'),
       ('Terrier Kerry Blue'),
       ('Terrier Soft Coated Wheaten'),
       ('Terrier Tibétain'),
       ('Terrier Noir Russe'),
       ('Bedlington Terrier'),
       ('Border Terrier'),
       ('Cairn Terrier'),
       ('Dandie Dinmont Terrier'),
       ('Lakeland Terrier'),
       ('Manchester Terrier'),
       ('Norfolk Terrier'),
       ('Norwich Terrier'),
       ('Sealyham Terrier'),
       ('Skye Terrier'),
       ('Terrier Australien'),
       ('Terrier Tchèque'),                    -- Cesky Terrier
       ('Terrier Japonais'),                   -- Nihon Teria
       ('Terrier Brésilien'),
       ('Basset Artésien Normand'),
       ('Basset Bleu de Gascogne'),
       ('Basset Fauve de Bretagne'),
       ('Grand Basset Griffon Vendéen'),
       ('Petit Basset Griffon Vendéen'),
       ('Briquet Griffon Vendéen'),
       ('Chien d''Artois'),
       ('Porcelaine'),
       ('Billy'),
       ('Français Blanc et Noir'),
       ('Français Tricolore'),
       ('Français Blanc et Orange'),
       ('Grand Anglo-Français Tricolore'),
       ('Grand Anglo-Français Blanc et Noir'),
       ('Grand Anglo-Français Blanc et Orange'),
       ('Grand Bleu de Gascogne'),
       ('Grand Gascon Saintongeois'),
       ('Griffon Bleu de Gascogne'),
       ('Griffon Fauve de Bretagne'),
       ('Griffon Nivernais'),
       ('Poitevin'),
       ('Chien de Saint-Hubert'),              -- Bloodhound
       ('Sloughi'),                            -- Lévrier Arabe
       ('Azawakh'),                            -- Lévrier Touareg
       ('Galgo Espagnol'),                     -- Lévrier Espagnol
       ('Irish Wolfhound'),                    -- Lévrier Irlandais
       ('Deerhound'),                          -- Lévrier Écossais
       ('Petit Lévrier Italien'),              -- Italian Greyhound
       ('Saluki');-- Lévrier Persan

INSERT INTO dog (name, birth_date, gender, chip_number, user_id)
VALUES ('Rex', '2025-02-15', 'MALE', 'CHIP123456', 3),
       ('Nala', '2017-08-22', 'FEMALE', 'CHIP789012', 3),
       ('Oscar', '2021-02-10', 'STERILIZED_MALE', 'CHIP345678', 4),
       ('Lola', '2018-11-30', 'FEMALE', 'CHIP901234', 4),
       ('Filou', '2022-01-05', 'MALE', 'CHIP567890', 5),
       ('Mia', '2020-07-18', 'STERILIZED_FEMALE', 'CHIP234567', 5);

INSERT INTO dog_breed (dog_id, breed_id)
VALUES (1, 1),  -- Rex est un Labrador Retriever
       (2, 3),  -- Nala est un Golden Retriever
       (3, 2),  -- Oscar est un Berger Allemand
       (3, 5),  -- Oscar est aussi en partie Bulldog (croisé)
       (4, 6),  -- Lola est un Caniche
       (5, 8),  -- Filou est un Rottweiler
       (6, 10), -- Mia est un Boxer
       (6, 16); -- Mia est aussi en partie Chihuahua (croisé)

INSERT INTO dog_weight (measurement_date, weight_value, unit, dog_id)
VALUES ('2023-01-10', 25.55, 'KILOGRAM', 1), -- Poids de Rex le 10 Jan 2023
       ('2023-03-15', 26.25, 'KILOGRAM', 1), -- Poids de Rex le 15 Mar 2023
       ('2023-06-20', 26.85, 'KILOGRAM', 1), -- Poids de Rex le 20 Juin 2023
       ('2023-09-25', 27.15, 'KILOGRAM', 1), -- Poids de Rex le 25 Sep 2023
       ('2023-10-15', 27.30, 'KILOGRAM', 1), -- Poids de Rex le 15 Oct 2023
       ('2023-11-05', 27.45, 'KILOGRAM', 1), -- Poids de Rex le 5 Nov 2023
       ('2023-11-28', 27.65, 'KILOGRAM', 1), -- Poids de Rex le 28 Nov 2023
       ('2023-12-20', 27.90, 'KILOGRAM', 1), -- Poids de Rex le 20 Déc 2023
       ('2024-01-10', 28.15, 'KILOGRAM', 1), -- Poids de Rex le 10 Jan 2024
       ('2024-01-30', 28.25, 'KILOGRAM', 1), -- Poids de Rex le 30 Jan 2024
       ('2024-02-15', 28.40, 'KILOGRAM', 1), -- Poids de Rex le 15 Fév 2024
       ('2024-03-01', 28.55, 'KILOGRAM', 1), -- Poids de Rex le 1 Mar 2024
       ('2024-03-20', 28.70, 'KILOGRAM', 1), -- Poids de Rex le 20 Mar 2024
       ('2024-04-05', 28.85, 'KILOGRAM', 1), -- Poids de Rex le 5 Avr 2024
       ('2024-04-22', 29.00, 'KILOGRAM', 1), -- Poids de Rex le 22 Avr 2024
       ('2024-05-08', 29.15, 'KILOGRAM', 1), -- Poids de Rex le 8 Mai 2024
       ('2024-05-25', 29.25, 'KILOGRAM', 1), -- Poids de Rex le 25 Mai 2024
       ('2024-06-10', 29.40, 'KILOGRAM', 1), -- Poids de Rex le 10 Juin 2024
       ('2024-06-28', 29.55, 'KILOGRAM', 1), -- Poids de Rex le 28 Juin 2024
       ('2023-01-12', 22.30, 'KILOGRAM', 2), -- Poids de Nala le 12 Jan 2023
       ('2023-05-18', 22.80, 'KILOGRAM', 2), -- Poids de Nala le 18 Mai 2023
       ('2023-09-22', 23.10, 'KILOGRAM', 2), -- Poids de Nala le 22 Sep 2023
       ('2023-02-05', 30.55, 'KILOGRAM', 3), -- Poids d'Oscar le 5 Fév 2023
       ('2023-08-15', 32.00, 'KILOGRAM', 3), -- Poids d'Oscar le 15 Août 2023
       ('2023-03-10', 8.25, 'KILOGRAM', 4),  -- Poids de Lola le 10 Mar 2023
       ('2023-07-20', 8.50, 'KILOGRAM', 4),  -- Poids de Lola le 20 Juil 2023
       ('2023-11-05', 8.75, 'KILOGRAM', 4),  -- Poids de Lola le 5 Nov 2023
       ('2023-04-15', 40.25, 'KILOGRAM', 5), -- Poids de Filou le 15 Avr 2023
       ('2023-10-25', 41.55, 'KILOGRAM', 5), -- Poids de Filou le 25 Oct 2023
       ('2023-01-30', 20.50, 'KILOGRAM', 6), -- Poids de Mia le 30 Jan 2023
       ('2023-06-10', 21.20, 'KILOGRAM', 6), -- Poids de Mia le 10 Juin 2023
       ('2023-12-05', 21.80, 'KILOGRAM', 6);
-- Poids de Mia le 5 Déc 2023

-- Insert common dog vaccines with their renewal periods
INSERT INTO vaccine (vaccine_name, renew_delay)
VALUES ('Rage', 36),                        -- Vaccin contre la rage, renouvelé tous les 3 ans
       ('CHPPIL (Carré, Hépatite, Parvovirose, Parainfluenza, Leptospirose)',
        12),                                -- Vaccin CHPPIL, renouvelé annuellement
       ('Bordetella (Toux du Chenil)', 6), -- Vaccin Bordetella, renouvelé tous les 6 mois
       ('Leptospirose', 12),                -- Vaccin contre la leptospirose, renouvelé annuellement
       ('Maladie de Lyme', 12),             -- Vaccin contre la maladie de Lyme, renouvelé annuellement
       ('Grippe Canine', 12),               -- Vaccin contre la grippe canine, renouvelé annuellement
       ('Coronavirus', 12);
-- Vaccin contre le coronavirus, renouvelé annuellement

-- Insert vaccination records for dogs
INSERT INTO vaccination (vaccination_date, batch_number, veterinarian, dog_id, vaccine_id)
VALUES ('2023-01-15', 'RAB123456', 'Dr. Dupont', 1, 1),   -- Vaccination contre la rage de Rex
       ('2023-02-10', 'DHPP789012', 'Dr. Martin', 1, 2),  -- Vaccination CHPPIL de Rex
       ('2023-03-05', 'BOR345678', 'Dr. Dupont', 1, 3),   -- Vaccination Bordetella de Rex
       ('2023-01-20', 'RAB567890', 'Dr. Bernard', 2, 1),  -- Vaccination contre la rage de Nala
       ('2023-02-15', 'DHPP123456', 'Dr. Bernard', 2, 2), -- Vaccination CHPPIL de Nala
       ('2023-02-01', 'RAB234567', 'Dr. Petit', 3, 1),    -- Vaccination contre la rage d'Oscar
       ('2023-03-10', 'DHPP345678', 'Dr. Petit', 3, 2),   -- Vaccination CHPPIL d'Oscar
       ('2023-04-05', 'BOR456789', 'Dr. Petit', 3, 3),    -- Vaccination Bordetella d'Oscar
       ('2023-01-25', 'RAB345678', 'Dr. Moreau', 4, 1),   -- Vaccination contre la rage de Lola
       ('2023-02-20', 'DHPP456789', 'Dr. Moreau', 4, 2),  -- Vaccination CHPPIL de Lola
       ('2023-03-15', 'LEP567890', 'Dr. Moreau', 4, 4),   -- Vaccination contre la leptospirose de Lola
       ('2023-02-05', 'RAB456789', 'Dr. Leroy', 5, 1),    -- Vaccination contre la rage de Filou
       ('2023-03-01', 'DHPP567890', 'Dr. Leroy', 5, 2),   -- Vaccination CHPPIL de Filou
       ('2023-04-10', 'LYM678901', 'Dr. Leroy', 5, 5),    -- Vaccination contre la maladie de Lyme de Filou
       ('2023-01-30', 'RAB678901', 'Dr. Bernard', 6, 1),  -- Vaccination contre la rage de Mia
       ('2023-02-25', 'DHPP678901', 'Dr. Bernard', 6, 2), -- Vaccination CHPPIL de Mia
       ('2023-03-20', 'BOR789012', 'Dr. Bernard', 6, 3);
-- Vaccination Bordetella de Mia

-- Insert medication treatments for dogs
INSERT INTO medication_treatment (medication_name, dosage, frequency, start_date, end_date, treatment_reason, dog_id)
VALUES
    -- Traitements de Rex
    ('Amoxicilline', '250mg', '08:00:00', '2023-02-15', '2023-02-25', 'Infection de l''oreille', 1),
    ('Prednisolone', '10mg', '12:00:00', '2023-05-10', '2023-05-20', 'Réaction allergique', 1),
    ('Préventif contre les vers du cœur', '1 comprimé', '09:00:00', '2023-06-01', NULL,
     'Prévention mensuelle contre les vers du cœur', 1),

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
    ('2025-04-20 09:30:00', 'CONFIRMED', 1, 1), -- Rex inscrit à Chiots - Éveil et Socialisation
    ('2025-05-10 14:15:00', 'CONFIRMED', 1, 3), -- Rex inscrit à Découverte de l'agilité
    ('2025-06-01 10:45:00', 'PENDING', 1, 4),   -- Rex en attente pour Trucs et astuces avancés

    -- Inscriptions de Nala (dog_id 2)
    ('2025-05-01 11:00:00', 'CONFIRMED', 2, 2), -- Nala inscrite à Obéissance du quotidien
    ('2025-05-15 16:30:00', 'CANCELLED', 2, 3), -- Nala a annulé Découverte de l'agilité
    ('2025-06-20 09:15:00', 'CONFIRMED', 2, 5), -- Nala inscrite à Formation chien visiteur

    -- Inscriptions d'Oscar (dog_id 3)
    ('2025-05-05 10:00:00', 'CONFIRMED', 3, 2), -- Oscar inscrit à Obéissance du quotidien
    ('2025-06-05 15:45:00', 'CONFIRMED', 3, 4), -- Oscar inscrit à Trucs et astuces avancés

    -- Inscriptions de Lola (dog_id 4)
    ('2025-04-25 13:30:00', 'CONFIRMED', 4, 1), -- Lola inscrite à Chiots - Éveil et Socialisation
    ('2025-06-25 11:15:00', 'PENDING', 4, 5),   -- Lola en attente pour Formation chien visiteur

    -- Inscriptions de Filou (dog_id 5)
    ('2025-05-20 09:45:00', 'CONFIRMED', 5, 3), -- Filou inscrit à Découverte de l'agilité
    ('2025-07-10 14:00:00', 'CONFIRMED', 5, 6), -- Filou inscrit à Bien-être des seniors

    -- Inscriptions de Mia (dog_id 6)
    ('2025-05-10 10:30:00', 'CONFIRMED', 6, 2), -- Mia inscrite à Obéissance du quotidien
    ('2025-06-01 13:45:00', 'CANCELLED', 6, 4), -- Mia a annulé Trucs et astuces avancés
    ('2025-07-15 11:30:00', 'CONFIRMED', 6, 6), -- Mia inscrite à Bien-être des seniors

    -- Inscriptions pour les cours à venir (2025-2026)

    -- Inscriptions de Rex (dog_id 1) aux nouveaux cours
    ('2025-05-18 10:30:00', 'CONFIRMED', 1, 2), -- Rex inscrit à Obéissance du quotidien
    ('2025-06-25 09:45:00', 'CONFIRMED', 1, 5), -- Rex inscrit à Formation chien visiteur
    ('2025-07-25 10:30:00', 'CONFIRMED', 1, 7), -- Rex inscrit à Chiots d'été
    ('2025-08-30 14:45:00', 'CONFIRMED', 1, 9), -- Rex inscrit à Parcours d'agilité
    ('2025-09-25 09:15:00', 'PENDING', 1, 10),  -- Rex en attente pour Préparation concours
    ('2025-10-20 11:30:00', 'CONFIRMED', 1, 11), -- Rex inscrit à Jeux d'intelligence canine
    ('2025-11-25 15:30:00', 'CONFIRMED', 1, 12), -- Rex inscrit à Rappel en toutes situations
    ('2026-01-05 11:00:00', 'CONFIRMED', 1, 13), -- Rex inscrit à Balades éducatives
    ('2026-02-10 09:30:00', 'PENDING', 1, 14),  -- Rex en attente pour Initiation au pistage
    ('2026-03-25 14:00:00', 'CONFIRMED', 1, 16), -- Rex inscrit à Sports canins ludiques

    -- Inscriptions de Nala (dog_id 2) aux nouveaux cours
    ('2025-08-15 15:00:00', 'CONFIRMED', 2, 8), -- Nala inscrite à Perfectionnement obéissance
    ('2025-10-25 13:45:00', 'CONFIRMED', 2, 11), -- Nala inscrite à Jeux d'intelligence canine
    ('2026-01-05 10:00:00', 'CONFIRMED', 2, 13), -- Nala inscrite à Balades éducatives

    -- Inscriptions d'Oscar (dog_id 3) aux nouveaux cours
    ('2025-09-01 09:30:00', 'CONFIRMED', 3, 9), -- Oscar inscrit à Parcours d'agilité
    ('2025-11-20 14:15:00', 'PENDING', 3, 12),  -- Oscar en attente pour Rappel en toutes situations
    ('2026-02-10 11:00:00', 'CONFIRMED', 3, 14), -- Oscar inscrit à Initiation au pistage

    -- Inscriptions de Lola (dog_id 4) aux nouveaux cours
    ('2025-07-30 11:45:00', 'CONFIRMED', 4, 7), -- Lola inscrite à Chiots d'été
    ('2025-10-30 10:00:00', 'CONFIRMED', 4, 11), -- Lola inscrite à Jeux d'intelligence canine
    ('2026-03-01 09:30:00', 'CONFIRMED', 4, 15), -- Lola inscrite à Chiots d'hiver

    -- Inscriptions de Filou (dog_id 5) aux nouveaux cours
    ('2025-08-20 16:30:00', 'CONFIRMED', 5, 8), -- Filou inscrit à Perfectionnement obéissance
    ('2025-10-01 13:00:00', 'CONFIRMED', 5, 10), -- Filou inscrit à Préparation concours
    ('2026-03-25 14:30:00', 'CONFIRMED', 5, 16), -- Filou inscrit à Sports canins ludiques

    -- Inscriptions de Mia (dog_id 6) aux nouveaux cours
    ('2025-09-10 10:15:00', 'CONFIRMED', 6, 9), -- Mia inscrite à Parcours d'agilité
    ('2025-11-30 15:30:00', 'CONFIRMED', 6, 12), -- Mia inscrite à Rappel en toutes situations
    ('2026-01-25 13:00:00', 'CONFIRMED', 6, 13); -- Mia inscrite à Balades éducatives


INSERT INTO veterinary_visit (visit_date, diagnosis, reason_for_visit, treatment, veterinarian, dog_id)
VALUES
    -- Visites de Rex
    ('2023-02-10', 'Otite externe à l''oreille droite', 'Grattement d''oreille et secouement de tête',
     'Prescription d''Amoxicilline pour 10 jours et solution de nettoyage auriculaire', 'Dr. Dupont', 1),
    ('2023-05-05', 'Réaction allergique à une piqûre d''insecte', 'Gonflement du museau et urticaire',
     'Administration d''injection d''antihistaminique, prescription de Prednisolone', 'Dr. Martin', 1),
    ('2023-09-15', 'Bilan annuel, tous les systèmes normaux', 'Examen de routine',
     'Mise à jour des vaccinations, test de dirofilariose négatif', 'Dr. Dupont', 1),
    ('2023-04-12', 'Coupure à la patte avant droite', 'Boiterie et saignement léger',
     'Nettoyage de la plaie, points de suture et prescription d''antibiotiques', 'Dr. Martin', 1),
    ('2023-06-28', 'Problèmes dentaires', 'Mauvaise haleine et difficulté à manger',
     'Détartrage complet et extraction d''une dent de lait persistante', 'Dr. Leroy', 1),
    ('2023-08-03', 'Infection urinaire', 'Mictions fréquentes et inconfort',
     'Prescription d''antibiotiques pour 7 jours et recommandation d''augmentation de l''hydratation', 'Dr. Dupont', 1),
    ('2023-10-20', 'Entorse légère à la patte arrière gauche', 'Boiterie après une activité intense',
     'Repos recommandé pendant 10 jours et anti-inflammatoires', 'Dr. Bernard', 1),
    ('2023-11-15', 'Dermatite allergique', 'Démangeaisons et rougeurs sur le ventre',
     'Prescription de crème topique et changement d''alimentation hypoallergénique', 'Dr. Martin', 1),
    ('2023-12-05', 'Contrôle vaccinal et examen général', 'Visite de routine',
     'Mise à jour des vaccins et vérification de la puce électronique', 'Dr. Dupont', 1),

    -- Visites de Nala
    ('2023-03-15', 'Arthrose des articulations de la hanche', 'Difficulté à se lever et boiterie',
     'Prescription de Carprofène pour la gestion de la douleur, recommandation de gestion du poids', 'Dr. Bernard', 2),
    ('2023-07-10', 'Diabète sucré', 'Augmentation de la soif, miction et perte de poids',
     'Début de l''insulinothérapie, changements alimentaires recommandés', 'Dr. Bernard', 2),
    ('2023-11-20', 'Suivi du diabète', 'Suivi pour la gestion du diabète',
     'Ajustement de la posologie d''insuline, amélioration des niveaux de glucose sanguin', 'Dr. Bernard', 2),

    -- Visites d'Oscar
    ('2023-01-05', 'Gastro-entérite', 'Vomissements et diarrhée depuis 2 jours',
     'Prescription de Métronidazole, recommandation d''un régime fade pendant 5 jours', 'Dr. Petit', 3),
    ('2023-04-01', 'Dermatite atopique', 'Démangeaisons persistantes et rougeurs sur la peau',
     'Début d''Apoquel, recommandation de shampooing hypoallergénique', 'Dr. Petit', 3),
    ('2023-08-05', 'Conjonctivite', 'Yeux rouges, gonflés avec écoulement',
     'Prescription de gouttes antibiotiques pour les yeux, collier élisabéthain pour éviter les grattements',
     'Dr. Moreau', 3),

    -- Visites de Lola
    ('2023-02-01', 'Infection bactérienne de la peau', 'Points chauds et perte de poils sur le dos',
     'Prescription de Céfalexine, bains médicamenteux deux fois par semaine', 'Dr. Moreau', 4),
    ('2023-05-10', 'Déchirure du ligament croisé', 'Boiterie soudaine de la patte arrière droite',
     'Réparation chirurgicale effectuée, prescription de Tramadol pour la douleur', 'Dr. Leroy', 4),
    ('2023-06-01', 'Suivi post-chirurgical', 'Surveillance de la cicatrisation du site chirurgical',
     'Retrait des sutures, exercices de rééducation recommandés', 'Dr. Leroy', 4),

    -- Visites de Filou
    ('2023-03-05', 'Infection fongique de la peau', 'Lésions circulaires sur la peau avec perte de poils',
     'Prescription de Fluconazole, crème antifongique topique', 'Dr. Leroy', 5),
    ('2023-06-15', 'Épilepsie idiopathique', 'Deux épisodes de crises signalés',
     'Début de Phénobarbital, analyses sanguines pour surveiller la fonction hépatique', 'Dr. Dupont', 5),
    ('2023-09-01', 'Insuffisance cardiaque congestive', 'Toux et intolérance à l''exercice',
     'Prescription de Furosémide, recommandation d''activité restreinte', 'Dr. Martin', 5),

    -- Visites de Mia
    ('2023-01-01', 'Maladie de Lyme', 'Boiterie de plusieurs pattes, fièvre',
     'Prescription de Doxycycline, repos recommandé', 'Dr. Bernard', 6),
    ('2023-04-05', 'Hypothyroïdie', 'Prise de poids, léthargie, amincissement des poils',
     'Début de médicament pour la thyroïde, suivi dans 1 mois', 'Dr. Bernard', 6),
    ('2023-07-10', 'Allergies saisonnières', 'Démangeaisons, éternuements et yeux larmoyants',
     'Prescription d''antihistaminique, recommandation de temps limité à l''extérieur pendant les jours à forte concentration de pollen',
     'Dr. Moreau', 6);


############################ VIEWS ############################

create view OwnersV as
select o.is_active         AS is_active,
       o.registration_date AS registration_date,
       o.user_id           AS user_id,
       o.address           AS address,
       u.email             AS email,
       u.firstname         AS firstname,
       u.lastname          AS lastname,
       u.phone             AS phone
from owner o left join CaniCampusConnect.user u on u.user_id = o.user_id;


CREATE VIEW CoachesV AS
SELECT c.acaced_number     AS acaced_number,
       c.is_active         AS is_active,
       c.registration_date AS registration_date,
       c.user_id           AS user_id,
       u.email             AS email,
       u.firstname         AS firstname,
       u.lastname          AS lastname,
       u.phone             AS phone
FROM coach c
         LEFT JOIN CaniCampusConnect.user u ON u.user_id = c.user_id;

CREATE VIEW ClubOwnersV AS
SELECT co.user_id  AS user_id,
       u.email     AS email,
       u.firstname AS firstname,
       u.lastname  AS lastname,
       u.phone     AS phone
FROM club_owner co
         LEFT JOIN CaniCampusConnect.user u ON u.user_id = co.user_id;

-- Create view for dogs with their breeds
CREATE VIEW DogsWithBreedsV AS
SELECT d.id          AS dog_id,
       d.name        AS dog_name,
       d.birth_date  AS birth_date,
       d.gender      AS gender,
       d.chip_number AS chip_number,
       d.user_id     AS owner_id,
       o.firstname   AS owner_firstname,
       o.lastname    AS owner_lastname,
       b.id          AS breed_id,
       b.name        AS breed_name
FROM dog d
         JOIN dog_breed db ON d.id = db.dog_id
         JOIN breed b ON db.breed_id = b.id
         JOIN owner ow ON d.user_id = ow.user_id
         JOIN user o ON ow.user_id = o.user_id;

-- Create view for dogs with their weights
CREATE VIEW DogsWithWeightsV AS
SELECT d.id                AS dog_id,
       d.name              AS dog_name,
       d.birth_date        AS birth_date,
       d.gender            AS gender,
       d.chip_number       AS chip_number,
       d.user_id           AS owner_id,
       o.firstname         AS owner_firstname,
       o.lastname          AS owner_lastname,
       dw.id               AS weight_id,
       dw.measurement_date AS measurement_date,
       dw.weight_value     AS weight_value,
       dw.unit             AS weight_unit
FROM dog d
         JOIN dog_weight dw ON d.id = dw.dog_id
         JOIN owner ow ON d.user_id = ow.user_id
         JOIN user o ON ow.user_id = o.user_id;

-- Create view for dogs with complete information (breeds and latest weight)
CREATE VIEW DogsCompleteV AS
SELECT d.id                                         AS dog_id,
       d.name                                       AS dog_name,
       d.birth_date                                 AS birth_date,
       d.gender                                     AS gender,
       d.chip_number                                AS chip_number,
       d.user_id                                    AS owner_id,
       o.firstname                                  AS owner_firstname,
       o.lastname                                   AS owner_lastname,
       GROUP_CONCAT(DISTINCT b.name SEPARATOR ', ') AS breeds,
       lw.weight_value                              AS latest_weight_value,
       lw.unit                                      AS latest_weight_unit,
       lw.measurement_date                          AS latest_weight_date
FROM dog d
         JOIN owner ow ON d.user_id = ow.user_id
         JOIN user o ON ow.user_id = o.user_id
         LEFT JOIN dog_breed db ON d.id = db.dog_id
         LEFT JOIN breed b ON db.breed_id = b.id
         LEFT JOIN (SELECT dw.dog_id, dw.weight_value, dw.unit, dw.measurement_date
                    FROM dog_weight dw
                             INNER JOIN (SELECT dog_id, MAX(measurement_date) as max_date
                                         FROM dog_weight
                                         GROUP BY dog_id) latest
                                        ON dw.dog_id = latest.dog_id AND dw.measurement_date = latest.max_date) lw
                   ON d.id = lw.dog_id
GROUP BY d.id, d.name, d.birth_date, d.gender, d.chip_number, d.user_id,
         o.firstname, o.lastname, lw.weight_value, lw.unit, lw.measurement_date;

-- Create view for dogs with health records (vaccinations, veterinary visits, medication treatments)
CREATE VIEW DogsHealthRecordsV AS
SELECT d.id            AS dog_id,
       d.name          AS dog_name,
       d.birth_date    AS birth_date,
       d.gender        AS gender,
       d.chip_number   AS chip_number,
       d.user_id       AS owner_id,
       o.firstname     AS owner_firstname,
       o.lastname      AS owner_lastname,

       -- Vaccination information
       v.vaccination_id,
       v.vaccination_date,
       v.batch_number,
       v.veterinarian  AS vaccination_veterinarian,
       vac.vaccine_name,

       -- Veterinary visit information
       vv.visit_id,
       vv.visit_date,
       vv.diagnosis,
       vv.reason_for_visit,
       vv.treatment,
       vv.veterinarian AS visit_veterinarian,

       -- Medication treatment information
       mt.treatment_id,
       mt.medication_name,
       mt.dosage,
       mt.frequency,
       mt.start_date,
       mt.end_date,
       mt.treatment_reason
FROM dog d
         JOIN owner ow ON d.user_id = ow.user_id
         JOIN user o ON ow.user_id = o.user_id
         LEFT JOIN vaccination v ON d.id = v.dog_id
         LEFT JOIN vaccine vac ON v.vaccine_id = vac.vaccine_id
         LEFT JOIN veterinary_visit vv ON d.id = vv.dog_id
         LEFT JOIN medication_treatment mt ON d.id = mt.dog_id;

-- Create view for dogs with their course registrations
CREATE VIEW DogsRegistrationsV AS
SELECT d.id                 AS dog_id,
       d.name               AS dog_name,
       d.birth_date         AS birth_date,
       d.gender             AS gender,
       d.chip_number        AS chip_number,
       d.user_id            AS owner_id,
       o.firstname          AS owner_firstname,
       o.lastname           AS owner_lastname,

       -- Registration information
       r.registration_id,
       r.registration_date,
       r.status,

       -- Course information
       c.course_id,
       c.title              AS course_title,
       c.description        AS course_description,
       c.start_datetime     AS course_start,
       c.end_datetime       AS course_end,
       c.max_capacity       AS course_max_capacity,

       -- Course type information
       ct.course_type_id,
       ct.name              AS course_type_name,
       ct.description       AS course_type_description,

       -- Coach information
       coach.user_id        AS coach_id,
       coach_user.firstname AS coach_firstname,
       coach_user.lastname  AS coach_lastname
FROM dog d
         JOIN owner ow ON d.user_id = ow.user_id
         JOIN user o ON ow.user_id = o.user_id
         LEFT JOIN registration r ON d.id = r.dog_id
         LEFT JOIN course c ON r.course_id = c.course_id
         LEFT JOIN course_type ct ON c.course_type_id = ct.course_type_id
         LEFT JOIN coach ON c.user_id = coach.user_id
         LEFT JOIN user coach_user ON coach.user_id = coach_user.user_id;
