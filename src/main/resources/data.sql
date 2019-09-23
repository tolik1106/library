INSERT INTO users (name, email, password, phone)
VALUES ('tolik1106', 'tolik1106@gmail.com', '00000', '0950160701'),
       ('marina', 'marina@i.ua', '00000', '0956842354'),
       ('andriy', 'andriy@gmail.com', '00000', '0958496257');

INSERT INTO role (role)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO user_role
VALUES (1, 1),
       (2, 2),
       (2, 3);

INSERT INTO book (user_id, name, owned_date, bookcase, bookshelf)
VALUES (NULL , 'Harry Potter and the Philosopher''s Stone', NULL , 1, 1),
       (2, 'Harry Potter and the Chamber of Secrets', now(), 1, 1),
       (2, 'Harry Potter and the Prisoner of Azkaban', now(), 1, 1),
       (NULL, 'Harry Potter and the Goblet of Fire', NULL, 1, 1),
       (1, 'Harry Potter and the Order of the Phoenix', now(), 1, 2),
       (1, 'Harry Potter and the Half-Blood Prince', now(), 1, 2),
       (NULL, 'Harry Potter and the Deathly Hallows', NULL, 1, 2),
       (3, 'The Mysterious Affair at Styles', now(), 2, 1),
       (3, 'The Murder on the Links', now(), 2, 1),
       (3, 'Poirot Investigates', now(), 2, 1),
       (NULL, 'The Big Four', NULL, 2, 1),
       (NULL, 'The Mystery of the Blue Train', NULL, 2, 1),
       (NULL, 'Black Coffee', NULL, 2, 1),
       (NULL, 'Peril at End House', NULL, 2, 1);

INSERT INTO author (name)
VALUES ('Joanne Rowling'),
       ('Agatha Christie');

INSERT INTO book_author (author_id, book_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
       (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14);

INSERT INTO attribute (name)
VALUES ('harry'), ('potter'), ('magic'), ('wizard'),
       ('ron'), ('weasley'), ('hermione'), ('granger'),
       ('hogwarts'), ('voldemort'), ('muggles'),
       ('stone'),
       ('chamber'),
       ('azkaban'),
       ('goblet'),
       ('orden'),
       ('phoenix'),
       ('half-blood'),
       ('hallows'),
       ('wand'),
       ('poirot'), ('detective');

INSERT INTO book_attribute (attribute_id, book_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
       (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7),
       (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7),
       (4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6), (4, 7),
       (5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), (5, 7),
       (6, 1), (6, 2), (6, 3), (6, 4), (6, 5), (6, 6), (6, 7),
       (7, 1), (7, 2), (7, 3), (7, 4), (7, 5), (7, 6), (7, 7),
       (8, 1), (8, 2), (8, 3), (8, 4), (8, 5), (8, 6), (8, 7),
       (9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6), (9, 7),
       (10, 1), (10, 2), (10, 3), (10, 4), (10, 5), (10, 6), (10, 7),
       (11, 1), (11, 2), (11, 3), (11, 4), (11, 5), (11, 6), (11, 7),
       (12, 1), (13, 2), (14, 3), (15, 4), (16, 5), (17, 5), (18, 6), (19, 7),
       (20, 1), (20, 2), (20, 3), (20, 4), (20, 5), (20, 6), (20, 7),
       (21, 8), (21, 9), (21, 10), (21, 11), (21, 12), (21, 13), (21, 14),
       (22, 8), (22, 9), (22, 10), (22, 11), (22, 12), (22, 13), (22, 14);