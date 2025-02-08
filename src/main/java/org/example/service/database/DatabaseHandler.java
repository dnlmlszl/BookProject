package org.example.service.database;

import org.example.config.DatabaseConnection;
import org.example.model.Book;

import java.sql.*;
import java.util.*;

public class DatabaseHandler {
    private Connection connection;

    public DatabaseHandler() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Elmenti a könyvet az adatbázisba.
     *
     * @param book A menteni kívánt könyv.
     * @throws SQLException Ha hiba történik az adatbázisba mentés során.
     */
    public void saveBook(Book book) throws SQLException {
        // Ellenőrizzük, hogy a könyv már létezik-e
        String checkBookQuery = "SELECT id FROM books WHERE title = ? AND publicationYear = ?";
        String bookId = null;
        try (PreparedStatement checkStmt = connection.prepareStatement(checkBookQuery)) {
            checkStmt.setString(1, book.getTitle());
            checkStmt.setInt(2, book.getPublicationYear());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                bookId = rs.getString("id");
                // Frissítjük az árát, ha létezik
                String updateBookQuery = "UPDATE books SET price = ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateBookQuery)) {
                    updateStmt.setDouble(1, book.getPrice());
                    updateStmt.setString(2, bookId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Új könyv hozzáadása
                bookId = UUID.randomUUID().toString();
                String insertBookQuery = "INSERT INTO books (id, title, publicationYear, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertBookQuery)) {
                    insertStmt.setString(1, bookId);
                    insertStmt.setString(2, book.getTitle());
                    insertStmt.setInt(3, book.getPublicationYear());
                    insertStmt.setDouble(4, book.getPrice());
                    insertStmt.executeUpdate();
                }
            }
        }
        // Szerzők mentése
        saveAuthors(bookId, book.getAuthors());
    }

    /**
     * Elmenti a szerzőket az adatbázisba.
     *
     * @param bookId  A könyv azonosítója.
     * @param authors A szerzők halmaza.
     * @throws SQLException Ha hiba történik az adatbázisba mentés során.
     */

    private void saveAuthors(String bookId, Set<String> authors) throws SQLException {
        String authorQuery = "INSERT INTO authors (id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";
        String bookAuthorQuery = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE book_id = book_id";

        Map<String, String> existingAuthors = new HashMap<>();

        // Lekérjük a már létező szerzőket
        String authorIdsQuery = "SELECT id, name FROM authors WHERE name IN (" + String.join(",", Collections.nCopies(authors.size(), "?")) + ")";
        try (PreparedStatement pstmt = connection.prepareStatement(authorIdsQuery)) {
            int index = 1;
            for (String author : authors) {
                pstmt.setString(index++, author);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                existingAuthors.put(rs.getString("name"), rs.getString("id"));
            }
        }

        try (PreparedStatement authorStmt = connection.prepareStatement(authorQuery);
             PreparedStatement bookAuthorStmt = connection.prepareStatement(bookAuthorQuery)) {

            for (String author : authors) {
                String authorId = existingAuthors.get(author);
                if (authorId == null) {
                    // Ha a szerző nem létezik, beszúrjuk
                    authorId = UUID.randomUUID().toString();
                    authorStmt.setString(1, authorId);
                    authorStmt.setString(2, author);
                    authorStmt.executeUpdate();
                }

                // Ellenőrizzük, hogy a könyv-szerző kapcsolat már létezik-e
                String checkBookAuthorQuery = "SELECT book_id FROM book_authors WHERE book_id = ? AND author_id = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkBookAuthorQuery)) {
                    checkStmt.setString(1, bookId);
                    checkStmt.setString(2, authorId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (!rs.next()) {
                        // Ha a kapcsolat nem létezik, beszúrjuk
                        bookAuthorStmt.setString(1, bookId);
                        bookAuthorStmt.setString(2, authorId);
                        bookAuthorStmt.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Lekéri a szerző azonosítóját a név alapján.
     *
     * @param author A szerző neve.
     * @return A szerző azonosítója, ha létezik; -1, ha nem létezik.
     * @throws SQLException Ha hiba történik az adatbázisból való lekérés során.
     */
    private String getAuthorId(String author) throws SQLException {
        String query = "SELECT id FROM authors WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, author);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        }
        return null;
    }



    /**
     * Lekéri az összes könyvet az adatbázisból.
     *
     * @return A könyvek listája.
     * @throws SQLException Ha hiba történik az adatbázisból való lekérés során.
     */
    public List<Book> loadBooks() throws SQLException {
        List<Book> books;
        String query = "SELECT b.id, b.title, b.publicationYear, b.price, a.name AS author_name " +
                "FROM books b " +
                "LEFT JOIN book_authors ba ON b.id = ba.book_id " +
                "LEFT JOIN authors a ON ba.author_id = a.id";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            Map<String, Book> bookMap = new HashMap<>();
            while (rs.next()) {
                String bookId = rs.getString("id");
                Book book = bookMap.get(bookId);

                if (book == null) {
                    String title = rs.getString("title");
                    int publicationYear = rs.getInt("publicationYear");
                    double price = rs.getDouble("price");
                    Set<String> authors = new HashSet<>();
                    authors.add(rs.getString("author_name"));
                    book = new Book(title, authors, publicationYear, price);
                    bookMap.put(bookId, book);
                } else {
                    book.getAuthors().add(rs.getString("author_name"));
                }
            }
            books = new ArrayList<>(bookMap.values());
        }
        return books;
    }

    /**
     * Töröl egy könyvet az adatbázisból.
     *
     * @param bookId A törlendő könyv azonosítója.
     * @throws SQLException Ha hiba történik az adatbázisból való törlés során.
     */
    public void deleteBook(String bookId) throws SQLException {
        String deleteBookAuthorsQuery = "DELETE FROM book_authors WHERE book_id = ?";
        String deleteBookQuery = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement pstmt1 = connection.prepareStatement(deleteBookAuthorsQuery);
             PreparedStatement pstmt2 = connection.prepareStatement(deleteBookQuery)) {

            pstmt1.setString(1, bookId);
            pstmt1.executeUpdate();

            pstmt2.setString(1, bookId);
            pstmt2.executeUpdate();
        }
    }



}