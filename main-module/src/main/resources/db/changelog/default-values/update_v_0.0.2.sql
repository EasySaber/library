INSERT INTO person(first_name, middle_name, last_name, date_of_birth, date_time_created, date_time_last_modified,
                   version)
VALUES ('Алексей', 'Анатольевич', 'Надзорный', '1985-01-01', now(), now(), 0);

UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'ivanov')
WHERE last_name = 'Иванов'
  AND date_of_birth = '1981-11-01';
UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'petrov')
WHERE last_name = 'Петров'
  AND date_of_birth = '1962-02-01';
UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'sidorov')
WHERE last_name = 'Сидоров'
  AND date_of_birth = '1982-10-05';
UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'pervii')
WHERE last_name = 'Первый'
  AND date_of_birth = '1981-11-09';
UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'krasnov')
WHERE last_name = 'Краснов'
  AND date_of_birth = '1975-03-16';
UPDATE person
SET account_id = (SELECT id FROM customer WHERE username = 'admin')
WHERE last_name = 'Надзорный'
  AND date_of_birth = '1985-01-01';