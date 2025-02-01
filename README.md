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
7. **Mentés adatbázisba**: A könyvek mentése adatbázisba.
8. **Kilépés**: A program bezárása.

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
- **Mentés fájlba**: A könyvek fájlba menthetők.
- **Betöltés fájlból**: A könyvek betölthetők fájlból.
- **Mentés adatbázisba**: A könyvek menthetők adatbázisba.

## Alkalmazott Osztályok és Csomagok

- **Controller**:
    - `CatalogController`: Kezeli a felhasználói interakciókat és a katalógus műveleteit (hozzáadás, törlés, listázás, keresés).

- **Model**:
    - `Book`: A könyv modellje, amely tartalmazza a könyv címét, szerzőit, kiadási évét és árát.
    - `User`: A felhasználó modellje, amely tartalmazza a felhasználó szerepét és hitelesítési adatait.

- **Service**:
    - `BookCatalog`: Kezeli a könyv katalógust és annak műveleteit.
    - `UserManager`: Kezeli a felhasználói hitelesítést és bejelentkezést.
    - `DatabaseHandler`: Kezeli az adatbázis műveleteit, mint a könyvek mentését és betöltését.
    - `FileHandler`: Kezeli a fájlba mentést és fájlból történő betöltést.

## Hozzájárulás

Bármilyen hozzájárulás üdvözlendő! Kérlek, forkold a repót, hozz létre egy új ágat, végezd el a módosításokat, majd küldj egy pull requestet.

## Licenc

Ez a projekt az MIT Licenc alatt áll - a [LICENSE](LICENSE) fájl tartalmazza a részleteket.
