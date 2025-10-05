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
        STUDENT,
        TEACHER
    }
    
    public enum AttendanceStatus {
        PRESENT,
        ABSENT
    }
    
    public enum AttendanceRemark {
        ON_TIME,
        LATE,
        LEFT_EARLY,
        NO_RECORD,
        EXCUSED
    }
    
    public enum SubjectScheduleDayOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
    
    public static AttendanceRemark parseRemark(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return AttendanceRemark.NO_RECORD;
        }

        try {
            return AttendanceRemark.valueOf(
                dbValue.trim().toUpperCase().replace(" ", "_")
            );
        } catch (IllegalArgumentException e) {
            System.err.println("WARNING: Unknown remark '" + dbValue + "' — defaulting to NO_RECORD");
            return AttendanceRemark.NO_RECORD;
        }
    }

    
}
