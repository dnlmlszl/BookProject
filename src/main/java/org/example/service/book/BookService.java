package org.example.service.book;

import org.example.model.Book;
import org.example.service.database.DatabaseHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final DatabaseHandler databaseHandler;
    private final BookCatalog bookCatalog;
    private final String filePath;

    public BookService(DatabaseHandler databaseHandler, String filePath) {
        this.databaseHandler = databaseHandler;
        this.bookCatalog = new BookCatalog();
        this.filePath = filePath;
    }

    /**
     * Hozzáad egy új könyvet az in-memory katalógushoz.
     */
    public void addBook(Book book) {
        bookCatalog.addBook(book);
    }

    /**
     * Lekéri az összes könyvet az in-memory katalógusból.
     */
    public List<Book> listBooks() {
        return bookCatalog.listBooks();
    }

    /**
     * Töröl egy könyvet az in-memory katalógusból.
     */
    public void deleteBook(String bookId) {
        bookCatalog.deleteBook(bookId);
    }

    /**
     * Könyvek mentése adatbázisba a memóriából.
     * Csak a 7-es opció hívja meg ezt.
     */
    public void saveBooksToDatabase() throws SQLException {
        try {
            for (Book book : bookCatalog.listBooks()) {
                databaseHandler.saveBook(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Betölti a könyveket az adatbázisból az in-memory katalógusba.
     */
    public void loadBooksFromDatabase() throws SQLException {
        List<Book> booksFromDb = databaseHandler.loadBooks();
        bookCatalog.setBooks(booksFromDb);
        System.out.println("✅ Könyvek sikeresen betöltve az adatbázisból az in-memory katalógusba.");
    }

    /**
     * Könyvek mentése text fájlba az in-memory katalógusból.
     */
    public void saveBooksToFile() throws IOException {
        bookCatalog.saveToTextFile();
    }

    /**
     * Könyvek betöltése text fájlból az in-memory katalógusba.
     */
    public void loadBooksFromFile() throws IOException {
        bookCatalog.loadFromTextFile();

    }

    /**
     * Könyvek mentése binaris fájlba az in-memory katalógusból.
     */
    public void saveBooksToBinaryFile() throws IOException {
        bookCatalog.saveToBinaryFile();
    }

    /**
     * Könyvek betöltése binaris fájlból az in-memory katalógusba.
     */
    public void loadBooksFromBinaryFile() throws IOException {
        bookCatalog.loadFromBinaryFile();
    }

    /**
     * Könyv keresése az in-memory katalógusból cím vagy szerző alapján.
     */
    public List<Book> searchBook(String keyword) {
        return bookCatalog.listBooks()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getAuthors().stream().anyMatch(author -> author.toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }
}
