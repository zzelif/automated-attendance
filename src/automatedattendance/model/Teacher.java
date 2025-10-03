/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.model;

/**
 *
 * @author danle
 */
public class Teacher {
    private int teacherId;
    private int userId;
    private String firstName;
    private String lastName;
    private String department;
    
    public Teacher(int teacherId, int userId, String firstName, String lastName, String department) {
        this.teacherId = teacherId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    public Teacher() {}

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
