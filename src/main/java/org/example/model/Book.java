package org.example.model;

import java.util.Set;

public class Book extends Item{
    private Set<String> authors;
    private int publicationYear;
    private double price;

    public Book(int id, String title, Set<String> authors, int publicationYear, double price) {
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

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Authors: " + authors + ", Year: " + publicationYear + ", Price: " + price;
    }

    @Override
    public boolean matches(String keyword) {
        return super.matches(keyword) || authors.contains(keyword);
    }
}
