import org.example.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;
    private Set<String> authors;

    @BeforeEach
    void setUp() {
        authors = new HashSet<>();
        authors.add("J.K. Rowling");
        book = new Book("263f756c-f487-435f-b83c-7fcc8dc0c825", "Harry Potter", authors, 1997, 29.99);
    }

    @Test
    void testGetItemInfo() {
        String expected = "ID: 263f756c-f487-435f-b83c-7fcc8dc0c825, Title: Harry Potter, Authors: [J.K. Rowling], Year: 1997, Price: $29.99";
        assertEquals(expected, book.getItemInfo());
    }

    @Test
    void testMatchesWithTitle() {
        assertTrue(book.matches("Harry Potter"));
    }

    @Test
    void testMatchesWithAuthor() {
        assertTrue(book.matches("J.K. Rowling"));
    }

    @Test
    void testMatchesWithNonExistentKeyword() {
        assertFalse(book.matches("J.R.R. Tolkien"));
    }

    @Test
    void testSettersAndGetters() {
        book.setPrice(39.99);
        assertEquals(39.99, book.getPrice());

        book.setPublicationYear(2001);
        assertEquals(2001, book.getPublicationYear());

        Set<String> newAuthors = new HashSet<>();
        newAuthors.add("New Author");
        book.setAuthors(newAuthors);
        assertEquals(newAuthors, book.getAuthors());
    }
}
