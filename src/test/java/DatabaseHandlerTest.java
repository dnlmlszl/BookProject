import org.example.config.DatabaseConfig;
import org.example.model.Book;
import org.example.service.database.DatabaseHandler;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;
import java.util.Set;

public class DatabaseHandlerTest {
    private static Connection connection;
    private static DatabaseHandler databaseHandler;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );

        databaseHandler = new DatabaseHandler();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "publicationYear INT, " +
                    "price DOUBLE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(255))");

            stmt.execute("CREATE TABLE IF NOT EXISTS book_authors (" +
                    "book_id VARCHAR(36), " +
                    "author_id VARCHAR(36), " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255), " +
                    "password VARCHAR(255), " +
                    "role enum('ADMIN', 'USER', 'GUEST'))");
        }
    }

    @BeforeEach
    void insertDefaultData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("INSERT INTO users (username, password, role) VALUES " +
                    "('admin', 'secret', 'ADMIN'), " +
                    "('user', 'secret', 'USER'), " +
                    "('guest', 'secret', 'GUEST')");

            //stmt.execute("DELETE FROM books");
            stmt.execute("INSERT INTO books (id, title, publicationYear, price) VALUES " +
                    "('263f756c-f487-435f-b83c-7fcc8dc0c825', 'The Catcher in the Rye', 1951, 10.99), " +
                    "('0e796100-b03a-4b47-b98d-f21c11d98779', 'To Kill a Mockingbird', 1960, 7.99), " +
                    "('0e796100-b03a-4b47-b98d-f21c11d98782', '1984', 1949, 8.99)");

            //stmt.execute("DELETE FROM authors");
            stmt.execute("INSERT INTO authors (id, name) VALUES " +
                    "('81bce2ba-8b3b-4992-afa6-11c689b1c55d', 'J.D. Salinger'), " +
                    "('5c09c790-ac3e-4532-9bbf-1d5bb1e5402d', 'Harper Lee'), " +
                    "('5c09c790-ac3e-4532-9bbf-1d5bb1e5402h', 'George Orwell')");

            //stmt.execute("DELETE FROM books_authors");
            stmt.execute("INSERT INTO book_authors (book_id, author_id) VALUES " +
                    "('263f756c-f487-435f-b83c-7fcc8dc0c825', '81bce2ba-8b3b-4992-afa6-11c689b1c55d'), " +
                    "('0e796100-b03a-4b47-b98d-f21c11d98779', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402d'), " +
                    "('0e796100-b03a-4b47-b98d-f21c11d98782', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402h')");
        }
    }

    @AfterEach
    void resetDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("DELETE FROM book_authors");
            stmt.execute("DELETE FROM books");
            stmt.execute("DELETE FROM authors");
            stmt.execute("DELETE FROM users");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            try (var rs = stmt.executeQuery("SELECT COUNT(*) AS row_count FROM users")) {
                if (rs.next()) {
                    System.out.println("Users count after reset: " + rs.getInt("row_count"));
                }
            }

        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("DROP TABLE IF EXISTS book_authors");
            stmt.execute("DROP TABLE IF EXISTS books");
            stmt.execute("DROP TABLE IF EXISTS authors");

        }
        connection.close();
    }

    @Test
    void testSaveBook() throws SQLException {
        Book book = new Book("5c09c790-ac3e-4532-9bbf-1d5bb1e5402o", "Test Book", Set.of("Author 1", "Author 2"), 2025, 19.99);

        databaseHandler.saveBook(book);

        List<Book> books = databaseHandler.loadBooks();
        Assertions.assertFalse(books.isEmpty(), "A könyv nem lett elmentve.");
        Assertions.assertTrue(
                books.stream().anyMatch(b -> b.getTitle().equals("Test Book")),
                "A könyv nem szerepel az adatbázisban."
        );

        Book savedBook = books.stream()
                .filter(b -> b.getTitle().equals("Test Book"))
                .findFirst()
                .orElseThrow();
        Assertions.assertEquals(2, savedBook.getAuthors().size(), "A szerzők száma nem megfelelő.");
    }

    @Test
    void testDeleteBook() throws SQLException {
        Book book = new Book("0e796100-b03a-4b47-b98d-f21c11d9878p", "To Be Deleted", Set.of("Author 3"), 2020, 12.99);
        databaseHandler.saveBook(book);

        databaseHandler.deleteBook(book.getId());

        List<Book> books = databaseHandler.loadBooks();
        Assertions.assertTrue(
                books.stream().anyMatch(b -> b.getTitle().equals("To Be Deleted")),
                "A könyv nem lett törölve."
        );

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM authors WHERE name = 'Author 3'")) {
            Assertions.assertTrue(rs.next(), "A szerző nem lett törölve.");
        }
    }

    @Test
    void testLoadBooks() throws SQLException {
        List<Book> books = databaseHandler.loadBooks();
        Assertions.assertFalse(books.isEmpty(), "Az adatbázis üres.");
        Assertions.assertTrue(
                books.stream().anyMatch(b -> b.getTitle().equals("The Catcher in the Rye")),
                "A 'The Catcher in the Rye' könyv nem található."
        );
    }

}
