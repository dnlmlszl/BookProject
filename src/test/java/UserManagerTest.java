import org.example.config.DatabaseConfig;
import org.example.service.user.UserManager;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserManagerTest {
    private static Connection connection;
    private static UserManager userManager;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );

        try (Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255), " +
                    "password VARCHAR(255), " +
                    "role enum('ADMIN', 'USER', 'GUEST'))");
        }
    }

    @BeforeEach
    void insertDefaultUser() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users");
            stmt.execute("INSERT INTO users (username, password, role) VALUES " +
                    "('admin', 'secret', 'ADMIN'), " +
                    "('user', 'secret', 'USER'), " +
                    "('guest', 'secret', 'GUEST')");
        }
    }

    @AfterEach
    void resetDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("DELETE FROM users");
            stmt.execute("ALTER TABLE users AUTO_INCREMENT = 1");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            try (var rs = stmt.executeQuery("SELECT COUNT(*) AS row_count FROM users")) {
                if (rs.next()) {
                    System.out.println("Users count after reset: " + rs.getInt("row_count"));
                }
            }

        }
    }

    @Test
    void testAdminLogin() throws SQLException {
        String username = "admin";
        String password = "secret";
        String expectedRole = "ADMIN";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");

            assertTrue(rs.next(), "User should exist in the database");
            assertEquals(expectedRole, rs.getString("role"), "User should have the correct role");
        }
    }

    @Test
    void testUserLogin() throws SQLException {
        String username = "user";
        String password = "secret";
        String expectedRole = "USER";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");

            assertTrue(rs.next(), "User should exist in the database");
            assertEquals(expectedRole, rs.getString("role"), "User should have the correct role");
        }
    }

    @Test
    void testGuestLogin() throws SQLException {
        String username = "guest";
        String password = "secret";
        String expectedRole = "GUEST";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");

            assertTrue(rs.next(), "User should exist in the database");
            assertEquals(expectedRole, rs.getString("role"), "User should have the correct role");
        }
    }

    @Test
    void testUserRole() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin' AND role = 'ADMIN'");
            assertTrue(rs.next(), "Admin should have ADMIN role");

            rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'user' AND role = 'USER'");
            assertTrue(rs.next(), "User should have USER role");

            rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'guest' AND role = 'GUEST'");
            assertTrue(rs.next(), "Guest should have GUEST role");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
        }
        connection.close();
    }
}
