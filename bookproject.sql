-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: localhost:8889
-- Létrehozás ideje: 2025. Feb 08. 13:40
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
('263f756c-f487-435f-b83c-7fcc8dc0c825', 'Nagy durranas', 2011, 11);

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
('0e796100-b03a-4b47-b98d-f21c11d98779', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402d'),
('263f756c-f487-435f-b83c-7fcc8dc0c825', '5c09c790-ac3e-4532-9bbf-1d5bb1e5402d'),
('0e796100-b03a-4b47-b98d-f21c11d98779', '81bce2ba-8b3b-4992-afa6-11c689b1c55d');

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
