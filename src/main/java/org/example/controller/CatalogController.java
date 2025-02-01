package org.example.controller;

import org.example.model.Book;
import org.example.model.User;
import org.example.service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CatalogController {
    private BookCatalog catalog;
    private Scanner scanner;
    private User currentUser;
    private UserManager userManager;

    public CatalogController() {
        this.catalog = new BookCatalog();
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager();
    }

    /**
     * A program indításakor bejelentkezik a felhasználó, majd megjeleníti a menüt
     * és lehetőséget biztosít a felhasználónak különböző műveletek végrehajtására
     * (pl. könyv hozzáadása, törlés, listázás stb.).
     *
     * @throws SQLException Ha adatbázis hiba történik a felhasználó bejelentkezése közben.
     */
    public void start() throws SQLException {
        loginUser();
        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    if (AccessControl.hasPermission(currentUser.getRole(), "addBook")) {
                        addBook();
                    } else {
                        System.out.println("Nincs jogosultságod könyv hozzáadásához!");
                    }
                    break;
                case 2:
                    if (AccessControl.hasPermission(currentUser.getRole(), "deleteBook")) {
                        deleteBook();
                    } else {
                        System.out.println("Nincs jogosultságod könyv törléséhez!");
                    }
                    break;
                case 3:
                    if (AccessControl.hasPermission(currentUser.getRole(), "listBooks")) {
                        listBooks();
                    } else {
                        System.out.println("Nincs jogosultságod könyvek listázásához!");
                    }
                    break;
                case 4:
                    if (AccessControl.hasPermission(currentUser.getRole(), "searchBooks")) {
                        searchBooks();
                    } else {
                        System.out.println("Nincs jogosultságod könyv kereséséhez!");
                    }
                    break;
                case 5:
                    if (AccessControl.hasPermission(currentUser.getRole(), "saveToFile")) {
                        saveToFile();
                    } else {
                        System.out.println("Nincs jogosultságod fájlba mentéshez!");
                    }
                    break;
                case 6:
                    if (AccessControl.hasPermission(currentUser.getRole(), "loadFromFile")) {
                        loadFromFile();
                    } else {
                        System.out.println("Nincs jogosultságod fájlból betöltéshez!");
                    }
                    break;
                case 7:
                    if (AccessControl.hasPermission(currentUser.getRole(), "saveToDatabase")) {
                        saveToDatabase();
                    } else {
                        System.out.println("Nincs jogosultságod adatbázisba mentéshez!");
                    }
                    break;
                case 8:
                    System.exit(0);
                default:
                    System.out.println("Érvénytelen választás!");
            }
        }
    }

    /**
     * Bejelentkezés a rendszerbe a felhasználónév és jelszó megadásával.
     * Ha a felhasználó sikeresen bejelentkezett, beállítja az aktuális felhasználót.
     * Ha a bejelentkezés sikertelen, kilép a program.
     *
     * @throws SQLException Ha hiba történik a bejelentkezési folyamat során.
     */
    private void loginUser() throws SQLException {
        System.out.println("Bejelentkezés");
        System.out.print("Felhasználónév: ");
        String username = scanner.nextLine();
        System.out.print("Jelszó: ");
        String password = scanner.nextLine();

        currentUser = userManager.login(username, password);
        if (currentUser == null) {
            System.out.println("Hibás felhasználónév vagy jelszó. Kilépés...");
            System.exit(0);
        } else {
            System.out.println("Sikeres bejelentkezés! Felhasználó: " + currentUser.getUsername());
        }
    }

    /**
     * Megjeleníti a felhasználói menüt, ahol a felhasználó választhat a különböző műveletek között.
     */
    private void showMenu() {
        System.out.println("1. Könyv hozzáadása");
        System.out.println("2. Könyv törlése");
        System.out.println("3. Könyvek listázása");
        System.out.println("4. Könyv keresése");
        System.out.println("5. Mentés fájlba");
        System.out.println("6. Betöltés fájlból");
        System.out.println("7. Mentés adatbázisba");
        System.out.println("8. Kilépés");
    }

    /**
     * Hozzáad egy új könyvet a katalógushoz, miután a felhasználó megadta a könyv adatait.
     * Az új könyvet az adatbázisba is menti.
     */
    public void addBook() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Add meg a könyv címét: ");
            String title = scanner.nextLine();

            System.out.println("Add meg a könyv szerzőjét (válaszd el vesszővel): ");
            String authorsInput = scanner.nextLine();
            Set<String> authors = Set.of(authorsInput.split(","));

            System.out.println("Add meg a könyv kiadási évét: ");
            int publicationYear = scanner.nextInt();

            System.out.println("Add meg a könyv árát: ");
            double price = scanner.nextDouble(); // Ebben az esetben angol formátumot fog használni

            // Itt folytatódik a könyv hozzáadásának logikája, például:
            Book book = new Book(0, title, authors, publicationYear, price);
            DatabaseHandler.saveBook(book);  // Példa mentés a DB-be

        } catch (InputMismatchException e) {
            System.out.println("Hibás adatbevitel! Kérlek, ellenőrizd a számokat.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Hiba történt a könyv hozzáadása közben: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Töröl egy könyvet azonosítója alapján a katalógusból és az adatbázisból.
     */
    private void deleteBook() {
        System.out.println("Add meg a törlendő könyv ID-ját: ");
        String input = scanner.nextLine();

        try {
            int bookId = Integer.parseInt(input);
            catalog.deleteBook(bookId);

            try {
                DatabaseHandler.deleteBook(bookId);
                System.out.println("Könyv törölve az adatbázisból.");
            } catch (SQLException e) {
                System.out.println("Hiba történt az adatbázisból való törlés során!");
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            System.out.println("Hibás könyv ID. Kérlek, adj meg egy érvényes számot.");
        }
    }

    /**
     * Listázza az összes könyvet a katalógusból, és kiírja azokat a konzolra.
     *
     * @throws SQLException Ha adatbázis hiba történik a könyvek lekérésekor.
     */
    private void listBooks() throws SQLException {
        List<Book> books = DatabaseHandler.loadBooks();
        if (books.isEmpty()) {
            System.out.println("Nincs elérhető könyv.");
        } else {
            for (Book book : books) {
                System.out.println(book.getItemInfo());
            }
        }
    }

    /**
     * Keres egy könyvet a cím alapján, és megjeleníti a találatokat a konzolon.
     */
    private void searchBooks() {
        System.out.println("Add meg a keresett könyv címét: ");
        String keyword = scanner.nextLine();
        try {
            List<Book> books = DatabaseHandler.searchBooks(keyword);
            if (books.isEmpty()) {
                System.out.println("Nincs találat.");
            } else {
                for (Book book : books) {
                    System.out.println(book.getItemInfo());
                }
            }
        } catch (SQLException e) {
            System.out.println("Hiba történt a könyvek keresésekor.");
            e.printStackTrace();
        }
    }

    /**
     * Ment egy fájlba az összes könyvet a katalógusból.
     * A fájl nevét a felhasználó adja meg.
     */
    private void saveToFile() {
        System.out.println("Add meg a fájl nevét a mentéshez: ");
        String filename = scanner.nextLine();

        try {
            List<Book> books = DatabaseHandler.loadBooks();

            if (books.isEmpty()) {
                System.out.println("Nincsenek könyvek a fájl mentéséhez.");
                return;
            }

            FileHandler.saveToTextFile(books, filename);
            System.out.println("Könyvek sikeresen mentve a fájlba.");
        } catch (IOException | SQLException e) {
            System.out.println("Hiba történt a fájl mentésekor!");
            e.printStackTrace();
        }
    }

    /**
     * Betölt könyveket egy fájlból, és elmenti őket az adatbázisba.
     * A fájl nevét a felhasználó adja meg.
     */
    private void loadFromFile() {
        System.out.println("Add meg a fájl nevét a betöltéshez: ");
        String filename = scanner.nextLine();

        try {
            List<Book> loadedBooks = FileHandler.loadFromTextFile(filename);
            if (loadedBooks.isEmpty()) {
                System.out.println("A fájl üres vagy nem tartalmaz betölthető adatokat.");
                return;
            }

            for (Book book : loadedBooks) {
                catalog.addBook(book);
                DatabaseHandler.saveBook(book);
            }

            System.out.println("Könyvek sikeresen betöltve és elmentve az adatbázisba.");
        } catch (IOException | SQLException e) {
            System.out.println("Hiba történt a fájl betöltésekor!");
            e.printStackTrace();
        }
    }

    /**
     * Ment egy könyvet az adatbázisba az összes könyv listája alapján.
     */
    private void saveToDatabase() {
        List<Book> books = catalog.listBooks();
        if (books.isEmpty()) {
            System.out.println("Nincsenek könyvek az adatbázisba mentéshez.");
        } else {
            for (Book book : books) {
                try {
                    DatabaseHandler.saveBook(book);
                    System.out.println("Könyv mentve az adatbázisba: " + book.getTitle());
                } catch (SQLException e) {
                    System.out.println("Hiba történt az adatbázisba mentés során!");
                    e.printStackTrace();
                }
            }
        }
    }
}
