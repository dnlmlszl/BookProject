package org.example.service;

import org.example.model.Book;

import java.io.*;
import java.util.*;

public class FileHandler {
    /**
     * Elmenti a könyvek listáját egy szöveges fájlba.
     *
     * @param books A menteni kívánt könyvek listája.
     * @param filename A fájl neve, ahová a könyveket menteni kell.
     * @throws IOException Ha hiba történik a fájl írása közben.
     */
    public static void saveToTextFile(List<Book> books, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                writer.write(book.getItemInfo());
                writer.newLine();
            }
        }
    }

    /**
     * Betölti a könyvek listáját egy szöveges fájlból.
     *
     * @param filename A fájl neve, ahonnan a könyveket be kell tölteni.
     * @return A fájlból beolvasott könyvek listája.
     * @throws IOException Ha hiba történik a fájl olvasása közben.
     * @throws SQLException Ha hiba történik az adatbázisba történő mentés során a betöltött könyvek esetében.
     */
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

    /**
     * Egy fájlsorban található adatokat könyv objektummá alakít.
     *
     * @param line A fájlsor, amely a könyv adatait tartalmazza.
     * @return A fájlsor alapján létrehozott könyv objektuma.
     * @throws IllegalArgumentException Ha a fájlsor formátuma nem megfelelő, vagy nem tartalmazza az összes szükséges adatot.
     */
    public static Book parseBookFromLine(String line) {
        // Ellenőrizzük, hogy a sor tartalmazza-e a szükséges mezőket
        if (!line.contains("ID:") || !line.contains("Title:") || !line.contains("Authors:") ||
                !line.contains("Year:") || !line.contains("Price:")) {
            throw new IllegalArgumentException("Érvénytelen formátum: " + line);
        }

        try {
            // ID kinyerése
            int idStart = line.indexOf("ID:") + 3;
            int idEnd = line.indexOf(", Title:");
            int id = Integer.parseInt(line.substring(idStart, idEnd).trim());

            // Cím kinyerése
            int titleStart = line.indexOf("Title:") + 6;
            int titleEnd = line.indexOf(", Authors:");
            String title = line.substring(titleStart, titleEnd).trim();

            // Szerzők kinyerése
            int authorsStart = line.indexOf("Authors:") + 8;
            int authorsEnd = line.indexOf(", Year:");
            String authorsRaw = line.substring(authorsStart, authorsEnd).trim();
            authorsRaw = authorsRaw.replace("[", "").replace("]", "");
            Set<String> authors = new HashSet<>(Arrays.asList(authorsRaw.split(", ")));

            // Kiadási év kinyerése
            int yearStart = line.indexOf("Year:") + 5;
            int yearEnd = line.indexOf(", Price:");
            int publicationYear = Integer.parseInt(line.substring(yearStart, yearEnd).trim());

            // Ár kinyerése
            int priceStart = line.indexOf("Price:") + 6;
            String priceString = line.substring(priceStart).trim();
            priceString = priceString.replace(',', '.');  // Vessző cseréje pontra
            priceString = priceString.replaceAll("[^0-9.]", ""); // Nem számjegy karakterek eltávolítása
            double price = Double.parseDouble(priceString);

            return new Book(id, title, authors, publicationYear, price);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Hibás számformátum a következő sorban: " + line);
        } catch (Exception e) {
            throw new IllegalArgumentException("Hiba a sor feldolgozása közben: " + line, e);
        }
    }
}
