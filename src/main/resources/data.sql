
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

#USER TABLE
INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD, USER_INFO_ID)
    VALUES (1, 'lungu', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK', 1);

INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD, USER_INFO_ID)
    VALUES (2, 'cosmina', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK', 2);

INSERT INTO secur_auth.user (ID, USER_NAME, PASSWORD, USER_INFO_ID)
    VALUES (3, 'madalina', '$2a$10$Ej3wkupBtxoM..0/PgSVOu9173oETSJJOpQ/tdYqJFwh5fentNumK', 3);

#USER_ROLE TABLE
INSERT INTO secur_auth.user_role (USER_ID, ROLE_ID) VALUES (1, 1);
INSERT INTO secur_auth.user_role (USER_ID, ROLE_ID) VALUES (1, 2);
INSERT INTO secur_auth.user_role (USER_ID, ROLE_ID) VALUES (2, 1);
INSERT INTO secur_auth.user_role (USER_ID, ROLE_ID) VALUES (3, 1);

