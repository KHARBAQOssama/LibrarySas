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


-- triggers to facilate the available books calculation 

DELIMITER ;;
CREATE TRIGGER book_borrowed
AFTER INSERT ON borrowedbooks
FOR EACH ROW
BEGIN
    UPDATE books
    SET available = available - 1
    WHERE ISBN = NEW.book_ISBN;
END;;

DELIMITER ;

DELIMITER ;;
CREATE TRIGGER borrowedbooks_updated
BEFORE UPDATE ON borrowedbooks
FOR EACH ROW
BEGIN
    IF (NEW.returned = 1) THEN
        IF (NEW.lost = 0) THEN
            UPDATE books
            SET available = available + 1
            WHERE ISBN = NEW.book_ISBN;
        ELSE
            UPDATE books
            SET available = available + 1,
                lost = lost - 1
            WHERE ISBN = NEW.book_ISBN;
        END IF;
    ELSEIF (NEW.lost = 1) THEN
        UPDATE books
        SET lost = lost + 1
        WHERE ISBN = NEW.book_ISBN;
    END IF;
END;;

DELIMITER ;
-- Event to make a book lost if the end date passed

DELIMITER ;;
CREATE EVENT checkLostBooksEvery2Hours
ON SCHEDULE EVERY 2 HOUR
DO
BEGIN
    UPDATE BorrowedBooks
    SET lost = TRUE
    WHERE end_date < NOW() AND lost = FALSE AND returned = FALSE;
END;
;;
DELIMITER ;
-- command that turn on the event 
SET GLOBAL event_scheduler="ON"