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