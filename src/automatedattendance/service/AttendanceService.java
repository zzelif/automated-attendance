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
        LocalTime graceTime = startTime.plusMinutes(30);
        LocalTime now = LocalTime.now();
        attendance.setRemarks(now.isAfter(graceTime) ? AttendanceRemark.LATE : AttendanceRemark.ON_TIME);

        return attendanceDAO.logTimeIn(attendance) ? "Time-in successful!" : "Error logging time in.";
    }
    
    public String logTimeOut(User currentUser, String studentNumber) {
        Student student = studentsDAO.getStudentByNumber(studentNumber);
        SubjectSchedule currentSchedule = scheduleDAO.getCurrentSchedule();

        if (student == null) return "Student not found.";
        if (currentSchedule == null) return "No active class schedule right now.";
        if (student.getUserId() != currentUser.getUserId()) 
            return "You can only log your own attendance.";

        int subjectId = currentSchedule.getSubjectId();
        if (!enrollmentsDAO.isStudentEnrolledInSubject(student.getStudentId(), subjectId)) {
            return "You are not enrolled in this subject.";
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Attendance existing = attendanceDAO.getAttendanceByStudentScheduleDate(
            student.getStudentId(),
            currentSchedule.getScheduleId(),
            today.toString()
        );

        if (existing == null) {
            return "You need to time in first before timing out.";
        }

        boolean success = attendanceDAO.logTimeOut(
            student.getStudentId(),
            currentSchedule.getScheduleId(),
            today,
            now
        );

        if (success) {
            LocalTime endTime = LocalTime.parse(currentSchedule.getEndTime());
            boolean leftEarly = now.isBefore(endTime);

            // Determine new remark
            String newRemark;
            if (leftEarly) {
                if (existing.getRemarks() == AttendanceRemark.LATE) {
                    newRemark = AttendanceRemark.LATE_AND_LEFT_EARLY.getDbValue();
                } else if (existing.getRemarks() == AttendanceRemark.ON_TIME) {
                    newRemark = AttendanceRemark.ON_TIME_AND_LEFT_EARLY.getDbValue();
                } else {
                    newRemark = AttendanceRemark.LEFT_EARLY.getDbValue();
                }
            } else {
                newRemark = existing.getRemarks().getDbValue();
            }
            attendanceDAO.updateRemarks(existing.getAttendanceId(), newRemark);

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

                boolean success = attendanceDAO.insertAbsent(absent);
                if (!success) {
                    System.err.println("Failed to insert absent for student_id: " + s.getStudentId());
                }
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
