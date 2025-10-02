/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.model;

import automatedattendance.util.Enums.SubjectScheduleDayOfWeek;
/**
 *
 * @author danle
 */
public class SubjectSchedule {
    private int scheduleId;
    private int subjectId;
    private SubjectScheduleDayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private String room;
    
    public SubjectSchedule(int scheduleId, int subjectId, SubjectScheduleDayOfWeek dayOfWeek, String startTime, String endTime, String room) {
        this.scheduleId = scheduleId;
        this.subjectId = subjectId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }
    
    public SubjectSchedule() {}
    
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public SubjectScheduleDayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(SubjectScheduleDayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
