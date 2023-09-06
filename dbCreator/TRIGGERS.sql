DELIMITER ;;

CREATE TRIGGER borrowedBookUpdated AFTER UPDATE ON borrowedbooks FOR EACH ROW
BEGIN 
    IF (NEW.lost = 1 AND NEW.returned = 0) THEN
        UPDATE books
        SET lost = lost + 1
        WHERE ISBN = NEW.book_ISBN;
    ELSEIF (NEW.lost = 0 AND NEW.returned = 1) THEN
        UPDATE books
        SET returned = returned + 1
        WHERE ISBN = NEW.book_ISBN;
    ELSEIF (NEW.returned = 1 AND NEW.lost = 1) THEN
        UPDATE borrowedbooks 
        SET lost = 0;
        UPDATE books 
        SET lost = lost -1 ,
        available = available + 1
        WHERE ISBN = NEW.book_ISBN;
        END IF;
END;;

DELIMITER ;

DELIMITER ;;
CREATE TRIGGER bookBorrowed
AFTER INSERT ON borrowedbooks
FOR EACH ROW
BEGIN
    UPDATE books
    SET available = available - 1
    WHERE ISBN = NEW.book_ISBN;
END;;

DELIMITER ;