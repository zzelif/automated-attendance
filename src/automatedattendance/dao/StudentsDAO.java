/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;
import automatedattendance.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Student> getStudentsBySubject(int subjectId) {
        final List<Student> students = new ArrayList<>();
        final String sql = "SELECT s.* FROM students s " +
                           "JOIN enrollments e ON s.student_id = e.student_id " +
                           "WHERE e.subject_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_number"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("course"),
                        rs.getInt("year_level")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

}
