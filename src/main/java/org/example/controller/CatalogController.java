package org.example.controller;

import org.example.model.Book;
import org.example.model.User;
import org.example.service.book.BookCatalog;
import org.example.service.book.BookService;
import org.example.service.database.DatabaseHandler;
import org.example.service.user.AccessControl;
import org.example.service.user.UserManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogController {
    private final BookCatalog catalog;
    private final Scanner scanner;
    private final UserManager userManager;
    private final BookService bookService;
    private User currentUser;


    public CatalogController() {
        this.catalog = new BookCatalog();
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager();
        DatabaseHandler handler = new DatabaseHandler();
        this.bookService = new BookService(handler, "books.txt");
    }

    /**
     * A program indításakor bejelentkezik a felhasználó, majd megjeleníti a menüt
     * és lehetőséget biztosít a felhasználónak különböző műveletek végrehajtására
     * (pl. könyv hozzáadása, törlés, listázás stb.).
     *
     * @throws SQLException Ha adatbázis hiba történik a felhasználó bejelentkezése közben.
     */
    public void start() throws SQLException {
        try {
            loginUser();
            while (true) {
                showMenu();
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    handleMenuChoice(choice);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Érvénytelen választás! Kérlek, adj meg egy számot.");
                } catch (Exception e) {
                    System.out.println("❌ Váratlan hiba történt: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Súlyos hiba történt: " + e.getMessage());
            System.exit(1);
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
            System.out.println("❌ Hibás felhasználónév vagy jelszó. Kilépés...");
            System.exit(0);
        } else {
            System.out.println("✅ Sikeres bejelentkezés! Felhasználó: " + currentUser.getUsername());
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
        System.out.println("5. Mentés text fájlba");
        System.out.println("6. Betöltés text fájlból");
        System.out.println("7. Mentés dat fájlba");
        System.out.println("8. Betöltés dat fájlból");
        System.out.println("9. Mentés adatbázisba");
        System.out.println("10. Betöltés adatbázisból");
        System.out.println("11. Kilépés");
    }

    private void handleMenuChoice(int choice) {
        String[] permissions = {
                "addBook", "deleteBook", "listBooks", "searchBooks", "saveToFile", "loadFromFile",
                "saveToBinaryFile", "loadFromBinaryFile", "saveToDatabase", "loadFromDatabase"
        };
        Runnable[] actions = {
                this::addBook, this::deleteBook, this::listBooks, this::searchBooks, this::saveToFile,
                this::loadFromFile, this::saveToBinaryFile, this::loadFromBinaryFile, this::saveToDatabase,
                this::loadFromDatabase
        };
        if (choice == 11) {
            System.exit(0);
        } else if (choice > 0 && choice <= permissions.length) {
            if (AccessControl.hasPermission(currentUser.getRole(), permissions[choice - 1])) {
                actions[choice - 1].run();
            } else {
                System.out.println("❌ Nincs jogosultságod ehhez a művelethez!");
            }
        } else {
            System.out.println("❌ Érvénytelen választás!");
        }
    }

    /**
     * Hozzáad egy új könyvet a katalógushoz, miután a felhasználó megadta a könyv adatait.
     */
    public void addBook() {
        try {
            System.out.print("Cím: ");
            String title = scanner.nextLine();
            if (title == null || title.trim().isEmpty()) {
                System.out.println("❌ A cím nem lehet üres!");
                return;
            }

            System.out.print("Szerző(k) (vesszővel elválasztva): ");
            String authorsInput = scanner.nextLine();
            if (authorsInput == null || authorsInput.trim().isEmpty()) {
                System.out.println("❌ Legalább egy szerzőt meg kell adni!");
                return;
            }

            Set<String> authorsSet = new HashSet<>(Arrays.asList(authorsInput.split(",")));

            System.out.print("Kiadás éve: ");
            int year;
            try {
                year = Integer.parseInt(scanner.nextLine());
                if (year < 0 || year > Calendar.getInstance().get(Calendar.YEAR)) {
                    System.out.println("❌ Érvénytelen kiadási év!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ A kiadási évnek számnak kell lennie!");
                return;
            }

            System.out.print("Ár: ");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("❌ Az ár nem lehet negatív!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Az árnak számnak kell lennie!");
                return;
            }

            // Könyv létrehozása
            Book book = new Book(title, authorsSet, year, price);

            // Ellenőrzés, hogy a könyv már létezik-e
            if (catalog.searchByTitle(title) == null) {
                bookService.addBook(book);
                catalog.addBook(book);
                System.out.println("✅ Könyv sikeresen hozzáadva.");
            } else {
                System.out.println("❌ A könyv már létezik a katalógusban.");
            }
        } catch (Exception e) {
            System.out.println("❌ Hiba történt a könyv hozzáadása közben: " + e.getMessage());
        }
    }


    /**
     * Töröl egy könyvet azonosítója alapján a katalógusból.
     */
    private void deleteBook() {
        try {
            System.out.print("Törlendő könyv ID: ");
            String bookId = scanner.nextLine();
            bookService.deleteBook(bookId);
            catalog.deleteBook(bookId);
            System.out.println("A könyv törölve.");
        } catch (Exception e) {
            System.out.println("Hiba: " + e.getMessage());
        }
    }


    /**
     * Listázza az összes könyvet a katalógusból, és kiírja azokat a konzolra.
     */
    private void listBooks() {
        bookService.listBooks().forEach(book -> System.out.println(book.getItemInfo()));
    }

    /**
     * Keres egy könyvet a cím alapján, és megjeleníti a találatokat a konzolon.
     */
    private void searchBooks() {
        System.out.print("Keresett könyv cím: ");
        bookService.searchBook(scanner.nextLine()).forEach(book -> System.out.println(book.getItemInfo()));
    }

    /**
     * Ment egy fájlba az összes könyvet a katalógusból.
     * A fájl neve books.txt.
     */
    private void saveToFile() {
        try {
            bookService.saveBooksToFile();
            System.out.println("✅ Könyvek sikeresen mentve a fájlba.");
        } catch (IOException e) {
            System.out.println("❌ Hiba történt a fájl mentésekor: " + e.getMessage());
        }
    }

    /**
     * Menti egy binaris fájlba az összes könyvet a katalógusból.
     * A fájl neve books.txt.
     */
    private void saveToBinaryFile() {
        try {
            bookService.saveBooksToBinaryFile();
            System.out.println("✅ Könyvek sikeresen mentve a fájlba.");
        } catch (IOException e) {
            System.out.println("❌ Hiba történt a fájl mentésekor: " + e.getMessage());
        }
    }


    /**
     * Betölt könyveket egy text fájlból.
     * A fájl neve books.dat.
     */
    private void loadFromFile() {
        try {
            bookService.loadBooksFromFile();
            System.out.println("✅ Könyvek sikeresen betöltve a fájlból.");
        } catch (IOException e) {
            System.out.println("❌ Hiba történt a fájl betöltésekor: " + e.getMessage());
        }
    }

    /**
     * Betölt könyveket egy binaris fájlból, és elmenti őket az in-memory adatbázisba.
     * A fájl neve books.dat.
     */
    private void loadFromBinaryFile() {
        try {
            bookService.loadBooksFromBinaryFile();
            System.out.println("✅ Könyvek sikeresen betöltve a fájlból.");
        } catch (IOException e) {
            System.out.println("❌ Hiba történt a fájl betöltésekor: " + e.getMessage());
        }
    }

    private void loadFromDatabase() {
        try {
            bookService.loadBooksFromDatabase();
        } catch (SQLException e) {
            System.out.println("❌ Hiba történt az adatbázisból való betöltés során: " + e.getMessage());
        }
    }


    /**
     * Ment egy könyvet az adatbázisba az összes könyv listája alapján.
     */
    private void saveToDatabase() {
        try {
            bookService.saveBooksToDatabase();
            System.out.println("✅ Mentés adatbázisba sikeres.");
        } catch (SQLException e) {
            System.out.println("❌ Hiba történt a könyv mentésekor az adatbázisba: " + e.getMessage());
        }
    }
}
