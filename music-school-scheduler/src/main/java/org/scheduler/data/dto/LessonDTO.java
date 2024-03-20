package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.IComboBox;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LessonDTO extends DTOBase<LessonDTO> implements ISqlConvertible<LessonDTO>, IComboBox {
    private StudentDTO student;
    private TeacherDTO teacher;
    private LessonScheduledDTO scheduledLesson ;
    

    public LessonDTO(){
        
    }

    public LessonDTO(StudentDTO student,
                     TeacherDTO teacher,
                     LessonScheduledDTO scheduledLesson,
                     LocalDateTime createDate,
                     int createdBy,
                     LocalDateTime lastUpdate,
                     int lastUpdatedBy) {
        this.student = student;
        this.teacher = teacher;
        this.scheduledLesson = scheduledLesson;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.updateDate = lastUpdate;
        this.updatedBy = lastUpdatedBy;
    }
    public LessonDTO(int id,
                     StudentDTO student,
                     TeacherDTO teacher,
                     LessonScheduledDTO scheduledLesson,
                     LocalDateTime lastUpdate,
                     int lastUpdatedBy) {
        super.id = id;
        this.student = student;
        this.teacher = teacher;
        this.scheduledLesson = scheduledLesson;
        this.updateDate = lastUpdate;
        this.updatedBy = lastUpdatedBy;
    }
    public LessonDTO(int id,
                     int studentId,
                     int teacherId,
                     LocalDateTime createDate,
                     int createdBy,
                     LocalDateTime lastUpdate,
                     int lastUpdatedBy) {
        super.id = id;
        this.student = new StudentDTO(studentId);
        this.teacher = new TeacherDTO(teacherId);
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.updateDate = lastUpdate;
        this.updatedBy = lastUpdatedBy;
    }



    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }



    public void setScheduledLesson(LessonScheduledDTO scheduledLesson) {
        this.scheduledLesson = scheduledLesson;
    }

    public String getStudentFullName(){
        return this.getStudent().getFullName();
    }
    public String getTeacherFullName(){
        return this.getTeacher().getFullName();
    }

    public String getLessonTimeFormatted() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String dayOfWeekTitleCase = this.getScheduledLesson().getDayOfWeek().toString();
        dayOfWeekTitleCase = dayOfWeekTitleCase.substring(0, 1).toUpperCase() + dayOfWeekTitleCase.substring(1).toLowerCase();

        return dayOfWeekTitleCase + "'s @ " + this.getScheduledLesson().getStart().format(timeFormatter);
    }


    public LessonScheduledDTO getScheduledLesson() {
        return scheduledLesson;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }
    public StudentDTO getStudent() {
        return student;
    }
    public String getGoldCup(){
        return yesOrNo(getScheduledLesson().isGoldCup());
    }
    public String getLevelName(){
        return getScheduledLesson().getLevel().getName();
    }
    public String getBookName(){
        return getScheduledLesson().getBook().getName();
    }
    public String getInstrumentName(){
        return getScheduledLesson().getInstrument().getName();
    }
    public String getNewStudent(){
        return yesOrNo(getScheduledLesson().isNewStudent());
    }

    private String yesOrNo(boolean bool){
        if(bool){
            return "Yes";
        }else{
            return "No";
        }
    }
    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.LESSONS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        // Assuming Create_Date and Last_Update are managed by the database or application logic
        String sql = "INSERT INTO lessons (Student_ID, Teacher_ID, Created_By, Last_Updated_By) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, lessonDTO.student.getId());
        statement.setInt(2, lessonDTO.teacher.getId());
        statement.setInt(3, lessonDTO.createdBy);
        statement.setInt(4, lessonDTO.updatedBy);

        return statement;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        String sql = "UPDATE lessons " +
                "SET Student_ID = ?, Teacher_ID = ?, Last_Update = NOW(), Last_Updated_By = ? " +
                "WHERE Lesson_ID = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, lessonDTO.student.getId());
        statement.setInt(2, lessonDTO.teacher.getId());
        statement.setInt(3, lessonDTO.updatedBy);
        statement.setInt(4, lessonDTO.id);

        return statement;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.LESSONS + " WHERE Lesson_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, lessonDTO.id);

        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new LessonsRepository();
    }

    @Override
    public LessonDTO fromResultSet(ResultSet rs) {
        try {
            //Don't forget to grab the scheduled lesson in its own DTO, if needed!
            Timestamp createDateTimestamp = rs.getTimestamp("Create_Date");
            LocalDateTime createDate = (createDateTimestamp != null) ? createDateTimestamp.toLocalDateTime() : null;

            Timestamp lastUpdateTimestamp = rs.getTimestamp("Last_Update");
            LocalDateTime lastUpdate = (lastUpdateTimestamp != null) ? lastUpdateTimestamp.toLocalDateTime() : null;
            return new LessonDTO(
                    rs.getInt("Lesson_ID"),
                    rs.getInt("Student_ID"),
                    rs.getInt("Teacher_ID"),
                    createDate,
                    rs.getInt("Created_By"),
                    lastUpdate,
                    rs.getInt("Last_Updated_By")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
