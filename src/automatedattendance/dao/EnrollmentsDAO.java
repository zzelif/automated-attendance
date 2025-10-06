/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;

import java.sql.*;

/**
 *
 * @author danle
 */
public class EnrollmentsDAO {
    public boolean isStudentEnrolledInSubject(int studentId, int subjectId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();

//            return true
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
