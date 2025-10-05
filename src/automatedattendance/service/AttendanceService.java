/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.service;

import automatedattendance.dao.*;
import automatedattendance.model.*;
import automatedattendance.util.Enums.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
/**
 *
 * @author danle
 */
public class AttendanceService {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final StudentsDAO studentsDAO = new StudentsDAO();
    private final EnrollmentsDAO enrollmentsDAO = new EnrollmentsDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
//    private final SubjectsDAO subjectsDAO = new SubjectsDAO();
    
    public String logTimeIn(User currentUser, String studentNumber) {
        Student student = studentsDAO.getStudentByNumber(studentNumber);
        SubjectSchedule currentSchedule = scheduleDAO.getCurrentSchedule();
        
        if (student == null) return "Student not found.";
        if (currentSchedule == null) return "No active class schedule at the moment.";
        
        if(student.getUserId() != currentUser.getUserId()) 
            return "You can only log your own attendance.";
        
        int subjectId = currentSchedule.getSubjectId();
        if (!enrollmentsDAO.isStudentEnrolledInSubject(student.getStudentId(), subjectId)) {
            return "You are not enrolled in this subject.";
        }
        
        LocalDate today = LocalDate.now();
        if (attendanceDAO.hasTimeIn(student.getStudentId(), currentSchedule.getScheduleId(), today.toString())) {
            return "You have already timed in.";
        }
        
        Attendance attendance = new Attendance();
        attendance.setStudentId(student.getStudentId());
        attendance.setScheduleId(currentSchedule.getScheduleId());
        attendance.setDate(today);
        attendance.setTimeIn(LocalTime.now());
        attendance.setStatus(AttendanceStatus.PRESENT);
        
        LocalTime startTime = LocalTime.parse(currentSchedule.getStartTime());
        attendance.setRemarks(LocalTime.now().isAfter(startTime) ? AttendanceRemark.LATE : AttendanceRemark.ON_TIME);

        return attendanceDAO.logTimeIn(attendance) ? "Time-in successful!" : "Error logging time in.";
    }
    
    public String logTimeOut(User currentUser, String studentNumber) {
        Student student = studentsDAO.getStudentByNumber(studentNumber);
        SubjectSchedule currentSchedule = scheduleDAO.getCurrentSchedule();

        if (student == null) return "Student not found.";
        if (currentSchedule == null) return "No active class schedule right now.";
        if (student.getUserId() != currentUser.getUserId()) 
            return "You can only log your own attendance.";

        LocalDate today = LocalDate.now();
        boolean success = attendanceDAO.logTimeOut(
                student.getStudentId(),
                currentSchedule.getScheduleId(),
                today.toString(),
                LocalTime.now().toString()
        );

        if (success) {
            
            LocalTime endTime = LocalTime.parse(currentSchedule.getEndTime());
            if (LocalTime.now().isBefore(endTime)) {
                Attendance att = attendanceDAO.getAttendanceByStudentScheduleDate(
                        student.getStudentId(), currentSchedule.getScheduleId(), today.toString());
                if (att != null) {
                    attendanceDAO.updateRemarks(att.getAttendanceId(), AttendanceRemark.LEFT_EARLY.name());
                }
            }
            return "Time-out successful!";
        }
        return "Error logging time out.";
    }
    
    public List<Attendance> getAttendanceForTeacher(int scheduleId, LocalDate date) {
        if (date != null) {
            return attendanceDAO.getAttendanceByScheduleAndDate(scheduleId, date);
        }
        return attendanceDAO.getAttendanceBySchedule(scheduleId);
    }
    
    // Automatic na marking after every schedule
    public void markAbsentees(SubjectSchedule schedule) {
        LocalDate today = LocalDate.now();
        List<Student> enrolledStudents = studentsDAO.getStudentsBySubject(schedule.getSubjectId());

        for (Student s : enrolledStudents) {
            Attendance att = attendanceDAO.getAttendanceByStudentScheduleDate(
                    s.getStudentId(),
                    schedule.getScheduleId(),
                    today.toString()
            );
            if (att == null) {
                Attendance absent = new Attendance(
                        0,
                        s.getStudentId(),
                        schedule.getScheduleId(),
                        today,
                        null,
                        null,
                        AttendanceStatus.ABSENT,
                        AttendanceRemark.NO_RECORD
                );
                attendanceDAO.insertAbsent(absent);
            }
        }
    }
    
    public boolean updateRemarks(int attendanceId, AttendanceRemark remark) {
        return attendanceDAO.updateRemarks(attendanceId, remark.name());
    }

    public boolean deleteAttendance(int attendanceId) {
        return attendanceDAO.deleteAttendance(attendanceId);
    }

}
