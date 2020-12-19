UPDATE author SET date_time_of_birth = '1799-06-06' WHERE last_name = 'Пушкин';
UPDATE author SET date_time_of_birth = '1821-11-11' WHERE last_name = 'Достоевский';
UPDATE author SET date_time_of_birth = '1891-05-18' WHERE last_name = 'Булгаков';
UPDATE author SET date_time_of_birth = '1828-09-09' WHERE last_name = 'Толстой';
UPDATE author SET date_time_of_birth = '1895-09-21' WHERE last_name = 'Есенин';
UPDATE author SET date_time_of_birth = '1917-03-01' WHERE date_time_of_birth IS NULL;

UPDATE book SET date_delivery = '2020-11-01' WHERE date_delivery IS NULL;
UPDATE book SET date_publication = '1833-11-01' WHERE book_name = 'Евгений Онегин';
UPDATE book SET date_publication = '1866-09-01' WHERE book_name = 'Преступление и наказание';
UPDATE book SET date_publication = '1966-02-01' WHERE book_name = 'Мастер и маргарита';
UPDATE book SET date_publication = '1900-11-01' WHERE book_name = 'Илья Муромец';
UPDATE book SET date_publication = '1926-12-01' WHERE book_name = 'Черный человек';
UPDATE book SET date_publication = '2000-11-01' WHERE date_publication IS NULL;


