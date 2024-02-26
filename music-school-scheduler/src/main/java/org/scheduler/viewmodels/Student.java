package org.scheduler.viewmodels;

public class Student {
    private final int Id;
    private final String firstName;
    private final String lastName;
    private final String addressLine1;
    private final String addressLine2;
    private String postalCode;
    private String phoneNumber;

    /**
     * constructor for customer
     *
     * @param Id
     * @param firstName
     * @param lastName
     * @param addressLine1
     * @param addressLine2
     * @param postalCode
     * @param phoneNumber
     */
    public Student(int Id, String firstName, String lastName, String addressLine1, String addressLine2, String postalCode, String phoneNumber) {
        this.Id = Id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter/setter for Student
     *
     * @return
     */
    public int getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * getter/setter for Student
     *
     * @return
     */
    public String getAddressLine1() {
        return addressLine1;
    }


    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * getter/setter for Student
     *
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * getter/setter for Student
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName(){ return String.format("%s %s", getFirstName(), getLastName()); }
}
