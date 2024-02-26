package org.scheduler.viewmodels;

public class Student {
    private final int Id;
    private final String name;
    private final String address;
    private String postalCode;
    private String phoneNumber;

    /**
     * constructor for customer
     * @param Id
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     */
    public Student(int Id, String name, String address, String postalCode, String phoneNumber){
        this.Id = Id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter/setter for Student
     * @return
     */
    public int getId() {
        return Id;
    }

public String getName() {
        return name;
    }

    /**
     * getter/setter for Student
     * @return
     */
    public String getAddress() {
        return address;
    }

// 
//// 
////    /**
////     * getter/setter for Student
////     * @param address
////     */
////    public void setAddress(String address) {
// 
//        this.address = address;
//    }
// 

    /**
     * getter/setter for Student
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }
//
//    /**
//     * getter/setter for Student
//     * @param postalCode
//     */
// 
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * getter/setter for Student
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * getter/setter for Student
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
