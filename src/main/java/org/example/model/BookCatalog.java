package org.example.model;

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

    public void addBook(Book book) {
        books.add(book);
        titleMap.put(book.getTitle(), book);
        for (String author : book.getAuthors()) {
            authorMap.computeIfAbsent(author, k -> new ArrayList<>()).add(book);
        }
    }

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

    public List<Book> listBooks() {
        return books;
    }

    public Book searchByTitle(String title) {
        return titleMap.get(title);
    }

    public List<Book> searchByAuthor(String author) {
        return authorMap.getOrDefault(author, Collections.emptyList());
    }

    public void sortByAuthor() {
        books.sort(Comparator.comparing(b -> b.getAuthors().iterator().next()));
    }

    public void sortByPrice() {
        books.sort(Comparator.comparing(Book::getPrice));
    }

    public void sortByPublicationYear() {
        books.sort(Comparator.comparing(Book::getPublicationYear));
    }
}
