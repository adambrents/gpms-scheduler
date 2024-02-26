package org.scheduler.repository;

import org.scheduler.repository.configuration.model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.configuration.context.JDBCContext;
import org.scheduler.viewmodels.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentsDTO {

    private final Logger _logger = LoggerFactory.getLogger(StudentsDTO.class);
    private final ObservableList<Student> allStudents = FXCollections.observableArrayList();
    private final ObservableList<String> allCountries = FXCollections.observableArrayList();
    private int lastID = 0;

    /**
     * gets a list of all countries in db
     *
     * @return
     */
    public ObservableList<String> getAllCountries() {
        try{
            allCountries.clear();
            Statement statement = JDBCContext.getStatement();
            String query = "SELECT Country "
                         + "FROM client_schedule.countries;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                allCountries.add(resultSet.getString("Country"));
            }
            return allCountries;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets a list of all customers in the db
     * @return
     */
    public ObservableList<Student> getAllCustomers() {
        try{
            allStudents.clear();
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Countries.Country, "
                         + "first_level_divisions.Division, "
                         + "customers.Customer_ID, "
                         + "customers.Customer_Name, "
                         + "customers.Address, "
                         + "customers.Postal_Code, "
                         + "customers.Phone, "
                         + "customers.Division_ID "
                         + "FROM client_schedule.customers "
                         + "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID "
                         + "INNER JOIN Countries ON first_level_divisions.Country_ID = Countries.Country_ID "
                         + "WHERE Customer_Name IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"));
                if (student.getName() == ""){
                    //do nothing
                }
                else {
                    allStudents.add(student);
                }
            }
            statement.close();
            return allStudents;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * updates a student with new values from input student
     *
     * @param student
     */
    public void updateCustomer(Student student) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(
                    "UPDATE client_schedule.customers "
                      + "SET Customer_Name = '" + student.getName()
                      + "', Address = '" + student.getAddress()
                      + "', Phone = '" + student.getPhoneNumber()
                      + "', Postal_Code = '" + student.getPostalCode()
                      + "' WHERE customers.Customer_ID = " + student.getId() + " AND Customer_Name IS NOT NULL;");
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            _logger.error("Student update failed! Exception: " + e);
        }
    }

    /**
     * gets a new customer id when adding new customers via UI
     *
     * @return
     */
    public int getId() {
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT MAX(Customer_ID) "
                         + "FROM client_schedule.customers;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt(1) > lastID) {
                    lastID = resultSet.getInt(1);
                } else {
                    //do nothing if the id is less than last id
                }
            }
            lastID++;
            statement.close();

            return lastID;
        } catch (SQLException sqlException) {
            return -1;
        }
    }

    /**
     * adds a student to the db
     *
     * @param student
     */
    public void addCustomer(Student student) {
        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(
                    "INSERT INTO customers " +
                        "VALUES(" + student.getId() +
                            ", '" + student.getName() +
                           "', '" + student.getAddress() +
                           "', '" + student.getPostalCode() +
                           "', '" + student.getPhoneNumber() +
                           "', "  + "NULL, "
                                  + "NULL, "
                                  + "NULL, "
                                  + "NULL);");
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            _logger.error("Adding student failed!", sqlException);
        }
    }

    /**
     * gets a customer name based on customer ID
     *
     * @param customerID
     * @return
     */
    public String getCustomerName(int customerID){
        try{
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Customer_Name "
                         + "FROM client_schedule.customers "
                         + "WHERE Customer_ID = " + customerID + " AND Customer_Name IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getString(1);
            }
            statement.close();
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
    public int getCustomerId(String customerName){
        try{
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Customer_ID FROM client_schedule.customers WHERE Customer_Name ='" + customerName + "' AND Customer_Name IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getInt(1);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * soft deletes a customer from the db
     *
     * @param selectedStudent
     */
    public void deleteCustomer(Student selectedStudent){
        try{PreparedStatement statement = JDBC.getConnection().prepareStatement(
                "DELETE FROM client_schedule.customers "
                  + "WHERE Customer_ID = " + selectedStudent.getId() + ";");
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
