package Models;

public class Book extends Model{
    private String title ;
    private String ISBN ;
    private int quantity;
    private int available ;
    private int lost ;
    private Author author;
    private boolean deleted;

    public Book(String newTitle,String newISBN,int newQuantity,String Author){

    }

    public Book[] availableBooks(){
        return null;
    }
    @Override
    protected Book[] getAll() {
        return null;
    }

    @Override
    protected Book get(int id) {
        return null;
    }

    protected Book get(String ISBN){
        return null;
    }
    @Override
    protected Book create() {
        return null;
    }

    @Override
    protected Book update() {
        return null;
    }

    @Override
    protected Book[] search(String value) {
        return null;
    }

    @Override
    protected void delete(int id) {

    }

    protected void delete(String ISBN){

    }
}
