/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.model;

import automatedattendance.util.Enums.AttendanceRemark;
import automatedattendance.util.Enums.AttendanceStatus;

/**
 *
 * @author danle
 */
public class Attendance {
    private int attendanceId;
    private int studentId;
    private int scheduleId;
    private String date;
    private String timeIn;
    private String timeOut;
    private AttendanceStatus status;
    private AttendanceRemark remarks;
    
    private String studentName;
    private String subjectName;
    
    public Attendance(int attendanceId, int studentId, int scheduleId, String date, String timeIn, String timeOut, AttendanceStatus status, AttendanceRemark remarks ) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.scheduleId = scheduleId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
        this.remarks = remarks;
    }
    
    public Attendance() {}
    
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) {this.attendanceId = attendanceId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTimeIn() { return timeIn; }
    public void setTimeIn(String timeIn) { this.timeIn = timeIn; }

    public String getTimeOut() { return timeOut; }
    public void setTimeOut(String timeOut) { this.timeOut = timeOut; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public AttendanceRemark getRemarks() { return remarks; }
    public void setRemarks(AttendanceRemark remarks) { this.remarks = remarks; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
}
