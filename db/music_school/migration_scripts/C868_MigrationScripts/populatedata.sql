USE music_school;

DELETE FROM lessons_scheduled;
DELETE FROM lessons_scheduled_mst;
DELETE FROM books;
DELETE FROM instruments;
DELETE FROM levels;
DELETE FROM recitals;
DELETE FROM student_recitals;
DELETE FROM teacher_student_instrument;
DELETE FROM lessons;
DELETE FROM students;
DELETE FROM users;
DELETE FROM teachers;
-- DELETE FROM contacts;

-- Users

INSERT INTO users VALUES(1, 'test', 'test', NOW(), 'script', NOW(), 'script', 1);
INSERT INTO users VALUES(2, 'admin', 'admin', NOW(), 'script', NOW(), 'script', 1);

-- contacts

-- INSERT INTO contacts VALUES(1,	'Anika Costa', 'acoasta@company.com');
-- INSERT INTO contacts VALUES(2,	'Daniel Garcia',	'dgarcia@company.com');
-- INSERT INTO contacts VALUES(3,	'Li Lee',	'llee@company.com');

-- Students

INSERT INTO students VALUES(1, 'Daddy', 'Warbucks', '1919 Boardwalk', NULL, '01291', '869-908-1875', NOW(), 'script', NOW(), 'script');
INSERT INTO students VALUES(2, 'Lady' , 'McAnderson', '2 Wonder Way', NULL, 'AF19B', '11-445-910-2135', NOW(), 'script', NOW(), 'script');
INSERT INTO students VALUES(3, 'Dudley', 'Do-Right', '48 Horse Manor ', NULL, '28198', '874-916-2671', NOW(), 'script', NOW(), 'script');

-- Teachers
INSERT INTO teachers VALUES(1, 'Daddy', 'Warbucks', '1919 Boardwalk', NULL, '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', NULL);
INSERT INTO teachers VALUES(2, 'Lady' , 'McAnderson', '2 Wonder Way', NULL, 'AF19B', '11-445-910-2135', NOW(), 'script', NOW(), 'script', NULL);
INSERT INTO teachers VALUES(3, 'Dudley', 'Do-Right', '48 Horse Manor ', NULL, '28198', '874-916-2671', NOW(), 'script', NOW(), 'script', NULL);

-- Lessons

INSERT INTO lessons VALUES(1, /*'title',*/ 'description', 'location', 'Planning Session', '2020-05-28 12:00:00', '2020-05-28 13:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 3);
INSERT INTO lessons VALUES(2, /*'title',*/ 'description', 'location', 'De-Briefing', '2020-05-29 12:00:00', '2020-05-29 13:00:00', NOW(), 'script', NOW(), 'script', 2, 2, 2);
