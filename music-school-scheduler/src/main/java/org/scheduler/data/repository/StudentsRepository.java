package org.scheduler.data.repository;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.factory.DTOFactory;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.scheduler.data.repository.properties.BookRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;
import org.scheduler.data.repository.properties.LevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentsRepository extends BaseRepository<StudentDTO> {

    private final Logger _logger = LoggerFactory.getLogger(StudentsRepository.class);
    private final ObservableList<StudentDTO> allStudentDTOS = FXCollections.observableArrayList();
    private final ObservableList<String> allCountries = FXCollections.observableArrayList();
    private int lastID = 0;

    /**
     * gets a list of all students in the db
     * @return
     */
    @Override
    public ObservableList<StudentDTO> getAllItems() {
        return FXCollections.observableArrayList(super.getAllItemsFromType(new StudentDTO()));
    }
    @Override
    public void updateItem(StudentDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(StudentDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(StudentDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }
    /**
     * gets a new customer id when adding new students via UI
     *
     * @return
     */
    public int getLastStudentId() {
        try(Statement statement = getStatement()){
            String query = String.format(
                    "SELECT MAX(Student_ID) "
                            + "FROM %s.%s;",
                    _database,
                    DB_TABLES.STUDENTS
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
     * gets a customer name based on customer ID
     *
     * @param customerID
     * @return
     */
    public String getStudentNameFromId(int customerID){
        try(Statement statement = getStatement()){
            String query = String.format(
                    "SELECT Student_First_Name + ' ' + Student_Last_Name "
                            + "FROM %s.%s "
                            + "WHERE Student_ID = %s;",
                    _database,
                    DB_TABLES.STUDENTS,
                    customerID
            );

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getString(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * gets a customer ID based on customer name
     *
     * @param customerName
     * @return
     */
    public List<Integer> getStudentIdsFromName(String customerName){
        List<Integer> studentIds = new ArrayList<>();
        String query = String.format(
                "SELECT Student_ID FROM %s.%s WHERE Student_First_Name = '%s' AND Student_First_Name IS NOT NULL;",
                _database,
                DB_TABLES.STUDENTS,
                customerName
        );
        try(Statement statement = getStatement()){


            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                studentIds.add(resultSet.getInt("Student_ID"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentIds;
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }
    public void insertNewStudent(StudentDTO newStudent) {
        try {
            insertItem(newStudent, JDBC.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateStudent(StudentDTO newStudent) throws SQLException {
        if (newStudent.getId() == 0) {
            throw new RuntimeException("Cannot update a student without a valid Student_ID!");
        }

        Connection connection = null;
        try {
            connection = JDBC.getConnection();
            connection.setAutoCommit(false);

            super.update(newStudent, connection);



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

    private <T extends DTOMappingBase> void updateMappingsForPropertyType(List<T> newPropertyList,
                                                                          List<DTOMappingBase> existingMappings, Constants.MAPPINGS mappingNeeded, Connection connection) throws SQLException {
        for (T newProperty : newPropertyList) {
            Optional<DTOMappingBase> existingMappingOptional = existingMappings.stream()
                    .filter(mapping -> mapping.getMapping() == mappingNeeded && mapping.getMappingToId() == newProperty.getMappingToId())
                    .findFirst();

            if (newProperty.getSelected().get() && !existingMappingOptional.isPresent()) {
                insert(newProperty, connection);
            } else if (!newProperty.getSelected().get() && existingMappingOptional.isPresent()) {
                DTOMappingBase mappingToDelete = existingMappingOptional.get();
                delete(mappingToDelete, connection);
            }
        }
    }

    public List<DTOMappingBase> getIdsForStudentProperties(int studentId){
        List<DTOMappingBase> dtos = new ArrayList<>();
        String query =
                "SELECT Student_Level_Id AS Key_Value, Student_Id, Level_Id AS MapToProperty, " +
                "'StudentLevel' AS Type_Value " +
                "FROM student_level " +
                "WHERE Student_Id = " + studentId +
                " UNION ALL " +
                "SELECT Student_Teacher_Id AS Key_Value, Student_Id, Teacher_Id AS MapToProperty, " +
                "'StudentTeacher' AS Type_Value " +
                "FROM student_teacher " +
                "WHERE Student_Id = " + studentId +
                " UNION ALL " +
                "SELECT Student_Book_Id AS Key_Value, Student_Id, Book_Id AS MapToProperty, " +
                "'StudentBook' AS Type_Value " +
                "FROM student_book " +
                "WHERE Student_Id = " + studentId +
                " UNION ALL " +
                "SELECT Student_Instrument_Id AS Key_Value, Student_Id, Instrument_Id AS MapToProperty, " +
                "'StudentInstrument' AS Type_Value " +
                "FROM student_instrument " +
                "WHERE Student_Id = " + studentId ;

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String typeValue = rs.getString("Type_Value");
                int keyValue = rs.getInt("Key_Value");
                DTOMappingBase<?> dto = DTOFactory.createMappingDTO(Constants.MAPPINGS.valueOf(typeValue), rs.getInt("Student_Id"), rs.getInt("MapToProperty"), new SimpleBooleanProperty());
                dto.setId(keyValue);
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dtos;
    }

    public List<DTOMappingBase> getAllMappings(){
        List<DTOMappingBase> dtos = new ArrayList<>();
        String query =
                "SELECT Student_Level_Id AS Key_Value, Student_Id, Level_Id AS MapToProperty, " +
                        "'StudentLevel' AS Type_Value " +
                        "FROM student_level " +
                        " UNION ALL " +
                        "SELECT Student_Teacher_Id AS Key_Value, Student_Id, Teacher_Id AS MapToProperty, " +
                        "'StudentTeacher' AS Type_Value " +
                        "FROM student_teacher " +
                        " UNION ALL " +
                        "SELECT Student_Book_Id AS Key_Value, Student_Id, Book_Id AS MapToProperty, " +
                        "'StudentBook' AS Type_Value " +
                        "FROM student_book " +
                        " UNION ALL " +
                        "SELECT Student_Instrument_Id AS Key_Value, Student_Id, Instrument_Id AS MapToProperty, " +
                        "'StudentInstrument' AS Type_Value " +
                        "FROM student_instrument ";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String typeValue = rs.getString("Type_Value");
                int keyValue = rs.getInt("Key_Value");
                DTOMappingBase<?> dto = DTOFactory.createMappingDTO(Constants.MAPPINGS.valueOf(typeValue), rs.getInt("Student_Id"), rs.getInt("MapToProperty"), new SimpleBooleanProperty());
                dto.setId(keyValue);
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dtos;
    }

    public void deleteStudent(StudentDTO studentDTO, Connection connection) throws SQLException {
        connection.setAutoCommit(false);

        delete(studentDTO, connection);

        connection.commit();
    }

    public String getStudentInstrumentsAsString(int id) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "i.Name " +
                    "FROM student_instrument AS ti " +
                    "INNER JOIN instruments AS i ON i.Instrument_Id = ti.Instrument_Id " +
                    "WHERE ti.Student_id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            return toString(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getStudentLevelsAsString(int id) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "i.Name " +
                    "FROM student_level AS ti " +
                    "INNER JOIN levels AS i ON i.Level_Id = ti.Level_Id " +
                    "WHERE ti.Student_id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            return toString(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getStudentBooksAsString(int id) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "i.Name " +
                    "FROM student_book AS ti " +
                    "INNER JOIN books AS i ON i.Book_Id = ti.Book_Id " +
                    "WHERE ti.Student_id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            return toString(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getStudentTeachersAsString(int id) {
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "i.Teacher_First_Name AS Name " +
                    "FROM student_teacher AS ti " +
                    "INNER JOIN teachers AS i ON i.Teacher_Id = ti.Teacher_Id " +
                    "WHERE ti.Student_Id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            return toString(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public ObservableList<StudentDTO> getAllStudentData() {
        final TeachersRepository teachersRepository = new TeachersRepository();
        final BookRepository bookRepository = new BookRepository();
        final InstrumentRepository instrumentRepository = new InstrumentRepository();
        final LevelRepository levelRepository = new LevelRepository();
        Map<Integer, TeacherDTO> teacherMap = teachersRepository.getAllItems().stream().collect(Collectors.toMap(TeacherDTO::getId, Function.identity()));
        Map<Integer, InstrumentDTO> instrumentMap = instrumentRepository.getAllItems().stream().collect(Collectors.toMap(InstrumentDTO::getId, Function.identity()));
        Map<Integer, LevelDTO> levelMap = levelRepository.getAllItems().stream().collect(Collectors.toMap(LevelDTO::getId, Function.identity()));
        Map<Integer, BookDTO> bookMap = bookRepository.getAllItems().stream().collect(Collectors.toMap(BookDTO::getId, Function.identity()));

        ObservableList<StudentDTO> allStudents = this.getAllItems();

        List<DTOMappingBase> studentMappings = new ArrayList<>(this.getAllMappings());

        for (StudentDTO student : allStudents) {
            List<TeacherDTO> studentTeachers = new ArrayList<>();
            List<InstrumentDTO> studentInstruments = new ArrayList<>();
            List<LevelDTO> studentLevels = new ArrayList<>();
            List<BookDTO> studentBooks = new ArrayList<>();

            studentMappings.stream()
                    .filter(mapping -> mapping.getMappingFromId() == student.getId())
                    .forEach(mapping -> {
                        switch (mapping.getMapping()) {
                            case StudentTeacher -> studentTeachers.add(teacherMap.get(mapping.getMappingToId()));
                            case StudentInstrument ->
                                    studentInstruments.add(instrumentMap.get(mapping.getMappingToId()));
                            case StudentLevel -> studentLevels.add(levelMap.get(mapping.getMappingToId()));
                            case StudentBook -> studentBooks.add(bookMap.get(mapping.getMappingToId()));
                        }
                    });

            // Set the properties on the student DTO
            student.setTeachers(studentTeachers);
            student.setInstruments(studentInstruments);
            student.setLevels(studentLevels);
            student.setBooks(studentBooks);
        }
        return allStudents;
    }

    private String toString(ResultSet resultSet) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        while (resultSet.next()) {
            stringBuilder.append(resultSet.getString("Name"));
            stringBuilder.append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }
}
