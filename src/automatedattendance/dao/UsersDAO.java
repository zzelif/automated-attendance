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
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("role").toUpperCase())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
