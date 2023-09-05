package Models;

import java.util.Date;

public class BorrowedBook extends Model{

    private int id;
    private Book book;
    private User user;
    private Date start_date;
    private Date end_date;
    private boolean returned;
    private boolean lost;

    public BorrowedBook(Book book ,Author author){

    }
    @Override
    protected BorrowedBook[] getAll() {
        return null;
    }

    @Override
    protected BorrowedBook get(int id) {
        return null;
    }

    @Override
    protected BorrowedBook create() {
        return null;
    }

    @Override
    protected BorrowedBook update() {
        return null;
    }

    @Override
    protected BorrowedBook[] search(String value) {
        return null;
    }

    @Override
    protected void delete(int id) {

    }
}
