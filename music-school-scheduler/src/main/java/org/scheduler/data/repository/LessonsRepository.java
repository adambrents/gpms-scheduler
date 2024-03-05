package org.scheduler.data.repository;

import org.scheduler.data.repository.base.BaseRepository;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.configuration.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.StudentDTO;

import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

public class LessonsRepository extends BaseRepository<LessonDTO> {

    private final ObservableList<LocalDateTime> _allStartTimes = FXCollections.observableArrayList();
    private final ObservableList<LessonDTO> _allLessonDTOS = FXCollections.observableArrayList();
    private final ObservableList<LessonDTO> _studentLessonDTOS = FXCollections.observableArrayList();
    private final ObservableList<String> _allTypes = FXCollections.observableArrayList();
    private final ObservableList<String> _studentLessonExists = FXCollections.observableArrayList();
    private final StudentsRepository studentsRepository = new StudentsRepository();

    /**
     * query returns a list of all lessons in database
     *
     * @return
     */
    @Override
    public ObservableList<LessonDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(LessonDTO.class));
    }
    @Override
    public void updateItem(LessonDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(LessonDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(LessonDTO item) throws SQLException {
        super.delete(item);
    }
    /**
     * query returns the latest appointment ID in order to generate an ID in the UI and insert it later
     *
     * @return
     */
    public int getId() {
        try(Statement statement = getStatement()){
            int lastID = 0;
            String query = String.format(
                    "SELECT MAX(Lesson_ID) FROM %s.%s;",
                    _database, DB_TABLES.LESSONS
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt(1) > lastID) {
                    lastID = resultSet.getInt(1);
                } else {
                    //do nothing if the id is less than last id
                }
            }
            lastID++;

            return lastID;
        } catch (SQLException sqlException) {
            return -1;
        }
    }


    /**
     * soft deletes all lessons related to a given customer
     *
     * @param customerId
     */
    public void deleteAllLessonsForStudent(int customerId) {//TODO MOVE THIS OUT OF THE DTO
        try {
            String query = String.format(
                    "DELETE FROM %s.%s WHERE %s = ?;",
                    _database, DB_TABLES.LESSONS, DB_TABLES.STUDENTS + "_ID"
            );

            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            statement.setInt(1, customerId);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * gets all lessons in the upcoming week
     *
     * @return
     */
    public ObservableList<LessonDTO> getWeeklyLessons() {//TODO MOVE THIS OUT OF THE DTO
        ObservableList<LessonDTO> allLessonDTOS = null;
        try {
            allLessonDTOS = getAllItems();
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }
        if (allLessonDTOS == null) {
            return null;
        }

        LocalDateTime nowTime = LocalDateTime.now();
        ZonedDateTime nowTimeCST = nowTime.atZone(ZoneId.of("America/Chicago"));
        LocalDateTime est = nowTimeCST.toLocalDateTime();
        LocalDateTime startOfWeek = est.with(DayOfWeek.MONDAY);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        return allLessonDTOS.stream()
                .filter(lesson -> lesson.getStart().isAfter(startOfWeek) && lesson.getStart().isBefore(endOfWeek))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * gets all lessons in the upcoming month
     *
     * @return
     */
    public ObservableList<LessonDTO> getMonthlyLessons() {//TODO MOVE THIS OUT OF THE DTO
        ObservableList<LessonDTO> allLessonDTOS = null;
        try {
            allLessonDTOS = getAllItems();
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }
        if (allLessonDTOS == null) {
            return null;
        }

        LocalDateTime nowTime = LocalDateTime.now();
        ZonedDateTime nowTimeCST = nowTime.atZone(ZoneId.of("America/Chicago"));
        LocalDateTime est = nowTimeCST.toLocalDateTime();
        LocalDateTime startOfMonth = est.withDayOfMonth(1);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        return allLessonDTOS.stream()
                .filter(lesson -> lesson.getStart().isAfter(startOfMonth) && lesson.getStart().isBefore(endOfMonth))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    /**
     * gets all unique lessons from the db
     *
     * @return
     */
    public ObservableList<String> getTypes() {//TODO MOVE THIS OUT OF THE DTO
        _allTypes.clear();
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT DISTINCT Type FROM %s.%s;",
                    _database, DB_TABLES.LESSONS
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                _allTypes.add(resultSet.getString(1));
            }
            return _allTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets a count of all lessons within a given month and appointment type
     *
     * @param localDateTime
     * @param type
     * @return
     */
    public int getMonthTypeAsInt(LocalDateTime localDateTime, String type) {//TODO MOVE THIS OUT OF THE DTO
        int returnNumber = 0;
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT * FROM %s.%s;",
                    _database, DB_TABLES.LESSONS
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                LocalDateTime YearAndMonth = resultSet.getTimestamp("Start").toLocalDateTime();
                String appointmentType = resultSet.getString("Type");
                if ((localDateTime.getYear() == YearAndMonth.getYear()) && (localDateTime.getMonth() == YearAndMonth.getMonth()) && (type.equals(appointmentType))) {
                    returnNumber++;
                } else {
                    // do nothing if the types are not the same
                }
            }
            return returnNumber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * gets all lessons related to a given studentName
     *
     * @param studentName
     * @return
     */
    public ObservableList<LessonDTO> getStudentLessonsByName(String studentName) {
        List<Integer> studentIds = studentsRepository.getStudentIdsFromName(studentName);
        for (Integer studentId:studentIds) {
            String query = String.format(
                    "SELECT a.* "
                            + "FROM %s.%s AS a "
                            + "INNER JOIN %s.%s AS student ON student.%s = a.%s "
                            + "WHERE student.%s=%s AND Title IS NOT NULL;",
                    _database, DB_TABLES.LESSONS,
                    _database, DB_TABLES.STUDENTS,
                    DB_TABLES.STUDENTS + "_ID", DB_TABLES.STUDENTS + "_ID",
                    DB_TABLES.STUDENTS + "_ID", studentId);
            try(Statement statement = getStatement()) {
                _studentLessonDTOS.clear();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    _studentLessonDTOS.add(buildLessonFromResultSet(resultSet));
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return _studentLessonDTOS;
    }

    /**
     * checks to see if a customer has any lessons scheduled
     *
     * @param selectedStudentDTO
     * @return
     */
    public boolean isStudentHaveLessons(StudentDTO selectedStudentDTO) {
        int customerID = selectedStudentDTO.getStudentId();
        _studentLessonExists.clear();
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT * FROM %s.%s WHERE Student_ID=%d AND Title IS NOT NULL;",
                    _database, DB_TABLES.LESSONS, customerID
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                _studentLessonExists.add(resultSet.getString(2));
            }
            return _studentLessonExists.isEmpty();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * gets the soonest appointment in the next 15 minutes
     *
     * @return
     */
    public LessonDTO getLessonsNext15Minutes(LocalDateTime now) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
//TODO COME BACK TO THIS
        return null;


//        try {
//            String sql = String.format(
//                    "SELECT * FROM %s.%s AS a WHERE Start>=? AND Start<=? ORDER BY Start ASC LIMIT 1;",
//                    _database, DB_TABLES.LESSONS
//            );
//            preparedStatement = JDBC.getConnection().prepareStatement(sql);
//
//            int x = 1;
//            preparedStatement.setTimestamp(x++, Timestamp.valueOf(now));
//            preparedStatement.setTimestamp(x++, Timestamp.valueOf(now.plusMinutes(15)));
//
//            resultSet = preparedStatement.executeQuery();
//            if (!resultSet.next()) {
//                return null;
//            }
//
//
//            return buildLessonFromResultSet(resultSet);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * gets all taken start times from the db by date and converts those times from UTC to times on the user's system
     * @param localDate
     * @return
     */
    public ObservableList<LessonDTO> getAllTakenLessonTimesByDate(LocalDate localDate) {
        ObservableList<LessonDTO> allTakenStartTimes = FXCollections.observableArrayList();
        _allStartTimes.clear();
        try(Statement statement = getStatement()) {
            String sql = String.format(
                    "SELECT * FROM %s.%s WHERE DAYOFMONTH(Start) = DAYOFMONTH('%s');",
                    _database, DB_TABLES.LESSONS, localDate
            );
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                allTakenStartTimes.add(buildLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return allTakenStartTimes;
    }
    private LessonDTO buildLessonFromResultSet(ResultSet resultSet) throws SQLException {

        LessonDTO lessonDTO = new LessonDTO(
                resultSet.getInt("Lesson_ID"),
                resultSet.getString("Description"),
                resultSet.getString("Location"),
                resultSet.getString("Type"),
                resultSet.getTimestamp("Start").toLocalDateTime(),
                resultSet.getTimestamp("End").toLocalDateTime(),
                resultSet.getInt("User_ID"));
        lessonDTO.setStudentId(resultSet.getInt("Student_ID"));
        return lessonDTO;
    }
}
