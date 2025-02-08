package org.example.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Book extends Item implements Serializable {

    private Set<String> authors;
    private int publicationYear;
    private double price;

    public Book() {
        super(UUID.randomUUID().toString(),"Unknown author");
        this.authors = new HashSet<>();
        this.publicationYear = 0;
        this.price = 0.0;
    }

    public Book(String title, Set<String> authors, int publicationYear, double price) {
        super(UUID.randomUUID().toString(), title);
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public Book(String id, String title, Set<String> authors, int publicationYear, double price) {
        super(id, title);
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Visszaadja a könyv lényeges információit egy karakterláncként.
     * Az információk tartalmazzák az azonosítót, címet, szerzőket, kiadási évet és árat.
     *
     * @return a könyv információit tartalmazó karakterlánc az alábbi formátumban:
     *         "Item ID: <id>, Title: <title>, Authors: <authors>, Year: <year>, Price: <price>"
     */
    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Authors: " + authors + ", Year: " + publicationYear + ", Price: $" + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return getTitle().equals(book.getTitle()) && authors.equals(book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), authors);
    }

    /**
     * Ez a metódus biztosítja, hogy egy könyv kereshető legyen a cím, illetve a szerzők alapján.
     *
     * @param keyword A keresett kulcsszó, amelyet a cím és a szerzők ellenőriznek.
     * @return true, ha a könyv címe vagy szerzője egyezik a kulcsszóval;
     *         false egyébként.
     */
    @Override
    public boolean matches(String keyword) {
        return getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                authors.stream().anyMatch(a -> a.toLowerCase().contains(keyword.toLowerCase()));
    }
}