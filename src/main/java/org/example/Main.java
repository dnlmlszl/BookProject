package org.example;

import org.example.controller.CatalogController;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CatalogController controller = new CatalogController();
        controller.start();

    }
}