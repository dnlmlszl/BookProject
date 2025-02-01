package org.example.service;

import org.example.config.DatabaseConnection;
import org.example.model.Book;

import java.sql.*;
import java.util.*;

public class DatabaseHandler {

    public static void saveBook(Book book) throws SQLException {

        String query = "INSERT INTO books (id, title, publicationYear, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getPublicationYear());
            pstmt.setDouble(4, book.getPrice());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookId = generatedKeys.getInt(1);

                    String authorQuery = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";
                    PreparedStatement authorStmt = connection.prepareStatement(authorQuery);

                    for (String author : book.getAuthors()) {
                        String checkAuthorQuery = "SELECT id FROM authors WHERE name = ?";
                        PreparedStatement checkAuthorStmt = connection.prepareStatement(checkAuthorQuery);
                        checkAuthorStmt.setString(1, author);
                        ResultSet rs = checkAuthorStmt.executeQuery();

                        int authorId;
                        if (rs.next()) {
                            authorId = rs.getInt("id");
                        } else {
                            String insertAuthorQuery = "INSERT INTO authors (name) VALUES (?)";
                            PreparedStatement insertAuthorStmt = connection.prepareStatement(insertAuthorQuery, Statement.RETURN_GENERATED_KEYS);
                            insertAuthorStmt.setString(1, author);
                            insertAuthorStmt.executeUpdate();

                            ResultSet generatedAuthorKeys = insertAuthorStmt.getGeneratedKeys();
                            if (generatedAuthorKeys.next()) {
                                authorId = generatedAuthorKeys.getInt(1);
                            } else {
                                throw new SQLException("Error retrieving author id for author: " + author);
                            }
                        }

                        authorStmt.setInt(1, bookId);
                        authorStmt.setInt(2, authorId);
                        authorStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error adding new book with title: " + book.getTitle(), e);
        }
    }

    public static List<Book> loadBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id, b.title, b.publicationYear, b.price, a.id AS author_id, a.name AS author_name " +
                "FROM books b " +
                "LEFT JOIN book_authors ba ON b.id = ba.book_id " +
                "LEFT JOIN authors a ON ba.author_id = a.id";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();

                Map<Integer, Book> bookMap = new HashMap<>();

                while (rs.next()) {
                    int bookId = rs.getInt("id");
                    String title = rs.getString("title");
                    int publicationYear = rs.getInt("publicationYear");
                    double price = rs.getDouble("price");
                    int authorId = rs.getInt("author_id");
                    String authorName = rs.getString("author_name");

                    Book book = bookMap.get(bookId);
                    if (book == null) {
                        Set<String> authors = new HashSet<>();
                        authors.add(authorName);
                        book = new Book(bookId, title, authors, publicationYear, price);
                        bookMap.put(bookId, book);
                    } else {
                        book.getAuthors().add(authorName);
                    }
                }

                books.addAll(bookMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving list of books: ", e);
        }
        return books;
    }


    public static void updateBook(Book book) throws SQLException {
        String query = "UPDATE books SET title = ?, authors = ?, publicationYear = ?, price = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, String.join(",", book.getAuthors()));
            pstmt.setInt(3, book.getPublicationYear());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, book.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No book found with id: " + book.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating book with id: " + book.getId(), e);
        }
    }

    public static void deleteBook(int bookId) throws SQLException {
        String deleteBookAuthorsQuery = "DELETE FROM book_authors WHERE book_id = ?";
        String deleteBookQuery = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(deleteBookAuthorsQuery)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = connection.prepareStatement(deleteBookQuery)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            System.out.println("Book with ID " + bookId + " has been deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting book with id: " + bookId, e);
        }
    }


    public static List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id, b.title, b.publicationYear, b.price, GROUP_CONCAT(a.name SEPARATOR ', ') AS authors "
                + "FROM books b "
                + "JOIN book_authors ba ON b.id = ba.book_id "
                + "JOIN authors a ON ba.author_id = a.id "
                + "WHERE b.title LIKE ? "
                + "GROUP BY b.id";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                Set<String> authors = Set.of(rs.getString("authors").split(", "));
                int publicationYear = rs.getInt("publicationYear");
                double price = rs.getDouble("price");
                books.add(new Book(id, title, authors, publicationYear, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error searching books by title: ", e);
        }
        return books;
    }

    public static Book loadBookById(int id) throws SQLException {
        String query = "SELECT b.id, b.title, b.publicationYear, b.price, a.id AS author_id, a.name AS author_name " +
                "FROM books b " +
                "LEFT JOIN book_authors ba ON b.id = ba.book_id " +
                "LEFT JOIN authors a ON ba.author_id = a.id " +
                "WHERE b.id = ?";
        Book book = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            Set<String> authors = new HashSet<>();

            while (rs.next()) {
                if (book == null) {
                    String title = rs.getString("title");
                    int publicationYear = rs.getInt("publicationYear");
                    double price = rs.getDouble("price");
                    authors.add(rs.getString("author_name"));
                    book = new Book(id, title, authors, publicationYear, price);
                } else {
                    authors.add(rs.getString("author_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving book with id: " + id, e);
        }
        return book;
    }
}
