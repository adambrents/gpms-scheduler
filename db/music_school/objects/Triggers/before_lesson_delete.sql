DELIMITER $$

CREATE TRIGGER before_lesson_delete
BEFORE DELETE ON lessons
FOR EACH ROW
BEGIN
    DECLARE remaining_lessons INT;
    DECLARE remaining_scheduled_lessons INT;

    -- Check remaining lessons for the student
    SELECT COUNT(*) INTO remaining_lessons
    FROM lessons
    WHERE Student_ID = OLD.Student_ID
    AND Lesson_ID <> OLD.Lesson_ID;

    -- Check remaining scheduled lessons for the student
    SELECT COUNT(*) INTO remaining_scheduled_lessons
    FROM lessons_scheduled
    WHERE Lesson_Id IN (SELECT Lesson_ID FROM lessons WHERE Student_ID = OLD.Student_ID);

    -- If no more lessons exist for the student, delete the student's book mappings
    IF remaining_lessons = 0 AND remaining_scheduled_lessons = 0 THEN
        DELETE FROM student_book WHERE Student_Id = OLD.Student_ID;
        DELETE FROM student_instrument WHERE Student_Id = OLD.Student_ID;
        DELETE FROM student_level WHERE Student_Id = OLD.Student_ID;
        DELETE FROM student_teacher WHERE Student_Id = OLD.Student_ID AND Teacher_Id = OLD.Teacher_ID;
    END IF;
END$$

DELIMITER ;
