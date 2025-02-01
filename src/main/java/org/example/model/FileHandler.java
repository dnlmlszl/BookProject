package org.example.model;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static void saveToTextFile(List<Book> books, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                writer.write(book.getItemInfo());
                writer.newLine();
            }
        }
    }

    public static List<Book> loadFromTextFile(String filename) throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                Book book = parseBookFromLine(line);
                books.add(book);
            }
        }
        return books;
    }

    private static Book parseBookFromLine(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0].trim());
        String title = parts[1].trim();
        Set<String> authors = new HashSet<>(Arrays.asList(parts[2].trim().split(";")));
        int publicationYear = Integer.parseInt(parts[3].trim());
        double price = Double.parseDouble(parts[4].trim());

        return new Book(id, title, authors, publicationYear, price);
    }
}
