package org.scheduler.repository;

import org.scheduler.constants.Constants;
import org.scheduler.repository.base.BaseDTO;
import org.scheduler.repository.configuration.model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.viewmodels.Lesson;
import org.scheduler.viewmodels.Student;

import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

public class LessonsDTO extends BaseDTO {

    private final ObservableList<LocalDateTime> _allStartTimes = FXCollections.observableArrayList();
    private final ObservableList<Lesson> _allLessons = FXCollections.observableArrayList();
    private final ObservableList<Lesson> _studentLessons = FXCollections.observableArrayList();
    private final ObservableList<String> _allTypes = FXCollections.observableArrayList();
    private final ObservableList<String> _studentLessonExists = FXCollections.observableArrayList();
    private final StudentsDTO studentsDTO = new StudentsDTO();

    /**
     * query returns a list of all lessons in database
     *
     * @return
     */
    public ObservableList<Lesson> getAllLessons() {

        try(Statement statement = getStatement()) {
            _allLessons.clear();
            String query = String.format(
                    "SELECT a.* "
                    + "FROM %s.%s AS a "
                    + "INNER JOIN %s.%s AS student ON student.Student_ID = a.Student_ID", _database, Constants.DbTables.LESSONS, _database, Constants.DbTables.STUDENTS);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                _allLessons.add(buildLessonFromResultSet(resultSet));
            }
            
            return _allLessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * query adds input lessons to the database
     *
     * @param lesson
     * @return
     */
    public boolean addAppointment(Lesson lesson) {

            try {
                String sql = String.format(
                        "INSERT INTO %s.%s (" +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s, " +
                                "%s) " +
                        "VALUES(" +
                                "?," +
                                "?," +
                                "?," +
                                "?," +
                                "?," +
                                "NOW()," +
                                "'User'," +
                                "NOW()," +
                                "'User'," +
                                "?," +
                                "?)",
                        _database, Constants.DbTables.LESSONS,
                        "Description",
                        "Location",
                        "Type",
                        "Start",
                        "End",
                        "Create_Date",
                        "Created_By",
                        "Last_Update",
                        "Last_Updated_By",
                        "Student_ID",
                        "User_ID"
                );
                
                PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
                int x = 1;
                preparedStatement.setString(x++, lesson.getDescription());
                preparedStatement.setString(x++, lesson.getLocation());
                preparedStatement.setString(x++, lesson.getType());
                preparedStatement.setTimestamp(x++, Timestamp.valueOf(lesson.getStart()));
                preparedStatement.setTimestamp(x++, Timestamp.valueOf(lesson.getEnd()));
                preparedStatement.setInt(x++, lesson.getStudentID());
                preparedStatement.setInt(x++, lesson.getUserID());

                preparedStatement.executeUpdate();
                preparedStatement.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    /**
     * query updates lessons in the database
     *
     * @param lesson
     * @return
     */
    public boolean modifyAppointment(Lesson lesson) {
        try {
            String sql = String.format(
                    "UPDATE %s.%s SET Create_Date=NOW(),Created_By='User',Last_Update=NOW(),Last_Updated_By='User', Description = ?," +
                            "Location = ?,Type = ?,Start = ?,END = ?,Student_ID = ?,User_ID = ? WHERE Lesson_ID = ?;",
                    _database, Constants.DbTables.LESSONS
            );
            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            int x = 1;
            statement.setString(x++, lesson.getDescription());
            statement.setString(x++, lesson.getLocation());
            statement.setString(x++, lesson.getType());
            statement.setTimestamp(x++, Timestamp.valueOf(lesson.getStart()));
            statement.setTimestamp(x++, Timestamp.valueOf(lesson.getEnd()));
            statement.setInt(x++, lesson.getStudentID());
            statement.setInt(x++, lesson.getUserID());
            statement.setInt(x++, lesson.getLessonID());

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
                    _database, Constants.DbTables.LESSONS
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
     * soft deletes an lesson from the db - sets all values to NULL except a few
     *
     * @param lesson
     */
    public void deleteAppointment(Lesson lesson) {
        try {
            String query = String.format(
                    "DELETE FROM %s.%s WHERE Lesson_ID = ?;",
                    _database, Constants.DbTables.LESSONS
            );
            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            statement.setInt(1, lesson.getLessonID());
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * soft deletes all lessons related to a given customer
     *
     * @param customerId
     */
    public void deleteAppointment(int customerId) {
        try {
            String query = String.format(
                    "DELETE FROM %s.%s WHERE %s = ?;",
                    _database, Constants.DbTables.LESSONS, Constants.DbTables.STUDENTS + "_ID"
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
    public ObservableList<Lesson> getWeeklyLessons() {
        ObservableList<Lesson> allLessons = getAllLessons();
        if (allLessons == null) {
            return null;
        }

        LocalDateTime nowTime = LocalDateTime.now();
        ZonedDateTime nowTimeCST = nowTime.atZone(ZoneId.of("America/Chicago"));
        LocalDateTime est = nowTimeCST.toLocalDateTime();
        LocalDateTime startOfWeek = est.with(DayOfWeek.MONDAY);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        return allLessons.stream()
                .filter(lesson -> lesson.getStart().isAfter(startOfWeek) && lesson.getStart().isBefore(endOfWeek))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * gets all lessons in the upcoming month
     *
     * @return
     */
    public ObservableList<Lesson> getMonthlyLessons() {
        ObservableList<Lesson> allLessons = getAllLessons();
        if (allLessons == null) {
            return null;
        }

        LocalDateTime nowTime = LocalDateTime.now();
        ZonedDateTime nowTimeCST = nowTime.atZone(ZoneId.of("America/Chicago"));
        LocalDateTime est = nowTimeCST.toLocalDateTime();
        LocalDateTime startOfMonth = est.withDayOfMonth(1);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        return allLessons.stream()
                .filter(lesson -> lesson.getStart().isAfter(startOfMonth) && lesson.getStart().isBefore(endOfMonth))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    /**
     * gets all unique lessons from the db
     *
     * @return
     */
    public ObservableList<String> getTypes() {
        _allTypes.clear();
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT DISTINCT Type FROM %s.%s;",
                    _database, Constants.DbTables.LESSONS
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
    public int getMonthTypeAsInt(LocalDateTime localDateTime, String type) {
        int returnNumber = 0;
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT * FROM %s.%s;",
                    _database, Constants.DbTables.LESSONS
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
     * gets all lessons related to a given contact
     *
     * @param contact
     * @return
     */
    public ObservableList<Lesson> getStudentLessons(String contact) {
        List<Integer> studentIds = studentsDTO.getStudentIdsFromName(contact);
        for (Integer studentId:studentIds) {
            String query = String.format(
                    "SELECT a.* "
                            + "FROM %s.%s AS a "
                            + "INNER JOIN %s.%s AS student ON student.%s = a.%s "
                            + "WHERE student.%s=%s AND Title IS NOT NULL;",
                    _database, Constants.DbTables.LESSONS,
                    _database, Constants.DbTables.STUDENTS,
                    Constants.DbTables.STUDENTS + "_ID", Constants.DbTables.STUDENTS + "_ID",
                    Constants.DbTables.STUDENTS + "_ID", studentId);
            try(Statement statement = getStatement()) {
                _studentLessons.clear();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    _studentLessons.add(buildLessonFromResultSet(resultSet));
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return _studentLessons;
    }

    /**
     * checks to see if a customer has any lessons scheduled
     *
     * @param selectedStudent
     * @return
     */
    public boolean checkForLessons(Student selectedStudent) {
        int customerID = selectedStudent.getId();
        _studentLessonExists.clear();
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT * FROM %s.%s WHERE Student_ID=%d AND Title IS NOT NULL;",
                    _database, Constants.DbTables.LESSONS, customerID
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
    public Lesson getLessonsNext15Minutes(LocalDateTime now) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = String.format(
                    "SELECT * FROM %s.%s AS a WHERE Start>=? AND Start<=? ORDER BY Start ASC LIMIT 1;",
                    _database, Constants.DbTables.LESSONS
            );
            preparedStatement = JDBC.getConnection().prepareStatement(sql);

            int x = 1;
            preparedStatement.setTimestamp(x++, Timestamp.valueOf(now));
            preparedStatement.setTimestamp(x++, Timestamp.valueOf(now.plusMinutes(15)));

            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }


            return buildLessonFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * gets all taken start times from the db by date and converts those times from UTC to times on the user's system
     * @param localDate
     * @return
     */
    public ObservableList<Lesson> getAllTakenLessonTimesByDate(LocalDate localDate) {
        ObservableList<Lesson> allTakenStartTimes = FXCollections.observableArrayList();
        _allStartTimes.clear();
        try(Statement statement = getStatement()) {
            String sql = String.format(
                    "SELECT * FROM %s.%s WHERE DAYOFMONTH(Start) = DAYOFMONTH('%s');",
                    _database, Constants.DbTables.LESSONS, localDate
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
    private Lesson buildLessonFromResultSet(ResultSet resultSet) throws SQLException {

        Lesson lesson = new Lesson(
                resultSet.getInt("Lesson_ID"),
                resultSet.getString("Description"),
                resultSet.getString("Location"),
                resultSet.getString("Type"),
                resultSet.getTimestamp("Start").toLocalDateTime(),
                resultSet.getTimestamp("End").toLocalDateTime(),
                resultSet.getInt("User_ID"));
        lesson.setStudentId(resultSet.getInt("Student_ID"));
        return lesson;
    }
}
