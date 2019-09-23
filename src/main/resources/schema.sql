DROP TABLE IF EXISTS book_attribute;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS attribute;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS users;

/*==============================================================*/
/* Table: Attribute                                              */
/*==============================================================*/
CREATE TABLE attribute
(
  id                   INT NOT NULL AUTO_INCREMENT,
  name                 VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_attribute_name UNIQUE (name)
);

/*==============================================================*/
/* Table: Author                                                */
/*==============================================================*/
CREATE TABLE author
(
  id                   INT NOT NULL AUTO_INCREMENT,
  name                 VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_author_name UNIQUE (name)
);

/*==============================================================*/
/* Table: Book                                                  */
/*==============================================================*/
CREATE TABLE book
(
  id                   INT NOT NULL AUTO_INCREMENT,
  user_id              INT DEFAULT NULL,
  name                 VARCHAR(100) NOT NULL,
  owned_date           TIMESTAMP DEFAULT NULL,
  bookcase             INT NOT NULL,
  bookshelf            INT NOT NULL,
  ordered              TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id)
);

/*==============================================================*/
/* Table: Book_attribute                                        */
/*==============================================================*/
CREATE TABLE book_attribute
(
  attribute_id         INT NOT NULL,
  book_id              INT NOT NULL,
  PRIMARY KEY (attribute_id, book_id)
);

/*==============================================================*/
/* Table: Book_author                                           */
/*==============================================================*/
CREATE TABLE book_author
(
  book_id              INT NOT NULL,
  author_id            INT NOT NULL,
  PRIMARY KEY (book_id, author_id)
);

/*==============================================================*/
/* Table: Role                                                  */
/*==============================================================*/
CREATE TABLE role
(
  id                   INT NOT NULL AUTO_INCREMENT,
  role                 VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_role UNIQUE (role)
);

/*==============================================================*/
/* Table: User_role                                             */
/*==============================================================*/
CREATE TABLE user_role
(
  role_id              INT NOT NULL,
  user_id              INT NOT NULL,
  PRIMARY KEY (role_id, user_id)
);

/*==============================================================*/
/* Table: Users                                                 */
/*==============================================================*/
CREATE TABLE users
(
  id                   INT NOT NULL AUTO_INCREMENT,
  name                 VARCHAR(64) NOT NULL,
  email                VARCHAR(255) NOT NULL,
  password             VARCHAR(64) NOT NULL,
  phone                VARCHAR(10) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_user_email UNIQUE (email)
);

ALTER TABLE book ADD CONSTRAINT fk_book_users FOREIGN KEY (user_id)
REFERENCES users (id);

ALTER TABLE book_attribute ADD CONSTRAINT fk_book_att_book FOREIGN KEY (book_id)
REFERENCES book (id) ON DELETE CASCADE;

ALTER TABLE book_attribute ADD CONSTRAINT fk_book_att_attribute FOREIGN KEY (attribute_id)
REFERENCES attribute (id);

ALTER TABLE book_author ADD CONSTRAINT fk_book_aut_author FOREIGN KEY (author_id)
REFERENCES author (id);

ALTER TABLE book_author ADD CONSTRAINT fk_book_aut_book FOREIGN KEY (book_id)
REFERENCES book (id) ON DELETE CASCADE;

ALTER TABLE user_role ADD CONSTRAINT fk_user_role_users FOREIGN KEY (user_id)
REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_role ADD CONSTRAINT fk_user_rol_relations_role FOREIGN KEY (role_id)
REFERENCES role (id);