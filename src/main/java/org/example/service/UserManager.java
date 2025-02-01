package org.example.service;

import org.example.config.DatabaseConnection;
import org.example.model.Role;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserManager {

    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String roleStr = rs.getString("role");
                Role role = Role.valueOf(roleStr);
                return new User(id, username, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Invalid user data.");
        }
        return null;
    }
}
