-- Au lieu de l'ancien INSERT
INSERT INTO user (firstname, lastname, email, password, phone, email_validated, created_date, last_modified_date,
                  created_by, last_modified_by)
VALUES ('PrénomAdmin', 'NomAdmin', 'admin@admin.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq',
        '0611111111', true, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Marin', 'Tintin', 'coach@coach.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq',
        '0622222222', true, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('PrénomCoach2', 'NomCoach2', 'coach2@coach.com', '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq',
        '0633333333', true, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Propriétaire1', 'Utilisateur1', 'owner3@user.com',
        '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0677777777', true, NOW(), NOW(), 'SYSTEM',
        'SYSTEM'),
       ('Propriétaire2', 'Utilisateur2', 'owner4@user.com',
        '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0644444444', true, NOW(), NOW(), 'SYSTEM',
        'SYSTEM'),
       ('Propriétaire3', 'Utilisateur3', 'owner5@user.com',
        '$2a$10$xDl57qh0D9i6MhS3B76UE.zExRDwKcXcr8TbW3MgSgwXVJKy9uxmq', '0655555555', true, NOW(), NOW(), 'SYSTEM',
        'SYSTEM');

INSERT INTO club_owner (user_id)
VALUES (1);

INSERT INTO coach (user_id, acaced_number, is_active, registration_date)
VALUES (2, 'OEACHK90', true, '2025-04-16'),
       (3, 'PQRST123', true, '2025-04-16');

INSERT INTO owner (user_id, is_active, registration_date)
VALUES (4, false, '2023-03-10'),
       (5, true, '2023-04-20'),
       (6, true, '2023-05-05');

INSERT INTO club (name, address, user_id)
VALUES ('Les moustaches Messines', '123 Rue des Chiens, Paris', 1);

INSERT INTO age_range (min_age, max_age, name)
VALUES (0, 12, 'Chiot'),  -- 0-12 mois (chiots)
       (13, 36, 'Jeune chien'), -- 13-36 mois (jeune chien)
       (37, 84, 'Adulte'), -- 37-84 mois (adulte)
       (85, 999, 'Senior'); -- 85+ mois (senior)

INSERT INTO course_type (name, description, age_range_id)
VALUES ('Socialisation des Chiots', 'Socialisation de base et introduction à l''éducation pour chiots', 1),
       ('Obéissance de Base', 'Commandes fondamentales et éducation comportementale pour tous les chiens', 2),
       ('Obéissance Avancée', 'Commandes complexes et entraînement sans laisse pour chiens bien éduqués', 3),
       ('Entraînement d''Agilité', 'Parcours d''obstacles pour chiens sportifs', 2),
       ('Formation de Chien Thérapeutique',
        'Formation pour chiens destinés à devenir des animaux thérapeutiques certifiés', 3),
       ('Bien-être des Chiens Seniors', 'Exercices doux et stimulation mentale pour chiens âgés', 4);

INSERT INTO course (title, description, start_datetime, end_datetime, max_capacity, user_id, club_id, course_type_id,
                    created_date, last_modified_date, created_by, last_modified_by)
VALUES
    -- Age Range 1 (Puppies) - 5 courses before May 22, 2025
    ('Chiots - Éveil et Socialisation',
     'On va apprendre à votre chiot les bases de l''éducation et le faire jouer avec d''autres pour une bonne socialisation',
     '2025-05-15 10:00:00', '2025-05-15 11:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Premiers pas',
     'Cours d''introduction pour les tout jeunes chiots, axé sur la socialisation de base et la découverte de l''environnement',
     '2025-04-25 09:00:00', '2025-04-25 10:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Jeux d''éveil', 'Activités ludiques pour stimuler le développement cognitif et social de votre chiot',
     '2025-05-01 14:00:00', '2025-05-01 15:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Découverte sensorielle',
     'Exposer votre chiot à différentes textures, sons et environnements pour une meilleure adaptation',
     '2025-05-08 10:00:00', '2025-05-08 11:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Bases de l''éducation', 'Premiers apprentissages pour une bonne cohabitation avec votre chiot',
     '2025-05-18 09:00:00', '2025-05-18 10:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 1 (Puppies) - 5 courses after May 22, 2025
    ('Chiots - Communication canine',
     'Apprendre à comprendre le langage corporel de votre chiot et à communiquer efficacement avec lui',
     '2025-06-15 10:00:00', '2025-06-15 11:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Manipulation et soins',
     'Habituer votre chiot à être manipulé pour les soins quotidiens et les visites vétérinaires',
     '2025-07-05 14:00:00', '2025-07-05 15:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Socialisation avancée',
     'Exposition à différents environnements, personnes et autres animaux pour une socialisation complète',
     '2025-08-10 10:00:00', '2025-08-10 11:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Préparation à l''adolescence',
     'Préparer les propriétaires aux changements comportementaux liés à l''adolescence canine',
     '2025-10-15 09:00:00', '2025-10-15 10:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Chiots - Jeux et apprentissage',
     'Utilisation de jeux pour développer les capacités cognitives et la socialisation des chiots',
     '2026-01-20 14:00:00', '2026-01-20 15:30:00', 8, 2, 1, 1, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 2 (Young dogs) - 5 courses before May 22, 2025
    ('Obéissance de base - Niveau 1', 'Apprentissage des commandes fondamentales pour jeunes chiens',
     '2025-04-20 14:00:00', '2025-04-20 16:00:00', 10, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Obéissance de base - Niveau 2', 'Renforcement des commandes de base et introduction à de nouvelles commandes',
     '2025-04-27 14:00:00', '2025-04-27 16:00:00', 10, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Marche en laisse sans traction', 'Techniques pour apprendre à votre jeune chien à marcher correctement en laisse',
     '2025-05-04 09:00:00', '2025-05-04 11:00:00', 8, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Rappel efficace', 'Méthodes pour obtenir un rappel fiable avec votre jeune chien',
     '2025-05-11 14:00:00', '2025-05-11 16:00:00', 8, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Obéissance du quotidien',
     'Pour les chiens qui connaissent déjà quelques bases, on va travailler sur les situations de tous les jours',
     '2025-05-18 14:00:00', '2025-05-18 16:00:00', 10, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 2 (Young dogs) - 5 courses after May 22, 2025
    ('Découverte de l''agilité',
     'Venez vous amuser avec votre chien sur différents obstacles, parfait pour renforcer votre complicité',
     '2025-06-05 09:00:00', '2025-06-05 11:00:00', 6, 3, 1, 4, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Parcours d''agilité',
     'On va travailler sur des parcours plus complexes pour les chiens qui ont déjà découvert l''agilité',
     '2025-07-20 09:00:00', '2025-07-20 11:30:00', 5, 3, 1, 4, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Jeux d''intelligence canine',
     'Des activités ludiques pour stimuler le cerveau de votre chien et renforcer votre lien',
     '2025-08-28 10:00:00', '2025-08-28 12:00:00', 8, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Rappel en toutes situations',
     'Fini les angoisses au parc! Apprenez à avoir un rappel fiable même avec des distractions',
     '2025-10-10 14:00:00', '2025-10-10 16:00:00', 6, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Balades éducatives', 'On sort en groupe pour mettre en pratique les apprentissages dans un environnement réel',
     '2026-01-18 13:30:00', '2026-01-18 15:30:00', 8, 3, 1, 2, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 3 (Adults) - 5 courses before May 22, 2025
    ('Obéissance avancée - Niveau 1', 'Perfectionnement des commandes et travail sans laisse pour chiens adultes',
     '2025-04-18 13:00:00', '2025-04-18 16:00:00', 8, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Obéissance avancée - Niveau 2', 'Travail à distance et avec distractions pour chiens adultes bien éduqués',
     '2025-04-25 13:00:00', '2025-04-25 16:00:00', 8, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Préparation aux sports canins', 'Introduction aux différentes disciplines sportives canines pour chiens adultes',
     '2025-05-02 10:00:00', '2025-05-02 13:00:00', 6, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Techniques de concentration', 'Améliorer la concentration de votre chien adulte dans différents environnements',
     '2025-05-09 14:00:00', '2025-05-09 17:00:00', 6, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Formation chien visiteur - Introduction',
     'Première approche pour les chiens adultes qui pourraient devenir chiens visiteurs',
     '2025-05-16 10:00:00', '2025-05-16 12:00:00', 5, 2, 1, 5, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 3 (Adults) - 5 courses after May 22, 2025
    ('Trucs et astuces avancés',
     'Une journée pour apprendre des techniques qui impressionneront vos amis et faciliteront votre vie',
     '2025-06-18 13:00:00', '2025-06-18 17:00:00', 8, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Formation chien visiteur - Avancé',
     'Préparation pour les chiens qui aiment le contact et pourraient rendre visite aux personnes isolées',
     '2025-07-10 10:00:00', '2025-07-10 12:00:00', 5, 2, 1, 5, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Préparation concours', 'Vous voulez participer à des compétitions? On vous donne toutes les clés pour réussir',
     '2025-09-15 13:00:00', '2025-09-15 16:00:00', 4, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Perfectionnement obéissance', 'Pour les chiens qui ont déjà de bonnes bases, on va aller plus loin ensemble',
     '2025-11-05 14:00:00', '2025-11-05 17:00:00', 6, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Initiation au pistage',
     'Découvrez comment utiliser le flair incroyable de votre chien pour des activités stimulantes',
     '2026-02-22 10:00:00', '2026-02-22 12:00:00', 5, 2, 1, 3, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 4 (Seniors) - 5 courses before May 22, 2025
    ('Bien-être des seniors - Introduction', 'Premiers pas pour adapter les activités à votre chien senior',
     '2025-04-15 11:00:00', '2025-04-15 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Exercices doux pour seniors', 'Activités physiques adaptées pour maintenir la mobilité de votre chien âgé',
     '2025-04-22 11:00:00', '2025-04-22 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Stimulation mentale pour seniors', 'Jeux et activités pour garder l''esprit de votre chien senior actif',
     '2025-05-01 11:00:00', '2025-05-01 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Nutrition et soins des seniors', 'Conseils pour adapter l''alimentation et les soins aux besoins des chiens âgés',
     '2025-05-08 11:00:00', '2025-05-08 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Confort et qualité de vie', 'Aménagements et astuces pour améliorer le quotidien de votre chien senior',
     '2025-05-15 11:00:00', '2025-05-15 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),

    -- Age Range 4 (Seniors) - 5 courses after May 22, 2025
    ('Bien-être des seniors - Avancé', 'Activités douces et stimulantes adaptées à nos compagnons plus âgés',
     '2025-07-25 11:00:00', '2025-07-25 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Massage canin pour seniors', 'Techniques de massage pour soulager les douleurs articulaires des chiens âgés',
     '2025-08-22 11:00:00', '2025-08-22 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Adaptation aux changements', 'Aider votre chien senior à s''adapter aux changements liés à l''âge',
     '2025-10-10 11:00:00', '2025-10-10 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Jeux adaptés aux seniors', 'Activités ludiques spécialement conçues pour les chiens âgés',
     '2025-12-05 11:00:00', '2025-12-05 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
    ('Promenades pour seniors', 'Comment adapter les sorties aux capacités de votre chien âgé',
     '2026-03-20 11:00:00', '2026-03-20 12:00:00', 6, 3, 1, 6, NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO breed (name, avatar_url, created_date, last_modified_date, created_by, last_modified_by)
VALUES ('Berger Australien', '/breed/1/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Golden Retriever', '/breed/2/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Staffordshire Bull Terrier', '/breed/3/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger Belge', '/breed/4/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Labrador Retriever', '/breed/5/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger Allemand', '/breed/6/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Setter Anglais', '/breed/7/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul Breton', '/breed/8/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Beagle', '/breed/9/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Cavalier King Charles Spaniel', '/breed/10/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bouledogue Français', '/breed/11/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Cocker Spaniel Anglais', '/breed/12/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Teckel', '/breed/13/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Yorkshire Terrier', '/breed/14/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Cane Corso', '/breed/15/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Husky de Sibérie', '/breed/16/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Shih Tzu', '/breed/17/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('American Staffordshire Terrier', '/breed/18/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chihuahua', '/breed/19/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Jack Russell Terrier', '/breed/20/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Border Collie', '/breed/21/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Pointer Anglais', '/breed/22/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dogue Allemand', '/breed/23/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Rottweiler', '/breed/24/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger Blanc Suisse', '/breed/25/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Samoyède', '/breed/26/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('West Highland White Terrier', '/breed/27/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Basset Hound', '/breed/28/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dalmatien', '/breed/29/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Akita Inu', '/breed/30/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Shiba Inu', '/breed/31/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bichon Frisé', '/breed/32/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bichon Maltais', '/breed/33/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Lhassa Apso', '/breed/34/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Shar Pei', '/breed/35/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chow Chow', '/breed/36/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dobermann', '/breed/37/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Leonberg', '/breed/38/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terre-Neuve', '/breed/39/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bouvier Bernois', '/breed/40/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Saint-Bernard', '/breed/41/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Welsh Corgi Pembroke', '/breed/42/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Setter Irlandais Rouge', '/breed/43/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Braque Allemand', '/breed/44/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Braque de Weimar', '/breed/45/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Korthals', '/breed/46/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Braque Français', '/breed/47/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Barbet', '/breed/48/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Coton de Tuléar', '/breed/49/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Spitz Allemand', '/breed/50/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Pékinois', '/breed/51/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Carlin', '/breed/52/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bull Terrier', '/breed/53/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Fox Terrier', '/breed/54/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Scottish Terrier', '/breed/55/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Schnauzer', '/breed/56/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Airedale Terrier', '/breed/57/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger des Pyrénées', '/breed/58/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Colley à poil long', '/breed/59/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Whippet', '/breed/60/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Lévrier Afghan', '/breed/61/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Greyhound', '/breed/62/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Borzoi', '/breed/63/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Basenji', '/breed/64/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Rhodesian Ridgeback', '/breed/65/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dogue de Bordeaux', '/breed/66/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bullmastiff', '/breed/67/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Mastiff', '/breed/68/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Pinscher Nain', '/breed/69/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chien d''eau Portugais', '/breed/70/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Lagotto Romagnolo', '/breed/71/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger Picard', '/breed/72/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Beauceron', '/breed/73/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Berger Hollandais', '/breed/74/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Komondor', '/breed/75/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Kuvasz', '/breed/76/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Mâtin des Pyrénées', '/breed/77/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Mâtin Espagnol', '/breed/78/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dogue du Tibet', '/breed/79/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Fila Brasileiro', '/breed/80/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Akita Américain', '/breed/81/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Malamute de l''Alaska', '/breed/82/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Groenlandais', '/breed/83/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chien Finnois de Laponie', '/breed/84/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Spitz Finlandais', '/breed/85/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Spitz des Visigoths', '/breed/86/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chien Norvégien de Macareux', '/breed/87/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Elkhound Norvégien', '/breed/88/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Eurasier', '/breed/89/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Hovawart', '/breed/90/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Landseer', '/breed/91/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Retriever à poil plat', '/breed/92/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Retriever de la Baie de Chesapeake', '/breed/93/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Retriever à poil bouclé', '/breed/94/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Retriever de la Nouvelle-Écosse', '/breed/95/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul d''eau Irlandais', '/breed/96/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul du Tibet', '/breed/97/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul Japonais', '/breed/98/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul King Charles', '/breed/99/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul Papillon', '/breed/100/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Épagneul Phalène', '/breed/101/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Petit Chien Lion', '/breed/102/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Belge', '/breed/103/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Bruxellois', '/breed/104/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Petit Brabançon', '/breed/105/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Irlandais', '/breed/106/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Kerry Blue', '/breed/107/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Soft Coated Wheaten', '/breed/108/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Tibétain', '/breed/109/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Noir Russe', '/breed/110/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Bedlington Terrier', '/breed/111/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Border Terrier', '/breed/112/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Cairn Terrier', '/breed/113/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Dandie Dinmont Terrier', '/breed/114/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Lakeland Terrier', '/breed/115/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Manchester Terrier', '/breed/116/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Norfolk Terrier', '/breed/117/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Norwich Terrier', '/breed/118/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Sealyham Terrier', '/breed/119/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Skye Terrier', '/breed/120/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Australien', '/breed/121/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Tchèque', '/breed/122/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Japonais', '/breed/123/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Terrier Brésilien', '/breed/124/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Basset Artésien Normand', '/breed/125/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Basset Bleu de Gascogne', '/breed/126/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Basset Fauve de Bretagne', '/breed/127/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Basset Griffon Vendéen', '/breed/128/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Petit Basset Griffon Vendéen', '/breed/129/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Briquet Griffon Vendéen', '/breed/130/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chien d''Artois', '/breed/131/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Porcelaine', '/breed/132/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Billy', '/breed/133/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Français Blanc et Noir', '/breed/134/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Français Tricolore', '/breed/135/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Français Blanc et Orange', '/breed/136/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Anglo-Français Tricolore', '/breed/137/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Anglo-Français Blanc et Noir', '/breed/138/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Anglo-Français Blanc et Orange', '/breed/139/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Bleu de Gascogne', '/breed/140/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Grand Gascon Saintongeois', '/breed/141/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Bleu de Gascogne', '/breed/142/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Fauve de Bretagne', '/breed/143/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Griffon Nivernais', '/breed/144/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Poitevin', '/breed/145/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Chien de Saint-Hubert', '/breed/146/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Sloughi', '/breed/147/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Azawakh', '/breed/148/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Galgo Espagnol', '/breed/149/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Irish Wolfhound', '/breed/150/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Deerhound', '/breed/151/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Petit Lévrier Italien', '/breed/152/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Saluki', '/breed/153/image', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO dog (name, birth_date, gender, chip_number, user_id, is_anonymized, created_date, last_modified_date,
                 created_by, last_modified_by)
VALUES ('Rex', '2025-02-15', 'MALE', 'CHIP123456', 4, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Nala', '2017-08-22', 'FEMALE', 'CHIP789012', 4, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Oscar', '2021-02-10', 'STERILIZED_MALE', 'CHIP345678', 5, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Lola', '2018-11-30', 'FEMALE', 'CHIP901234', 5, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Filou', '2022-01-05', 'MALE', 'CHIP567890', 6, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
       ('Mia', '2020-07-18', 'STERILIZED_FEMALE', 'CHIP234567', 6, false, NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO dog_breed (dog_id, breed_id, breed_order)
VALUES (1, 1, 0),  -- Chien 1, race 1, première race
       (2, 3, 0),  -- Chien 2, race 3, première race
       (3, 2, 0),  -- Chien 3, race 2, première race
       (3, 5, 1),  -- Chien 3, race 5, deuxième race
       (4, 6, 0),  -- Chien 4, race 6, première race
       (5, 8, 0),  -- Chien 5, race 8, première race
       (6, 10, 0), -- Chien 6, race 10, première race
       (6, 16, 1); -- Chien 6, race 16, deuxième race


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
VALUES ('Rage', 36),                       -- Vaccin contre la rage, renouvelé tous les 3 ans
       ('CHPPIL (Carré, Hépatite, Parvovirose, Parainfluenza, Leptospirose)',
        12),                               -- Vaccin CHPPIL, renouvelé annuellement
       ('Bordetella (Toux du Chenil)', 6), -- Vaccin Bordetella, renouvelé tous les 6 mois
       ('Leptospirose', 12),               -- Vaccin contre la leptospirose, renouvelé annuellement
       ('Maladie de Lyme', 12),            -- Vaccin contre la maladie de Lyme, renouvelé annuellement
       ('Grippe Canine', 12),              -- Vaccin contre la grippe canine, renouvelé annuellement
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
INSERT INTO registration (registration_date, status, dog_id, course_id, last_modified_date, created_by,
                          last_modified_by)
VALUES ('2025-04-20 09:30:00', 'CONFIRMED', 1, 2, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-05 14:15:00', 'CONFIRMED', 1, 4, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-25 10:45:00', 'CONFIRMED', 1, 6, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-20 09:45:00', 'CONFIRMED', 1, 7, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-25 14:30:00', 'CONFIRMED', 1, 8, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-01 11:00:00', 'CONFIRMED', 2, 5, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-10 16:30:00', 'CONFIRMED', 2, 11, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-13 09:45:00', 'CONFIRMED', 2, 15, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-18 11:45:00', 'CONFIRMED', 2, 22, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-10-05 09:30:00', 'CONFIRMED', 2, 23, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-12-01 10:15:00', 'CONFIRMED', 2, 24, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2026-03-15 11:00:00', 'CONFIRMED', 2, 25, NOW(), 'SYSTEM', 'SYSTEM'),
       (CURRENT_TIMESTAMP, 'PENDING', 2, 18, NOW(), 'SYSTEM', 'SYSTEM'),
       (CURRENT_TIMESTAMP, 'PENDING', 2, 19, NOW(), 'SYSTEM', 'SYSTEM'),
       (CURRENT_TIMESTAMP, 'PENDING', 2, 20, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-05 10:00:00', 'CONFIRMED', 3, 5, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-22 11:15:00', 'CONFIRMED', 3, 12, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-08 11:30:00', 'CONFIRMED', 3, 14, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-05 15:45:00', 'CONFIRMED', 3, 15, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-22 14:30:00', 'CONFIRMED', 3, 21, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-25 13:30:00', 'CONFIRMED', 4, 1, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-15 11:15:00', 'CONFIRMED', 4, 11, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-01 11:30:00', 'CONFIRMED', 4, 13, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-25 11:15:00', 'PENDING', 4, 17, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-20 10:30:00', 'CONFIRMED', 4, 22, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-22 11:30:00', 'CONFIRMED', 5, 12, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-20 09:45:00', 'CONFIRMED', 5, 11, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-10 14:00:00', 'CONFIRMED', 5, 19, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-10-08 11:30:00', 'CONFIRMED', 5, 23, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-10 10:30:00', 'CONFIRMED', 6, 5, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-04-28 11:45:00', 'CONFIRMED', 6, 13, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-08 11:30:00', 'CONFIRMED', 6, 14, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-01 13:45:00', 'CANCELLED', 6, 15, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-15 11:30:00', 'CONFIRMED', 6, 19, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-12-03 10:00:00', 'CONFIRMED', 6, 24, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-05-18 10:30:00', 'CONFIRMED', 2, 3, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-25 09:45:00', 'CONFIRMED', 3, 6, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-25 10:30:00', 'CONFIRMED', 4, 7, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-30 14:45:00', 'CONFIRMED', 5, 8, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-09-25 09:15:00', 'PENDING', 6, 9, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-20 11:30:00', 'CONFIRMED', 2, 12, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-05 15:30:00', 'CONFIRMED', 3, 13, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2026-01-05 11:00:00', 'CONFIRMED', 4, 14, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-10 09:30:00', 'PENDING', 5, 15, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-15 14:00:00', 'CONFIRMED', 6, 16, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-15 15:00:00', 'CONFIRMED', 2, 17, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-25 13:45:00', 'CONFIRMED', 3, 18, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2026-01-05 10:00:00', 'CONFIRMED', 4, 19, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-15 09:30:00', 'CONFIRMED', 5, 20, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-05 14:15:00', 'PENDING', 2, 21, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-10 11:00:00', 'CONFIRMED', 3, 22, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-30 11:45:00', 'CONFIRMED', 4, 23, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-25 10:00:00', 'CONFIRMED', 5, 24, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-15 09:30:00', 'CONFIRMED', 6, 25, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-15 16:30:00', 'CONFIRMED', 2, 26, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-07-10 13:00:00', 'CONFIRMED', 3, 27, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-15 14:30:00', 'CONFIRMED', 4, 28, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-06-15 10:15:00', 'CONFIRMED', 5, 29, NOW(), 'SYSTEM', 'SYSTEM'),
       ('2025-08-05 15:30:00', 'CONFIRMED', 6, 30, NOW(), 'SYSTEM', 'SYSTEM');


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

# create view OwnersV as
# select o.is_active         AS is_active,
#        o.registration_date AS registration_date,
#        o.user_id           AS user_id,
#        o.address           AS address,
#        u.email             AS email,
#        u.firstname         AS firstname,
#        u.lastname          AS lastname,
#        u.phone             AS phone
# from owner o left join CaniCampusConnect.user u on u.user_id = o.user_id;
#
#
# CREATE VIEW CoachesV AS
# SELECT c.acaced_number     AS acaced_number,
#        c.is_active         AS is_active,
#        c.registration_date AS registration_date,
#        c.user_id           AS user_id,
#        u.email             AS email,
#        u.firstname         AS firstname,
#        u.lastname          AS lastname,
#        u.phone             AS phone
# FROM coach c
#          LEFT JOIN CaniCampusConnect.user u ON u.user_id = c.user_id;
#
# CREATE VIEW ClubOwnersV AS
# SELECT co.user_id  AS user_id,
#        u.email     AS email,
#        u.firstname AS firstname,
#        u.lastname  AS lastname,
#        u.phone     AS phone
# FROM club_owner co
#          LEFT JOIN CaniCampusConnect.user u ON u.user_id = co.user_id;
#
# -- Create view for dogs with their breeds
# CREATE VIEW DogsWithBreedsV AS
# SELECT d.id          AS dog_id,
#        d.name        AS dog_name,
#        d.birth_date  AS birth_date,
#        d.gender      AS gender,
#        d.chip_number AS chip_number,
#        d.user_id     AS owner_id,
#        o.firstname   AS owner_firstname,
#        o.lastname    AS owner_lastname,
#        b.id          AS breed_id,
#        b.name        AS breed_name
# FROM dog d
#          JOIN dog_breed db ON d.id = db.dog_id
#          JOIN breed b ON db.breed_id = b.id
#          JOIN owner ow ON d.user_id = ow.user_id
#          JOIN user o ON ow.user_id = o.user_id;
#
# -- Create view for dogs with their weights
# CREATE VIEW DogsWithWeightsV AS
# SELECT d.id                AS dog_id,
#        d.name              AS dog_name,
#        d.birth_date        AS birth_date,
#        d.gender            AS gender,
#        d.chip_number       AS chip_number,
#        d.user_id           AS owner_id,
#        o.firstname         AS owner_firstname,
#        o.lastname          AS owner_lastname,
#        dw.id               AS weight_id,
#        dw.measurement_date AS measurement_date,
#        dw.weight_value     AS weight_value,
#        dw.unit             AS weight_unit
# FROM dog d
#          JOIN dog_weight dw ON d.id = dw.dog_id
#          JOIN owner ow ON d.user_id = ow.user_id
#          JOIN user o ON ow.user_id = o.user_id;
#
# -- Create view for dogs with complete information (breeds and latest weight)
# CREATE VIEW DogsCompleteV AS
# SELECT d.id                                         AS dog_id,
#        d.name                                       AS dog_name,
#        d.birth_date                                 AS birth_date,
#        d.gender                                     AS gender,
#        d.chip_number                                AS chip_number,
#        d.user_id                                    AS owner_id,
#        o.firstname                                  AS owner_firstname,
#        o.lastname                                   AS owner_lastname,
#        GROUP_CONCAT(DISTINCT b.name SEPARATOR ', ') AS breeds,
#        lw.weight_value                              AS latest_weight_value,
#        lw.unit                                      AS latest_weight_unit,
#        lw.measurement_date                          AS latest_weight_date
# FROM dog d
#          JOIN owner ow ON d.user_id = ow.user_id
#          JOIN user o ON ow.user_id = o.user_id
#          LEFT JOIN dog_breed db ON d.id = db.dog_id
#          LEFT JOIN breed b ON db.breed_id = b.id
#          LEFT JOIN (SELECT dw.dog_id, dw.weight_value, dw.unit, dw.measurement_date
#                     FROM dog_weight dw
#                              INNER JOIN (SELECT dog_id, MAX(measurement_date) as max_date
#                                          FROM dog_weight
#                                          GROUP BY dog_id) latest
#                                         ON dw.dog_id = latest.dog_id AND dw.measurement_date = latest.max_date) lw
#                    ON d.id = lw.dog_id
# GROUP BY d.id, d.name, d.birth_date, d.gender, d.chip_number, d.user_id,
#          o.firstname, o.lastname, lw.weight_value, lw.unit, lw.measurement_date;
#
# -- Create view for dogs with health records (vaccinations, veterinary visits, medication treatments)
# CREATE VIEW DogsHealthRecordsV AS
# SELECT d.id            AS dog_id,
#        d.name          AS dog_name,
#        d.birth_date    AS birth_date,
#        d.gender        AS gender,
#        d.chip_number   AS chip_number,
#        d.user_id       AS owner_id,
#        o.firstname     AS owner_firstname,
#        o.lastname      AS owner_lastname,
#
#        -- Vaccination information
#        v.vaccination_id,
#        v.vaccination_date,
#        v.batch_number,
#        v.veterinarian  AS vaccination_veterinarian,
#        vac.vaccine_name,
#
#        -- Veterinary visit information
#        vv.visit_id,
#        vv.visit_date,
#        vv.diagnosis,
#        vv.reason_for_visit,
#        vv.treatment,
#        vv.veterinarian AS visit_veterinarian,
#
#        -- Medication treatment information
#        mt.treatment_id,
#        mt.medication_name,
#        mt.dosage,
#        mt.frequency,
#        mt.start_date,
#        mt.end_date,
#        mt.treatment_reason
# FROM dog d
#          JOIN owner ow ON d.user_id = ow.user_id
#          JOIN user o ON ow.user_id = o.user_id
#          LEFT JOIN vaccination v ON d.id = v.dog_id
#          LEFT JOIN vaccine vac ON v.vaccine_id = vac.vaccine_id
#          LEFT JOIN veterinary_visit vv ON d.id = vv.dog_id
#          LEFT JOIN medication_treatment mt ON d.id = mt.dog_id;
#
# -- Create view for dogs with their course registrations
# CREATE VIEW DogsRegistrationsV AS
# SELECT d.id                 AS dog_id,
#        d.name               AS dog_name,
#        d.birth_date         AS birth_date,
#        d.gender             AS gender,
#        d.chip_number        AS chip_number,
#        d.user_id            AS owner_id,
#        o.firstname          AS owner_firstname,
#        o.lastname           AS owner_lastname,
#
#        -- Registration information
#        r.registration_id,
#        r.registration_date,
#        r.status,
#
#        -- Course information
#        c.course_id,
#        c.title              AS course_title,
#        c.description        AS course_description,
#        c.start_datetime     AS course_start,
#        c.end_datetime       AS course_end,
#        c.max_capacity       AS course_max_capacity,
#
#        -- Course type information
#        ct.course_type_id,
#        ct.name              AS course_type_name,
#        ct.description       AS course_type_description,
#
#        -- Coach information
#        coach.user_id        AS coach_id,
#        coach_user.firstname AS coach_firstname,
#        coach_user.lastname  AS coach_lastname
# FROM dog d
#          JOIN owner ow ON d.user_id = ow.user_id
#          JOIN user o ON ow.user_id = o.user_id
#          LEFT JOIN registration r ON d.id = r.dog_id
#          LEFT JOIN course c ON r.course_id = c.course_id
#          LEFT JOIN course_type ct ON c.course_type_id = ct.course_type_id
#          LEFT JOIN coach ON c.user_id = coach.user_id
#          LEFT JOIN user coach_user ON coach.user_id = coach_user.user_id;
