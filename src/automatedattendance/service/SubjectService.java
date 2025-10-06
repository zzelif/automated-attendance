/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.service;

import automatedattendance.dao.ScheduleDAO;
import automatedattendance.dao.SubjectDAO;
import automatedattendance.model.Subject;
import automatedattendance.model.SubjectSchedule;

import java.time.LocalDateTime;
/**
 *
 * @author danle
 */
public class SubjectService {
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    
    public SubjectSchedule getCurrentSchedule() {
        return scheduleDAO.getCurrentSchedule();
    }
    
    public SubjectSchedule getScheduleForDateTime(LocalDateTime dateTime) {
        return scheduleDAO.getScheduleForDateTime(dateTime);
    }
    
    public String getScheduleLabel(SubjectSchedule schedule) {
        if (schedule == null) {
            return "No active class";
        }
        
        Subject subject = subjectDAO.getSubjectById(schedule.getSubjectId());
        if (subject == null) {
            return "Unknown subject";
        }
        
        return String.format(
            "%s - %s | %s-%s",
            subject.getSubjectCode(),
            subject.getSubjectName(),
            schedule.getStartTime(),
            schedule.getEndTime()
        );
    }
}
