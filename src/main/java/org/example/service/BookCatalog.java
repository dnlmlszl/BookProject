package org.example.service;

import org.example.model.Book;

import java.util.*;

public class BookCatalog {
    private List<Book> books;
    private Map<String, Book> titleMap;
    private Map<String, List<Book>> authorMap;

    public BookCatalog() {
        books = new ArrayList<>();
        titleMap = new HashMap<>();
        authorMap = new HashMap<>();
    }

    /**
     * Hozzáad egy könyvet a könyv katalógushoz.
     * A könyvet a cím alapján is elmenti a cím-térképen (titleMap),
     * és az összes szerzőt a szerző-térképen (authorMap) is rögzíti.
     *
     * @param book A hozzáadandó könyv.
     */
    public void addBook(Book book) {
        books.add(book);
        titleMap.put(book.getTitle(), book);
        for (String author : book.getAuthors()) {
            authorMap.computeIfAbsent(author, k -> new ArrayList<>()).add(book);
        }
    }

    /**
     * Töröl egy könyvet a katalógusból azonosítója alapján.
     * Ha a könyv létezik, eltávolítja a könyvet a könyvlistából, a cím-térképről és
     * a szerzőtérképről is.
     *
     * @param id A törlendő könyv azonosítója.
     */
    public void deleteBook(int id) {
        Book bookToRemove = books.stream().filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);

        if (bookToRemove != null) {
            books.remove(bookToRemove);
            titleMap.remove(bookToRemove.getTitle());
            for (String author : bookToRemove.getAuthors()) {
                authorMap.get(author).remove(bookToRemove);
                if (authorMap.get(author).isEmpty()) {
                    authorMap.remove(author);
                }
            }
        }
    }

    /**
     * Visszaadja az összes könyvet a katalógusból.
     *
     * @return A könyvek listája.
     */
    public List<Book> listBooks() {
        return books;
    }

    /**
     * Keres egy könyvet a cím alapján.
     * Ha a cím nem található, null értéket ad vissza.
     *
     * @param title A keresett könyv címe.
     * @return A megtalált könyv, ha van; null, ha nem található.
     */
    public Book searchByTitle(String title) {
        return titleMap.get(title);
    }

    /**
     * Keres könyveket egy szerző alapján.
     * Ha a szerző nem található, üres listát ad vissza.
     *
     * @param author A keresett szerző neve.
     * @return A könyvek listája, amelyek a megadott szerzőhöz tartoznak.
     */
    public List<Book> searchByAuthor(String author) {
        return authorMap.getOrDefault(author, Collections.emptyList());
    }

    /**
     * Rendezni az összes könyvet a szerzők neve szerint.
     */
    public void sortByAuthor() {
        books.sort(Comparator.comparing(b -> b.getAuthors().iterator().next()));
    }

    /**
     * Rendezni az összes könyvet az árak alapján.
     */
    public void sortByPrice() {
        books.sort(Comparator.comparing(Book::getPrice));
    }

    /**
     * Rendezni az összes könyvet a kiadási évük szerint.
     */
    public void sortByPublicationYear() {
        books.sort(Comparator.comparing(Book::getPublicationYear));
    }
}
