package org.example.model;

import java.util.UUID;

public class Item implements CatalogItem {
    private final String id;
    private String title;

    public Item(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Item(String title) {
        this.id = UUID.randomUUID().toString(); // UUID generálása
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getItemInfo() {
        return "ID: " + id + ", Title: " + title;
    }

    @Override
    public boolean matches(String keyword) {
        return title.contains(keyword);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
