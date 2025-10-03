/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;
import automatedattendance.model.Student;

import java.sql.*;
/**
 *
 * @author danle
 */
public class StudentsDAO {
    public Student getStudentByNumber(String studentNumber) {
        String sql = "SELECT * FROM students WHERE student_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_number"),
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("course"),
                    rs.getInt("year_level")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
