package org.scheduler.data.repository;

import org.scheduler.app.configuration.model.BusinessHours;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.utilities.GsonHelper;
import org.scheduler.data.dto.*;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.factory.DTOFactory;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.mapping.StudentBookDTO;
import org.scheduler.data.dto.mapping.StudentInstrumentDTO;
import org.scheduler.data.dto.mapping.StudentLevelDTO;
import org.scheduler.data.dto.mapping.StudentTeacherDTO;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.configuration.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.repository.properties.BookRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;
import org.scheduler.data.repository.properties.LevelRepository;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.scheduler.app.constants.Constants.APP_CONFIG;
import static org.scheduler.data.configuration.JDBC.getConnection;

public class LessonsRepository extends BaseRepository<LessonDTO> {

    private final ObservableList<LocalDateTime> _allStartTimes = FXCollections.observableArrayList();
    private final ObservableList<LessonDTO> _studentLessonDTOS = FXCollections.observableArrayList();
    private final ObservableList<String> _allTypes = FXCollections.observableArrayList();
    private final StudentsRepository studentsRepository = new StudentsRepository();

    /**
     * query returns a list of all lessons in database
     *
     * @return
     */
    @Override
    public ObservableList<LessonDTO> getAllItems() {
        return FXCollections.observableArrayList(super.getAllItemsFromType(new LessonDTO()));
    }
    @Override
    public void updateItem(LessonDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(LessonDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(LessonDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
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
     * gets all lessons in the upcoming week
     *
     * @return
     */
    public ObservableList<LessonDTO> getWeeklyLessons() {
        ObservableList<LessonDTO> allLessonDTOS = getAllLessonData();
        if (allLessonDTOS == null) {
            return null;
        }

        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime startOfWeek = nowTime.with(DayOfWeek.MONDAY);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        return allLessonDTOS
                .stream()
                .filter(lesson -> lesson.getScheduledLesson().getDayOfWeek().getValue() > startOfWeek.getDayOfWeek().getValue() &&
                                  lesson.getScheduledLesson().getDayOfWeek().getValue() < endOfWeek.getDayOfWeek().getValue())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<LessonDTO> getAllLessonData() {
        final TeachersRepository teachersRepository = new TeachersRepository();
        final BookRepository bookRepository = new BookRepository();
        final InstrumentRepository instrumentRepository = new InstrumentRepository();
        final LevelRepository levelRepository = new LevelRepository();
        ObservableList<LessonDTO> allLessonData = getAllItems();
        if (allLessonData == null) {
            return null;
        }
        Map<Integer, TeacherDTO> teachersMap = teachersRepository.getAllItems().stream()
                .collect(Collectors.toMap(TeacherDTO::getId, Function.identity()));
        Map<Integer, StudentDTO> studentsMap = studentsRepository.getAllStudentData().stream()
                .collect(Collectors.toMap(StudentDTO::getId, Function.identity()));
        Map<Integer, InstrumentDTO> instrumentsMap = instrumentRepository.getAllItems().stream()
                .collect(Collectors.toMap(InstrumentDTO::getId, Function.identity()));
        Map<Integer, LevelDTO> levelsMap = levelRepository.getAllItems().stream()
                .collect(Collectors.toMap(LevelDTO::getId, Function.identity()));
        Map<Integer, BookDTO> booksMap = bookRepository.getAllItems().stream()
                .collect(Collectors.toMap(BookDTO::getId, Function.identity()));

        List<LessonScheduledDTO> allScheduledLessons = super.getAllItemsFromType(new LessonScheduledDTO());

        for (LessonDTO lessonDTO : allLessonData) {
            for (LessonScheduledDTO scheduledDTO : allScheduledLessons) {
                if (lessonDTO.getId() == scheduledDTO.getLessonId()) {

                    scheduledDTO.setInstrument(instrumentsMap.get(scheduledDTO.getInstrument().getId()));
                    scheduledDTO.setLevel(levelsMap.get(scheduledDTO.getLevel().getId()));
                    scheduledDTO.setBook(booksMap.get(scheduledDTO.getBook().getId()));

                    lessonDTO.setScheduledLesson(scheduledDTO);
                    break;
                }
            }
            lessonDTO.setTeacher(teachersMap.get(lessonDTO.getTeacher().getId()));
            lessonDTO.setStudent(studentsMap.get(lessonDTO.getStudent().getId()));
        }

        return allLessonData
                .stream()
                .filter(lesson -> lesson.getScheduledLesson() != null)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }

    public void updateLesson(LessonDTO newLesson, Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint("BeforeUpdatingLesson");

        try {
            updateItem(newLesson, connection);

            update(newLesson.getScheduledLesson(), connection);

            List<DTOMappingBase> existingMappings = studentsRepository.getIdsForStudentProperties(newLesson.getStudent().getId());

            insertMappings(new StudentTeacherDTO(newLesson.getStudent().getId(), newLesson.getTeacher().getId()), connection, existingMappings);
            insertMappings(new StudentInstrumentDTO(newLesson.getStudent().getId(), newLesson.getScheduledLesson().getInstrument().getId()), connection, existingMappings);
            insertMappings(new StudentBookDTO(newLesson.getStudent().getId(), newLesson.getScheduledLesson().getBook().getId()), connection, existingMappings);
            insertMappings(new StudentLevelDTO(newLesson.getStudent().getId(), newLesson.getScheduledLesson().getLevel().getId()), connection, existingMappings);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(savepoint);
                    System.err.println("Transaction is being rolled back");
                } catch(SQLException excep) {

                }
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    private <T extends DTOMappingBase> void insertMappings(T prospectiveMapping, Connection connection, List<T> existingMappings){
        Predicate<T> isMatchingInstrument = item -> item.getMappingToId() == prospectiveMapping.getMappingToId();
        Predicate<T> isStudentInstrumentMapping = item -> item.getMapping() == prospectiveMapping.getMapping();

        List<T> mappings = existingMappings.stream()
                .filter(isMatchingInstrument.and(isStudentInstrumentMapping))
                .toList();

        if(!mappings.contains(prospectiveMapping.getMappingToId())){
            insert(prospectiveMapping, connection);
        }
    }

    public void addLesson(LessonDTO newLesson, Connection connection) {
        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavepoint("BeforeAddingLesson");
            connection.setAutoCommit(false);
            int lessonKey = insertReturnGeneratedKey(newLesson, connection);

            if (lessonKey == 0) {
                throw new SQLException("Creating lesson failed, no key obtained.");
            }

            newLesson.getScheduledLesson().setLessonId(lessonKey);

            int scheduledLessonKey = insertReturnGeneratedKey(newLesson.getScheduledLesson(), connection);

            if (scheduledLessonKey == 0) {
                throw new SQLException("Creating scheduled lesson failed, no key obtained.");
            }

            insert(new LessonScheduledMstDTO(scheduledLessonKey), connection);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(savepoint);
                    System.err.println("Transaction is being rolled back");
                } catch(SQLException excep) {

                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public List<LocalTime> getPossibleStartTimesForTeacherAndStudent(int studentId, int teacherId, DayOfWeek dayOfWeek) {
        List<LocalTime> startTimes = new ArrayList<>();

        BusinessHours businessHours = APP_CONFIG.getBusinessHours();

        String query = """
        SELECT 
            ADDTIME(?, SEC_TO_TIME(a.a * 1800)) AS time_slot
        FROM 
            (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS a
        WHERE 
            ADDTIME(?, SEC_TO_TIME(a.a * 1800)) < ?
            AND NOT EXISTS (
                SELECT 1
                FROM lessons_scheduled ls
                INNER JOIN lessons l ON ls.Lesson_ID = l.Lesson_ID
                WHERE (ls.Start <= ADDTIME(?, SEC_TO_TIME(a.a * 1800)) AND ls.End > ADDTIME(?, SEC_TO_TIME(a.a * 1800)))
                AND (l.Teacher_ID = ? OR l.Student_ID = ?)
                AND ls.DayOfWeek = ?
            )
        ORDER BY time_slot;
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, businessHours.getOpen().toString());
            pstmt.setString(2, businessHours.getOpen().toString());
            pstmt.setString(3, businessHours.getClose().toString());
            pstmt.setString(4, businessHours.getOpen().toString());
            pstmt.setString(5, businessHours.getOpen().toString());
            pstmt.setInt(6, teacherId);
            pstmt.setInt(7, studentId);
            pstmt.setString(8, dayOfWeek.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalTime timeSlot = rs.getTime("time_slot").toLocalTime();
                    startTimes.add(timeSlot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return startTimes;
    }
}
