package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.reports.NumberStudentsInstrumentsDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.data.configuration.JDBC.getConnection;

public class InstrumentRepository extends BaseRepository<InstrumentDTO> {
    private final Logger _logger = LoggerFactory.getLogger(InstrumentRepository.class);
    @Override
    public ObservableList<InstrumentDTO> getAllItems() {
        return FXCollections.observableArrayList(super.getAllItemsFromType(new InstrumentDTO()));
    }

    @Override
    public void updateItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }

    public void performAction(InstrumentDTO instrumentDTO, String action) {
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }

    public List<InstrumentDTO> getInstrumentsByTeacherStudent(int studentId, int teacherId) {
        List<InstrumentDTO> teacherInstrumentMappings = new ArrayList<>();
        String sql = "SELECT DISTINCT " +
                "instruments.Instrument_Id, " +
                "instruments.Name " +
                "FROM instruments " +
                "INNER JOIN student_instrument ON student_instrument.Instrument_Id = instruments.Instrument_Id " +
                "INNER JOIN teacher_instrument ON teacher_instrument.Instrument_Id = student_instrument.Instrument_Id " +
                "WHERE Student_Id = ? AND Teacher_Id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, teacherId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    teacherInstrumentMappings.add(new InstrumentDTO().fromResultSet(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherInstrumentMappings;
    }
    public List<NumberStudentsInstrumentsDTO> getStudentsPerInstrumentReport() {
        List<NumberStudentsInstrumentsDTO> numberStudentsInstruments = new ArrayList<>();
        try(Statement statement = getStatement()) {
            String sql = "SELECT " +
                    "COUNT(1) AS Student_Count, " +
                    "Name AS Instrument_Name " +
                    "FROM instruments AS i " +
                    "INNER JOIN student_instrument AS si ON si.Instrument_Id = i.Instrument_Id " +
                    "GROUP BY i.Name;";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                numberStudentsInstruments.add(new NumberStudentsInstrumentsDTO().fromResultSet(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberStudentsInstruments;
    }

}
