INSERT INTO user (firstname, lastname, email, password, phone)
VALUES ('Stéphane', 'Scheeres', 'stephane.scheeres@gmail.com', '$2a$10$BfXutvk76gOr/RblVNl7qOw3mDrBGxeiPWjS9B3/Iq4GgpwWIUODu', '0600000000'),
       ('Victor', 'Monteragioni', 'victor.monteriggioni@gmail.com', '$2a$10$BfXutvk76gOr/RblVNl7qOw3mDrBGxeiPWjS9B3/Iq4GgpwWIUODu', '+33 6 02 02 49 53');

INSERT INTO owner (id, is_active, registration_date, address)
VALUES (1, true, current_date, 'Rue sainte Marie');

INSERT INTO club_owner(id)
VALUES (2);