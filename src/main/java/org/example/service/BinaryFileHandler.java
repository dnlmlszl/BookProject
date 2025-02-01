package org.example.service;

import org.example.model.Book;

import java.io.*;
import java.util.List;

public class BinaryFileHandler {
    public static void saveToBinaryFile(List<Book> books, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(books);
        }
    }

    public static List<Book> loadFromBinaryFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Book>) ois.readObject();
        }
    }
}
