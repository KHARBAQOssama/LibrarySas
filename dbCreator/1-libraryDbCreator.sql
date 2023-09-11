DROP DATABASE IF EXISTS LibraryMiniSas;
CREATE DATABASE IF NOT EXISTS LibraryMiniSas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE LibraryMiniSas;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    membership_number INT NOT NULL,
    phone VARCHAR(15),
    deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS authors (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS books (
    title VARCHAR(255) NOT NULL,
    ISBN VARCHAR(13) PRIMARY KEY NOT NULL,
    quantity INT NOT NULL,
    available INT NOT NULL,
    lost INT DEFAULT 0,
    author_id INT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (author_id) REFERENCES authors(id)
);


CREATE TABLE IF NOT EXISTS BorrowedBooks (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    book_ISBN VARCHAR(13) NOT NULL,
    user_id INT  NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    returned BOOLEAN DEFAULT 0,
    lost BOOLEAN DEFAULT 0,
    FOREIGN KEY (book_ISBN) REFERENCES books(ISBN),
    FOREIGN KEY (user_id) REFERENCES users(id)
);


DELIMITER ;;
CREATE TRIGGER book_borrowed
AFTER INSERT ON borrowedbooks
FOR EACH ROW
BEGIN
    IF NEW.returned = 0 THEN
        UPDATE books
        SET available = available - 1
        WHERE ISBN = NEW.book_ISBN;
    END IF;
END;;   
DELIMITER ;

