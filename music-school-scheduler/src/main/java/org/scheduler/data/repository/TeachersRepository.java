package org.scheduler.data.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.interfaces.IReportable;
import org.scheduler.data.dto.mapping.TeacherInstrumentDTO;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.reports.InstrumentLessonsRatioDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeachersRepository extends BaseRepository<TeacherDTO> {
    private final Logger _logger = LoggerFactory.getLogger(TeachersRepository.class);
    private static final ObservableList<TeacherDTO> ALL_TEACHER_DTOS = FXCollections.observableArrayList();

    /**
     * returns a user object in response to a username
     *
     * @param teacherName
     * @return
     */
    public static TeacherDTO getTeacherByName(String teacherName){
        ALL_TEACHER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM music_school.teachers WHERE Teacher_Name='" + teacherName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new TeacherDTO().fromResultSet(resultSet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns a user object in response to a userid
     * @param teacherId
     * @return
     */
    public static TeacherDTO getTeacherById(int teacherId){
        ALL_TEACHER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM music_school.teachers WHERE Teacher_Id=" + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new TeacherDTO().fromResultSet(resultSet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<TeacherDTO> getAllItems() {
        return FXCollections.observableArrayList(super.getAllItemsFromType(new TeacherDTO()));
    }

    @Override
    public void updateItem(TeacherDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(TeacherDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(TeacherDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {
        if(item instanceof TeacherInstrumentDTO){
            ((TeacherInstrumentDTO)item).setMappingFromId(key);
        }
    }

    public List<TeacherInstrumentDTO> getAllTeacherInstrumentMappingsByTeacherId(int teacherId){
        List<TeacherInstrumentDTO> teacherInstrumentMappings = new ArrayList<>();
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "Teacher_Instrument_Id, " +
                    "Teacher_Id, " +
                    "Instrument_Id " +
                    "FROM teacher_instrument " +
                    "WHERE Teacher_Id = " + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                teacherInstrumentMappings.add(new TeacherInstrumentDTO(
                    resultSet.getInt("Teacher_Instrument_Id"),
                    resultSet.getInt("Teacher_Id"),
                    resultSet.getInt("Instrument_Id")
                ));
            }
            return teacherInstrumentMappings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherInstrumentMappings;
    }

    public String getTeacherInstrumentsAsString(int teacherId) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "i.Name " +
                    "FROM teacher_instrument AS ti " +
                    "INNER JOIN instruments AS i ON i.Instrument_Id = ti.Instrument_Id " +
                    "WHERE ti.Teacher_id = " + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                stringBuilder.append(resultSet.getString("Name"));
                stringBuilder.append(", ");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<Integer> getTeacherInstrumentIds(int teacherId){
        List<Integer> teacherInstrumentIds = new ArrayList<>();
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "Instrument_Id " +
                    "FROM teacher_instrument " +
                    "WHERE Teacher_id = " + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                teacherInstrumentIds.add(resultSet.getInt("Instrument_Id"));
            }

            return teacherInstrumentIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherInstrumentIds;
    }

    public int getTeacherStudentsCount(int teacherId) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                        "COUNT(1) AS count " +
                        "FROM student_teacher " +
                        "WHERE Teacher_id = " + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                resultSet.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    private List<Integer> getAllTeacherInstrumentIds(int teacherId){
        List<Integer> teacherInstrumentIds = new ArrayList<>();
        try(Statement statement = getStatement()) {
            String sql = "SELECT Instrument_Id " +
                    "FROM teacher_instrument " +
                    "WHERE Teacher_id = " + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                teacherInstrumentIds.add(resultSet.getInt("Instrument_Id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return teacherInstrumentIds;
        }
        return teacherInstrumentIds;
    }


    public void insertNewTeacher(TeacherDTO item, List<InstrumentDTO> teacherInstruments){
        Connection conn = null;
        try {
            conn = JDBC.getConnection();
            conn.setAutoCommit(false);

            int generatedId = insertReturnGeneratedKey(item, conn);

            for (InstrumentDTO instrument : teacherInstruments) {
                insert(new TeacherInstrumentDTO(generatedId, instrument.getId(), instrument.getSelected()), conn);
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    _logger.error("Error while rolling back transaction!", ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    _logger.error("Could not close connection!", e);
                }
            }
        }
    }
    public void updateTeacher(TeacherDTO newTeacher, List<InstrumentDTO> teacherInstruments) throws SQLException {

        if(newTeacher.getId() == 0){
            throw new RuntimeException("Cannot update a teacher without a valid Teacher_ID!");
        }

        List<TeacherInstrumentDTO> newTeacherInstruments = new ArrayList<>();
        Connection connection = null;
        try {
            connection = JDBC.getConnection();
            connection.setAutoCommit(false);

            updateItem(newTeacher, connection);

            List<TeacherInstrumentDTO> existingMappings = getAllTeacherInstrumentMappingsByTeacherId(newTeacher.getId());

            Set<Integer> selectedInstrumentIds = teacherInstruments.stream()
                    .filter(instrument -> instrument.getSelected().get())
                    .map(InstrumentDTO::getId)
                    .collect(Collectors.toSet());

            List<TeacherInstrumentDTO> toAdd = new ArrayList<>();
            List<TeacherInstrumentDTO> toRemove = new ArrayList<>();

            for (InstrumentDTO instrument : teacherInstruments) {
                if (instrument.getSelected().get() && existingMappings.stream().noneMatch(mapping -> mapping.getMappingToId() == instrument.getId())) {
                    toAdd.add(new TeacherInstrumentDTO(newTeacher.getId(), instrument.getId(), instrument.getSelected()));
                }
            }

            for (TeacherInstrumentDTO existingMapping : existingMappings) {
                if (!selectedInstrumentIds.contains(existingMapping.getMappingToId())) {
                    toRemove.add(existingMapping);
                }
            }

            for (TeacherInstrumentDTO add : toAdd) {
                insert(add, connection);
            }

            for (TeacherInstrumentDTO remove : toRemove) {
                delete(remove, connection);
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    _logger.error("Error while rolling back transaction!", ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    _logger.error("Could not close connection!", e);
                }
            }
        }
    }

    public List<InstrumentLessonsRatioDTO> getTeacherInstrumentToLessonsRatio() {
        List<InstrumentLessonsRatioDTO> instrumentLessonsRatioItems = new ArrayList<>();
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "COUNT(1) NumberOfLessons, " +
                    "i.Name, " +
                    "t.Teacher_First_Name, " +
                    "t.Teacher_Last_Name " +
                    "FROM teachers AS t " +
                    "INNER JOIN lessons AS l ON l.Teacher_Id = t.Teacher_id " +
                    "INNER JOIN lessons_scheduled AS ls ON ls.Lesson_Id = l.Lesson_Id " +
                    "INNER JOIN instruments AS i ON i.Instrument_Id = ls.Lesson_Instrument_ID " +
                    " GROUP BY i.Name, " +
                    "t.Teacher_First_Name, " +
                    "t.Teacher_Last_Name;";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                instrumentLessonsRatioItems.add(new InstrumentLessonsRatioDTO().fromResultSet(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return instrumentLessonsRatioItems;
        }
        return instrumentLessonsRatioItems;
    }

//    public List<TeacherDTO> getPossibleStudentInstrumentTeachers(int studentId) {
//        List<TeacherDTO> teachersMatchingStudentInstruments = new ArrayList<>();
//        String sql = "SELECT " +
//                "t.* " +
//                "FROM student_instrument " +
//                "INNER JOIN teacher_instrument ON teacher_instrument.Instrument_Id = student_instrument.Instrument_Id " +
//                "INNER JOIN teachers t ON t.Teacher_Id = teacher_instrument.Teacher_Id " +
//                "WHERE student_instrument.Student_Id = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, studentId);
//
//            try (ResultSet resultSet = pstmt.executeQuery()) {
//                while (resultSet.next()) {
//                    teachersMatchingStudentInstruments.add(new TeacherDTO().fromResultSet(resultSet));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return teachersMatchingStudentInstruments;
//    }


}
