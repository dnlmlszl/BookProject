package org.example.model;

public interface CatalogItem {
    String getItemInfo();
    boolean matches(String keyword);
}
