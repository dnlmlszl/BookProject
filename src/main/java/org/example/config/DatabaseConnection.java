package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    DatabaseConfig.getUrl(),
                    DatabaseConfig.getUser(),
                    DatabaseConfig.getPassword()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database!");
        }
    }
}
