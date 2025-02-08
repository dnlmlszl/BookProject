package org.example.service.book;

import org.example.model.Book;

import java.io.*;
import java.util.*;

public class BookCatalog implements Comparator<Book> {
    private List<Book> books;
    private Map<String, Book> titleMap;
    private Map<String, List<Book>> authorMap;

    private static final String TEXT_FILE = "books.txt";
    private static final String BINARY_FILE = "books.dat";

    public BookCatalog() {
        books = new ArrayList<>();
        titleMap = new HashMap<>();
        authorMap = new HashMap<>();
    }

    /**
     * Beállítja (vagy felülírja) az in-memory könyvlistát a megadott listával.
     */
    public void setBooks(List<Book> newBooks) {
        books.clear();
        titleMap.clear();
        authorMap.clear();
        for (Book book : newBooks) {
            addBook(book);
        }
    }

    /**
     * Hozzáad egy könyvet a könyv katalógushoz.
     * A könyvet a cím alapján is elmenti a cím-térképen (titleMap),
     * és az összes szerzőt a szerző-térképen (authorMap) is rögzíti.
     *
     * @param book A hozzáadandó könyv.
     */
    public void addBook(Book book) {
        if (titleMap.containsKey(book.getTitle())) {
            System.out.println("A könyv már létezik a katalógusban.");
            return;
        }

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
    public void deleteBook(String id) {
        Book bookToRemove = books.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst().orElse(null);
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            titleMap.remove(bookToRemove.getTitle());
            for (String author : bookToRemove.getAuthors()) {
                List<Book> list = authorMap.get(author);
                if (list != null) {
                    list.remove(bookToRemove);
                    if (list.isEmpty()) {
                        authorMap.remove(author);
                    }
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
        return new ArrayList<>(books);
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

    /**
     * Comparator<Book> implementáció – alapértelmezett rendezés a cím alapján.
     */
    @Override
    public int compare(Book b1, Book b2) {
        return b1.getTitle().compareTo(b2.getTitle());
    }

    /**
     * 📌 Könyvek mentése szöveges fájlba.
     */
    public void saveToTextFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE))) {
            for (Book book : books) {
                writer.write(book.getId() + ";" + book.getTitle() + ";" +
                        String.join(",", book.getAuthors()) + ";" +
                        book.getPublicationYear() + ";" + book.getPrice());
                writer.newLine();
            }
            System.out.println("✅ Könyvek sikeresen mentve a " + TEXT_FILE + " fájlba.");
        } catch (IOException e) {
            System.err.println("❌ Hiba a fájl mentése közben: " + e.getMessage());
        }
    }

    /**
     * 📌 Könyvek betöltése szöveges fájlból.
     */
    public void loadFromTextFile() {
        books.clear();
        titleMap.clear();
        authorMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 5) continue;

                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                Set<String> authors = new HashSet<>(Arrays.asList(parts[2].split(",")));
                int publicationYear = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);

                if (titleMap.containsKey(title)) {
                    continue;
                }

                addBook(new Book(title, authors, publicationYear, price));
            }
            System.out.println("✅ Könyvek sikeresen betöltve a " + TEXT_FILE + " fájlból.");
        } catch (IOException e) {
            System.err.println("❌ Hiba a fájl betöltése közben: " + e.getMessage());
        }
    }

    /**
     * 📌 Könyvek mentése bináris fájlba.
     */
    public void saveToBinaryFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
            oos.writeObject(books);
            System.out.println("✅ Könyvek sikeresen mentve a " + BINARY_FILE + " fájlba.");
        } catch (IOException e) {
            System.err.println("❌ Hiba a bináris fájl mentése közben: " + e.getMessage());
        }
    }

    /**
     * 📌 Könyvek betöltése bináris fájlból.
     */
    @SuppressWarnings("unchecked")
    public void loadFromBinaryFile() {
        books.clear();
        titleMap.clear();
        authorMap.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BINARY_FILE))) {
            books = (List<Book>) ois.readObject();

            for (Book book : books) {
                titleMap.put(book.getTitle(), book);
                for (String author : book.getAuthors()) {
                    authorMap.computeIfAbsent(author, k -> new ArrayList<>()).add(book);
                }
            }
            System.out.println("✅ Könyvek sikeresen betöltve a " + BINARY_FILE + " fájlból.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Hiba a bináris fájl betöltése közben: " + e.getMessage());
        }
    }
}
