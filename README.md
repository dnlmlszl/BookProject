# Könyv Katalógus Kezelő Rendszer

Ez egy Java alapú könyv katalógus kezelő rendszer, amely lehetővé teszi a felhasználók számára könyvek kezelését. A rendszer különféle funkciókat kínál, mint például könyvek hozzáadása, törlése, keresése és listázása. Emellett lehetőség van adatok mentésére és betöltésére fájlokból és adatbázisból. A rendszer szerepalapú jogosultságkezelést alkalmaz a különböző műveletekhez.

## Funkciók

- **Felhasználói Hitelesítés**: A felhasználók felhasználónév és jelszó segítségével jelentkezhetnek be a rendszerbe.
- **Szerepalapú Jogosultságkezelés**: Minden művelethez (hozzáadás, törlés, listázás, keresés, mentés/betöltés) a felhasználó szerepe alapján kerülnek hozzárendelésre a jogosultságok.
- **Könyvkezelés**: A felhasználók:
    - Könyvet adhatnak hozzá a katalógushoz.
    - Könyvet törölhetnek a katalógusból.
    - Könyveket listázhatnak a katalógusból.
    - Könyveket kereshetnek cím alapján.
- **Adatmentés és Betöltés**:
    - Katalógus mentése szöveges fájlba.
    - Katalógus betöltése szöveges fájlból.
    - Katalógus mentése binary fájlba.
    - Katalógus betöltése binary fájlból.
    - Katalógus mentése adatbázisba.
    - Katalógus betöltése adatbázisból.

## Használat

### Főmenü

A felhasználók a bejelentkezést követően az alábbi lehetőségek közül választhatnak:

1. **Könyv hozzáadása**: Új könyv hozzáadása a katalógushoz.
2. **Könyv törlése**: Könyv törlése a katalógusból.
3. **Könyvek listázása**: A katalógus összes könyvének listázása.
4. **Könyv keresése**: Könyv keresése a cím alapján.
5. **Mentés fájlba**: A könyvek mentése egy szöveges fájlba.
6. **Betöltés fájlból**: Könyvek betöltése egy szöveges fájlból.
7. **Mentés fájlba**: A könyvek mentése egy binary fájlba.
8. **Betöltés fájlból**: Könyvek betöltése egy binary fájlból.
9. **Mentés adatbázisba**: A könyvek mentése adatbázisba.
10. **Betöltés adatbázisból:** Könyvek betöltése adatbázisból.
11. **Kilépés**: A program bezárása.

### Szerepalapú Jogosultságkezelés

A különböző szerepekhez eltérő jogosultságok tartoznak, és minden művelet a felhasználó szerepe alapján van engedélyezve. Például:
- Csak azok a felhasználók adhatnak hozzá könyvet, akik rendelkeznek a szükséges jogosultsággal.
- Hasonlóan a törlés, listázás, mentés és betöltés jogosultságai is a szerep alapján vannak meghatározva.

### Példa Működés

1. **Bejelentkezés**:
    - A program indításakor a felhasználóknak be kell jelentkezniük felhasználónév és jelszó megadásával.
    - Ha a belépési adatok helyesek, a felhasználó hozzáférést nyer a főmenühöz.

2. **Könyv Hozzáadása**:
    - A felhasználó beírja a könyv címét, szerzőjét, kiadási évét és árát.
    - A rendszer validálja az adatokat, majd hozzáadja a könyvet a katalógushoz és elmenti az adatbázisba.

3. **Adatok Mentése**:
    - A felhasználók bármikor menthetik a katalógust fájlba vagy adatbázisba.

## Projekt Konfiguráció

A projekt használatához szükséges egy **MySQL adatbázis** létrehozása és a megfelelő táblák feltöltése.

### Adatbázis és Táblák Létrehozása

A rendszer működéséhez szükséges adatbázis és táblák létrehozásához futtasd a következő SQL scriptet.

1. **Lépj be a MySQL adatbázisba**:
    ```bash
    mysql -u root -p
    ```

2. **Futtasd a scriptet** az adatbázis és táblák létrehozásához. Az alábbi SQL parancsokat használd:

    ```sql
    -- 1. Létrehozzuk az adatbázist
    CREATE DATABASE IF NOT EXISTS BookCatalog;

    -- 2. Választjuk a könyv katalógus adatbázist
    USE BookCatalog;

    -- 3. Létrehozzuk a "Role" táblát a felhasználói szerepek tárolásához
    CREATE TABLE IF NOT EXISTS Role (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(50) NOT NULL
    );

    -- 4. Létrehozzuk a "User" táblát a felhasználók tárolásához
    CREATE TABLE IF NOT EXISTS User (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        password VARCHAR(255) NOT NULL,
        role_id INT,
        FOREIGN KEY (role_id) REFERENCES Role(id)
    );

    -- 5. Létrehozzuk a "Book" táblát a könyvek adataihoz
    CREATE TABLE IF NOT EXISTS Book (
        id INT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(100) NOT NULL,
        author VARCHAR(100) NOT NULL,
        publication_year INT NOT NULL,
        price DECIMAL(10, 2) NOT NULL
    );

    -- 6. Létrehozzuk a "CatalogItem" táblát, ami egy könyv másolatát reprezentálja
    CREATE TABLE IF NOT EXISTS CatalogItem (
        id INT AUTO_INCREMENT PRIMARY KEY,
        book_id INT,
        availability BOOLEAN DEFAULT TRUE,
        price DECIMAL(10, 2),
        FOREIGN KEY (book_id) REFERENCES Book(id)
    );

    -- 7. Betöltjük a szerepeket a "Role" táblába
    INSERT INTO Role (name) VALUES ('ADMIN');
    INSERT INTO Role (name) VALUES ('USER');
    INSERT INTO Role (name) VALUES ('GUEST');

    -- 8. Betöltünk néhány felhasználót a "User" táblába
    INSERT INTO User (username, password, role_id) VALUES ('admin', 'admin_password', 1);
    INSERT INTO User (username, password, role_id) VALUES ('user1', 'user1_password', 2);
    INSERT INTO User (username, password, role_id) VALUES ('guest1', 'guest1_password', 3);

    -- 9. Betöltünk néhány könyvet a "Book" táblába
    INSERT INTO Book (title, author, publication_year, price) VALUES ('Könyv 1', 'Szerző 1', 2020, 1500.00);
    INSERT INTO Book (title, author, publication_year, price) VALUES ('Könyv 2', 'Szerző 2', 2019, 2000.00);
    INSERT INTO Book (title, author, publication_year, price) VALUES ('Könyv 3', 'Szerző 3', 2018, 2500.00);

    -- 10. Betöltünk néhány katalogizált könyvet a "CatalogItem" táblába
    INSERT INTO CatalogItem (book_id, price) VALUES (1, 1500.00);
    INSERT INTO CatalogItem (book_id, price) VALUES (2, 2000.00);
    INSERT INTO CatalogItem (book_id, price) VALUES (3, 2500.00);
    ```

### Konfigurációs fájl

A projekt használatához szükséges egy config.properties fájl, amely tartalmazza az adatbázis kapcsolatokat.
Hozz létre egy új fájlt a projekt gyökérkönyvtárában, és nevezd el config.properties-nek.
A fájlban add meg a szükséges adatbázis kapcsolatokat, például:

```bash
1. **Fő adatbázis kapcsolati beállítások**:
- **db.url=<ADATBAZIS_URL>**
- **db.user=<ADATBAZIS_FELHASZNALONEV>**
- **db.password=<ADATBAZIS_JELSZO>**

2. **Teszt adatbázis kapcsolati beállítások**:
- **test.db.url=<TESZT_ADATBAZIS_URL>**
- **test.db.user=<TESZT_FELHASZNALONEV>**
- **test.db.password=<TESZT_JELSZO>**

```

Cseréld ki a <ADATBAZIS_URL>, <ADATBAZIS_FELHASZNALONEV>, <ADATBAZIS_JELSZO>, <TESZT_ADATBAZIS_URL>, 
<TESZT_FELHASZNALONEV> és <TESZT_JELSZO> helyeket a saját adatbázis kapcsolati információidra.

## Használatba vétel

A rendszer beállítása után a felhasználók a következő lehetőségek közül választhatnak a könyvek kezelésére:

- **Könyv hozzáadása**: A felhasználó könyveket adhat hozzá, megadva azok adatait (cím, szerzők, kiadási év, ár).
- **Könyv törlése**: A felhasználó törölhet könyvet az ID alapján.
- **Könyv keresése**: A felhasználó kereshet könyveket cím alapján.
- **Mentés fájlba**: A könyveket text vagy binary fájlba menthetők.
- **Betöltés fájlból**: A könyvek betölthetők text vagy binary fájlból.
- **Mentés adatbázisba**: A könyvek menthetők adatbázisba.

## Alkalmazott Osztályok és Csomagok

- **Controller**:
    - `CatalogController`: Kezeli a felhasználói interakciókat és a katalógus műveleteit (hozzáadás, törlés, listázás, keresés).

- **Model**:
    - `Book`: A könyv modellje, amely tartalmazza a könyv címét, szerzőit, kiadási évét és árát. Ez a modell a könyvek adatait tárolja.
    - `User`: A felhasználó modellje, amely tartalmazza a felhasználó szerepét és hitelesítési adatait (például jelszót és felhasználónevet).
    - `CatalogItem`: Egy elem a könyv katalógusban, amely a könyv adatait és a hozzá kapcsolódó információkat tárolja, mint például a könyv elérhetősége, ára és esetleg egyéb metaadatok.
    - `Item`: Általános elem osztály, amely a katalógusokhoz és egyéb listákhoz szükséges alapadatokat tartalmazza, mint például az azonosítót és a típust.
    - `Role`: A felhasználói szerepkör típusát leíró modell. Különböző szerepeket tartalmaz, mint például ADMIN, USER és GUEST, amelyek a hozzáférést szabályozzák.

- **Service**:
    - `BookCatalog`: Kezeli a könyv katalógust és annak műveleteit, mint például a könyvek hozzáadását, törlését, keresését, és az összes könyv listázását.
    - `BookService`: Az üzleti logikát tartalmazza, amely az egyes könyvek kezelését végzi, mint a könyv információinak frissítése, törlése, vagy a keresési funkciók végrehajtása.
    - `ÀccessControl`: A hozzáférés-ellenőrzést végző szolgáltatás, amely meghatározza, hogy a különböző szerepkörök milyen műveleteket végezhetnek el (például könyv törlés, fájl mentés stb.).
    - `UserManager`: Kezeli a felhasználói hitelesítést és bejelentkezést. Az adminisztrátorok és felhasználók jogosultságait is ellenőrzi, valamint a regisztrációs és bejelentkezési folyamatokat kezeli.
    - `DatabaseHandler`: Kezeli az adatbázis műveleteit, mint a könyvek mentését, betöltését, valamint az adatbázisból való adatkiolvasást. Feladatai közé tartozik az adatbázisba történő írás és az adatbázisból való olvasás könyv-, felhasználó- és egyéb kapcsolódó adatokkal kapcsolatosan.

## Hozzájárulás

Bármilyen hozzájárulás üdvözlendő! Kérlek, forkold a repót, hozz létre egy új ágat, végezd el a módosításokat, majd küldj egy pull requestet.

## Licenc

Ez a projekt az MIT Licenc alatt áll - a [LICENSE](LICENSE) fájl tartalmazza a részleteket.
