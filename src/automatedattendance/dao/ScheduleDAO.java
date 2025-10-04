/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;
import automatedattendance.model.SubjectSchedule;
import automatedattendance.util.Enums.SubjectScheduleDayOfWeek;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author danle
 */
public class ScheduleDAO {
    public SubjectSchedule getCurrentSchedule() {
        final String sql = "SELECT * FROM subject_schedule " +
                "WHERE day_of_week = DAYNAME(CURDATE()) " +
                "AND CURTIME() BETWEEN start_time AND end_time LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SubjectSchedule(
                        rs.getInt("schedule_id"),
                        rs.getInt("subject_id"),
                        SubjectScheduleDayOfWeek.valueOf(rs.getString("day_of_week").toUpperCase()),
                        rs.getTime("start_time").toLocalTime().toString(),
                        rs.getTime("end_time").toLocalTime().toString(),
                        rs.getString("room")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public List<SubjectSchedule> getSchedulesBySubject(int subjectId) {
        final List<SubjectSchedule> schedules = new ArrayList<>();
        final String sql = "SELECT * FROM subject_schedule WHERE subject_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(new SubjectSchedule(
                        rs.getInt("schedule_id"),
                        rs.getInt("subject_id"),
                        SubjectScheduleDayOfWeek.valueOf(rs.getString("day_of_week").toUpperCase()),
                        rs.getTime("start_time").toLocalTime().toString(),
                        rs.getTime("end_time").toLocalTime().toString(),
                        rs.getString("room")
                ));
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        } return schedules;
    }
}
