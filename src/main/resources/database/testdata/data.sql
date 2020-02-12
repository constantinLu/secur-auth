#USER TABLE
INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD)
VALUES (1, 'lungu', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK');

INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD)
VALUES (2, 'cosmina', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK');

INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD)
VALUES (3, 'madalina', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK');


#USER INFO TABLE
INSERT INTO secur_auth.user_info (ID, FIRST_NAME, LAST_NAME, EMAIL, ABOUT_ME)
VALUES (1, 'Freddy', 'Mercury', 'fm@gmail.com', 'Singer');

INSERT INTO secur_auth.user_info (ID, FIRST_NAME, LAST_NAME, EMAIL, ABOUT_ME)
VALUES (2, 'Michael', 'Jackson', 'jakson@gmail.com', 'Singer');

INSERT INTO secur_auth.user_info (ID, FIRST_NAME, LAST_NAME, EMAIL, ABOUT_ME)
VALUES (3, 'Nikki', 'Lauda', 'nikki@gmail.com', 'Driver');


#ROLE TABLE
INSERT INTO secur_auth.role (ID, ROLE)
VALUES (1, 'ADMIN');

INSERT INTO secur_auth.role (ID, ROLE)
VALUES (2, 'ADMIN');



