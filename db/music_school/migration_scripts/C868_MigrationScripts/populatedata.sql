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

INSERT INTO users VALUES(1, 'test', 'test', NOW(), 'script', NOW(), 1, 1);
INSERT INTO users VALUES(2, 'admin', 'admin', NOW(), 'script', NOW(), 1, 1);