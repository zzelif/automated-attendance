/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.util;

/**
 *
 * @author danle
 */
public class Enums {
    public enum UserRole {
        student,
        teacher
    }
    
    public enum AttendanceStatus {
        present,
        absent
    }
    
    public enum AttendanceRemark {
        on_time,
        late,
        left_early,
        no_record,
        excused
    }
    
    public enum SubjectScheduleDayOfWeek {
        monday,
        tuesday,
        wednesday,
        thursday,
        friday,
        saturday
    }
}
