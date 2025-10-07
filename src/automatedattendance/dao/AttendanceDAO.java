/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;
import automatedattendance.model.Attendance;
import automatedattendance.util.Enums.AttendanceRemark;
import automatedattendance.util.Enums.AttendanceStatus;
import static automatedattendance.util.Enums.parseRemark;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danle
 */
public class AttendanceDAO {

    // Time In
    public boolean logTimeIn(Attendance attendance) {
        final String sql = "INSERT INTO attendance (student_id, schedule_id, date, time_in, status, remarks) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance.getStudentId());
            stmt.setInt(2, attendance.getScheduleId());
            stmt.setDate(3, Date.valueOf(attendance.getDate()));
            stmt.setTime(4, attendance.getTimeIn() != null ? Time.valueOf(attendance.getTimeIn()) : null);
            stmt.setString(5, attendance.getStatus().name());
            stmt.setString(6, attendance.getRemarks().getDbValue());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean logTimeOut(int studentId, int scheduleId, LocalDate date, LocalTime timeOut) {
        final String sql = "UPDATE attendance SET time_out = ? " +
                           "WHERE student_id = ? AND schedule_id = ? AND date = ? AND time_out IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTime(1, Time.valueOf(timeOut));
            stmt.setInt(2, studentId);
            stmt.setInt(3, scheduleId);
            stmt.setDate(4, Date.valueOf(date));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean hasTimeIn(int studentId, int scheduleId, String date) {
        final String sql = "SELECT COUNT(*) FROM attendance " +
                           "WHERE student_id = ? AND schedule_id = ? AND date = ? AND time_in IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, scheduleId);
            stmt.setDate(3, Date.valueOf(LocalDate.parse(date)));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Attendance getAttendanceByStudentScheduleDate(int studentId, int scheduleId, String date) {
        final String sql = "SELECT * FROM attendance WHERE student_id = ? AND schedule_id = ? AND date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, scheduleId);
            stmt.setDate(3, Date.valueOf(LocalDate.parse(date)));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Attendance(
                    rs.getInt("attendance_id"),
                    rs.getInt("student_id"),
                    rs.getInt("schedule_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time_in") != null ? rs.getTime("time_in").toLocalTime() : null,
                    rs.getTime("time_out") != null ? rs.getTime("time_out").toLocalTime() : null,
                    AttendanceStatus.valueOf(rs.getString("status")),
                    parseRemark(rs.getString("remarks"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Attendance> getAttendanceBySchedule(int scheduleId) {
        final List<Attendance> records = new ArrayList<>();
        final String sql = "SELECT a.*, s.first_name, s.last_name " +
                           "FROM attendance a " +
                           "JOIN students s ON a.student_id = s.student_id " +
                           "WHERE a.schedule_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Attendance att = new Attendance(
                    rs.getInt("attendance_id"),
                    rs.getInt("student_id"),
                    rs.getInt("schedule_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time_in") != null ? rs.getTime("time_in").toLocalTime() : null,
                    rs.getTime("time_out") != null ? rs.getTime("time_out").toLocalTime() : null,
                    AttendanceStatus.valueOf(rs.getString("status")),
                    parseRemark(rs.getString("remarks"))
                );
                
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                att.setStudentName(fullName);
                
                records.add(att);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    public List<Attendance> getAttendanceByScheduleAndDate(int scheduleId, LocalDate date) {
        final List<Attendance> records = new ArrayList<>();
        final String sql = "SELECT a.*, s.first_name, s.last_name " +
                           "FROM attendance a " +
                           "JOIN students s ON a.student_id = s.student_id " +
                           "WHERE a.schedule_id = ? AND a.date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.setDate(2, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Attendance att = new Attendance(
                    rs.getInt("attendance_id"),
                    rs.getInt("student_id"),
                    rs.getInt("schedule_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time_in") != null ? rs.getTime("time_in").toLocalTime() : null,
                    rs.getTime("time_out") != null ? rs.getTime("time_out").toLocalTime() : null,
                    AttendanceStatus.valueOf(rs.getString("status").toUpperCase()),
                    AttendanceRemark.valueOf(rs.getString("remarks").replace(" ", "_"))
                );
                
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                att.setStudentName(fullName);
                
                records.add(att);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    public List<LocalDate> getAvailableDatesBySchedule(int scheduleId) {
        List<LocalDate> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT date FROM attendance WHERE schedule_id = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dates.add(rs.getDate("date").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    
    public boolean insertAbsent(Attendance attendance) {
        final String sql = "INSERT INTO attendance (student_id, schedule_id, date, status, remarks) " +
                           "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance.getStudentId());
            stmt.setInt(2, attendance.getScheduleId());
            stmt.setDate(3, Date.valueOf(attendance.getDate()));
            stmt.setString(4, attendance.getStatus().name());
            stmt.setString(5, attendance.getRemarks().getDbValue());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateStatus(int attendanceId, String status) {
        final String sql = "UPDATE attendance SET status = ? WHERE attendance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.toUpperCase());
            stmt.setInt(2, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateRemarks(int attendanceId, String remark) {
        final String sql = "UPDATE attendance SET remarks = ? WHERE attendance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, remark);
            stmt.setInt(2, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    // Delete attendance (para lang sa delete ng crud, for demo sa teacher)
    public boolean deleteAttendance(int attendanceId) {
        final String sql = "DELETE FROM attendance WHERE attendance_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendanceId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
