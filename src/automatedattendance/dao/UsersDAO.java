/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automatedattendance.dao;

import automatedattendance.db.DatabaseConnection;
import automatedattendance.model.User;
import automatedattendance.util.Enums.UserRole;

import java.sql.*;
/**
 *
 * @author danle
 */
public class UsersDAO {
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );

                if (user.getRole() == UserRole.STUDENT) {
                    StudentsDAO studentDAO = new StudentsDAO();
                    String studentNumber = studentDAO.getStudentNumberByUserId(user.getUserId());
                    user.setStudentNumber(studentNumber);
                    
                    System.out.println("DEBUG: Loaded student number = " + studentNumber);
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
