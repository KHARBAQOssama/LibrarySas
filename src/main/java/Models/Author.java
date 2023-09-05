package Models;

public class Author extends Model {
    private int id;
    private int name;
    private boolean deleted;

    public Author(String name){

    }
    @Override
    protected Author[] getAll() {
        return null;
    }

    @Override
    protected Author get(int id) {
        return null;
    }

    @Override
    protected Author create() {
        return null;
    }

    @Override
    protected Author update() {
        return null;
    }

    @Override
    protected Author[] search(String value) {
        return null;
    }

    @Override
    protected void delete(int id) {

    }
}
