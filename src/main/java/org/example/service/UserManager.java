package org.example.service;

import org.example.config.DatabaseConnection;
import org.example.model.Role;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserManager {

    /**
     * Bejelentkezik egy felhasználó a megadott felhasználónév és jelszó alapján.
     * Ellenőrzi a megadott hitelesítési adatokat, és visszaadja a megfelelő felhasználót,
     * ha a bejelentkezés sikeres. Ha a hitelesítés nem sikerül, null értékkel tér vissza.
     *
     * @param username A felhasználó neve.
     * @param password A felhasználó jelszava.
     * @return A bejelentkezett felhasználó objektuma, ha sikeres, egyébként null.
     * @throws SQLException Ha hiba történik az adatbázisban a felhasználói adatok lekérésekor.
     */
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
