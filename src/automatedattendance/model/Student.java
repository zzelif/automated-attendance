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
public class Student {
    private int studentId;
    private String studentNumber;
    private int userId;
    private String firstName;
    private String lastName;
    private String course;
    private int yearLevel;

    public Student(int studentId, String studentNumber, int userId,
                   String firstName, String lastName, String course, int yearLevel) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.yearLevel = yearLevel;
    }

    public Student() {}

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }
}
