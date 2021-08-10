--Just two roles
INSERT INTO ROLE(role_name, description) VALUES ('ROLE_ADMIN', 'User with administrator privileges');
INSERT INTO ROLE(role_name, description) VALUES ('ROLE_USER', 'User with basic privileges');

--insert admin user
INSERT INTO USERS(username, password, status, balance, uuid)
VALUES('admin@test.com','$2a$10$mZ9ACXfnUmOJgL5GCSEX5u8Ly6LYPgUo1NnyNL8QuOVuc4B3WuR1m','ACTIVE',1000,'9ce84592-7178-43d0-999f-26d90dbcfd01');

INSERT INTO users_roles(user_id, role_id)
VALUES(
          (SELECT id FROM users WHERE username='admin@test.com'),
          (SELECT id FROM role where role_name = 'ROLE_ADMIN')
      );

--100 credits for new users
INSERT INTO SYSTEM_CONFIGURATION(name, value, description) VALUES ('DEFAULT_BALANCE', '100', 'Initial balance for new users');

--First services available
INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130001', 'Integer Addition','This service returns the result of adding two double numbers', 'ADDITION', 'ACTIVE', 1, 2);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130002', 'Integer Subtraction', 'This service returns the result of subtracting two double numbers', 'SUBTRACTION', 'ACTIVE', 1, 2);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130003', 'Integer Multiplication', 'This service returns the result of multiplying two double numbers', 'MULTIPLICATION', 'ACTIVE', 5, 2);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130004', 'Integer Division', 'This service returns the result of dividing two double numbers', 'DIVISION', 'ACTIVE', 5, 2);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130005', 'Integer square root', 'This service returns the square root of a double number', 'SQUARE_ROOT', 'ACTIVE', 10, 1);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130006', 'Integer math operation', 'This service execute the math operation specified by the user', 'FREE_FORM', 'ACTIVE', 10, 1);

INSERT INTO SERVICE(uuid, name, description, type, status, cost, num_parameters)
VALUES ('b647290a-f2f8-11eb-9a03-0242ac130007', 'Random String', 'As it name implies, this service returns a random string.', 'RANDOM_STRING', 'ACTIVE', 1, 0);