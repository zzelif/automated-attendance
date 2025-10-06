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
        ON_TIME("ON TIME"),
        LATE("LATE"),
        LEFT_EARLY("LEFT EARLY"),
        ON_TIME_AND_LEFT_EARLY("ON TIME AND LEFT EARLY"),
        LATE_AND_LEFT_EARLY("LATE AND LEFT EARLY"),
        NO_RECORD("NO RECORD"),
        EXCUSED("EXCUSED");

        private final String dbValue;

        AttendanceRemark(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        public static AttendanceRemark fromDbValue(String dbValue) {
            if (dbValue == null) return NO_RECORD;
            for (AttendanceRemark remark : values()) {
                if (remark.dbValue.equalsIgnoreCase(dbValue.trim())) {
                    return remark;
                }
            }
            return NO_RECORD;
        }
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
