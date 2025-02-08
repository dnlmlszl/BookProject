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
-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: localhost:8889
-- Létrehozás ideje: 2025. Feb 08. 14:16
-- Kiszolgáló verziója: 8.0.35
-- PHP verzió: 8.2.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `bookproject`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `authors`
--

CREATE TABLE `authors` (
  `id` varchar(36) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb3_hungarian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_hungarian_ci;

--
-- A tábla adatainak kiíratása `authors`
--

INSERT INTO `authors` (`id`, `name`) VALUES
('f0288c2d-2bb8-4e43-8e2f-7d1fc9ee1217', 'F. Scott Fitzgerald'),
('000c0a0a-304e-4912-adee-547f40eab1d2', 'George Orwell'),
('9b4f2e56-348b-4a81-a069-6844ae7fd07f', 'Harper Lee'),
('84d00ec7-18cd-49ec-bd02-43017c2f69da', 'Herman Melville'),
('afe96dd8-9293-4555-a0f9-e1fe34af72d3', 'J.D. Salinger'),
('13e7494f-78f3-4fa5-b030-33c6fed1b8e8', 'Jane Austen'),
('5c09c790-ac3e-4532-9bbf-1d5bb1e5402d', 'John F. Kennedy'),
('81bce2ba-8b3b-4992-afa6-11c689b1c55d', 'Ronald Reagen');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `books`
--

CREATE TABLE `books` (
  `id` varchar(36) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `publicationYear` int NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_hungarian_ci;

--
-- A tábla adatainak kiíratása `books`
--

INSERT INTO `books` (`id`, `title`, `publicationYear`, `price`) VALUES
('0e796100-b03a-4b47-b98d-f21c11d98779', 'It\'s my life featured Dr. Alban', 2022, 22),
('1f7365d1-8436-4d32-a56a-e17c634900d2', 'The Great Gatsby', 1925, 69.99),
('263f756c-f487-435f-b83c-7fcc8dc0c825', 'Nagy durranas', 2011, 11),
('52653dca-9d1b-454f-9f6e-aa20d8a75f6f', '1984', 1949, 199.99),
('76fe8c90-309d-4da1-801d-7f30b2012515', 'Moby-Dick', 1851, 89.99),
('a5906449-2ed2-4fdc-825d-948a7a1a3cba', 'Pride and Prejudice', 1813, 119.99),
('aacb40e1-e14f-484c-9133-9f8323babe57', 'The Catcher in the Rye', 1951, 59.99),
('fb813cd4-9d5f-4b32-8b9d-963d5b3e199a', 'To Kill a Mockingbird', 1960, 49.99);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `book_authors`
--

CREATE TABLE `book_authors` (
  `book_id` varchar(36) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `author_id` varchar(36) COLLATE utf8mb3_hungarian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_hungarian_ci;

--
-- A tábla adatainak kiíratása `book_authors`
--

INSERT INTO `book_authors` (`book_id`, `author_id`) VALUES
('52653dca-9d1b-454f-9f6e-aa20d8a75f6f', '000c0a0a-304e-4912-adee-547f40eab1d2'),
('a5906449-2ed2-4fdc-825d-948a7a1a3cba', '13e7494f-78f3-4fa5-b030-33c6fed1b8e8'),
('0e796100-b03a-4b47-b98d-f21c11d98779', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402d'),
('263f756c-f487-435f-b83c-7fcc8dc0c825', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402d'),
('0e796100-b03a-4b47-b98d-f21c11d98779', '81bce2ba-8b3b-4992-afa6-11c689b1c55d'),
('76fe8c90-309d-4da1-801d-7f30b2012515', '84d00ec7-18cd-49ec-bd02-43017c2f69da'),
('fb813cd4-9d5f-4b32-8b9d-963d5b3e199a', '9b4f2e56-348b-4a81-a069-6844ae7fd07f'),
('aacb40e1-e14f-484c-9133-9f8323babe57', 'afe96dd8-9293-4555-a0f9-e1fe34af72d3'),
('1f7365d1-8436-4d32-a56a-e17c634900d2', 'f0288c2d-2bb8-4e43-8e2f-7d1fc9ee1217');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(50) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb3_hungarian_ci NOT NULL,
  `role` enum('ADMIN','USER','GUEST') COLLATE utf8mb3_hungarian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_hungarian_ci;

--
-- A tábla adatainak kiíratása `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin', 'secret', 'ADMIN'),
(4, 'user', 'secret', 'USER'),
(5, 'guest', 'secret', 'GUEST');

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- A tábla indexei `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_book` (`title`,`publicationYear`),
  ADD UNIQUE KEY `id` (`id`);

--
-- A tábla indexei `book_authors`
--
ALTER TABLE `book_authors`
  ADD PRIMARY KEY (`book_id`,`author_id`),
  ADD KEY `fk_author_id` (`author_id`);

--
-- A tábla indexei `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `book_authors`
--
ALTER TABLE `book_authors`
  ADD CONSTRAINT `fk_author_id` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

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
