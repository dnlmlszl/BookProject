import org.example.model.Book;
import org.example.service.book.BookCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookCatalogTest {

    private BookCatalog catalog;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        catalog = new BookCatalog();

        Set<String> authors1 = Set.of("J.K. Rowling");
        book1 = new Book(1, "Harry Potter", authors1, 1997, 29.99);

        Set<String> authors2 = Set.of("J.R.R. Tolkien");
        book2 = new Book(2, "The Hobbit", authors2, 1937, 19.99);
    }

    @Test
    void testAddBook() {
        catalog.addBook(book1);
        assertEquals(1, catalog.listBooks().size());
        assertTrue(catalog.listBooks().contains(book1));
    }

    @Test
    void testDeleteBook() {
        catalog.addBook(book1);
        catalog.addBook(book2);
        catalog.deleteBook(book1.getId());

        assertEquals(1, catalog.listBooks().size());
        assertFalse(catalog.listBooks().contains(book1));
        assertTrue(catalog.listBooks().contains(book2));
    }

    @Test
    void testSearchByTitle() {
        catalog.addBook(book1);
        catalog.addBook(book2);

        Book foundBook = catalog.searchByTitle("Harry Potter");
        assertEquals(book1, foundBook);
    }

    @Test
    void testSearchByAuthor() {
        catalog.addBook(book1);
        catalog.addBook(book2);

        List<Book> foundBooks = catalog.searchByAuthor("J.R.R. Tolkien");
        assertTrue(foundBooks.contains(book2));
    }

    @Test
    void testSortByPrice() {
        catalog.addBook(book1);
        catalog.addBook(book2);

        catalog.sortByPrice();
        List<Book> books = catalog.listBooks();

        assertEquals(book2, books.get(0));
        assertEquals(book1, books.get(1));
    }

    @Test
    void testSortByPublicationYear() {
        catalog.addBook(book1);
        catalog.addBook(book2);

        catalog.sortByPublicationYear();
        List<Book> books = catalog.listBooks();

        assertEquals(book2, books.get(0));
        assertEquals(book1, books.get(1));
    }

    @Test
    void testSortByAuthor() {
        catalog.addBook(book1);
        catalog.addBook(book2);

        catalog.sortByAuthor();
        List<Book> books = catalog.listBooks();

        assertEquals(book1, books.get(0));
        assertEquals(book2, books.get(1));
    }
}
