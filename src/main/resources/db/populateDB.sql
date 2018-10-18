DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
  (100000, '2018-10-18 10:00', 'Завтрак',   650),
  (100000, '2018-10-18 13:00', 'Обед',      900),
  (100000, '2018-10-18 22:00', 'Ужин',      500),
  (100000, '2018-10-19 07:00', 'Завтрак',   256),
  (100000, '2018-10-19 13:00', 'Обед',      1000),
  (100000, '2018-10-19 22:00', 'Ужин',      500),

  (100001, '2018-10-18 09:00', 'Завтрак 1', 500),
  (100001, '2018-10-18 12:00', 'Обед 1',    950),
  (100001, '2018-10-18 18:00', 'Ужин 1',    650),
  (100001, '2018-10-19 09:00', 'Завтрак 1', 400),
  (100001, '2018-10-19 12:00', 'Обед 1',    800),
  (100001, '2018-10-19 18:00', 'Ужин 1',    300)