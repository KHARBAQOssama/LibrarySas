package Models;


public class User extends Model{
    private int id;
    private String name;
    private int membership_number;
    private String phone;
    private boolean deleted;
    public  User(String newName,String newPhone){

    }
    @Override
    protected User[] getAll() {
        return null;
    }

    @Override
    protected User get() {
        return null;
    }

    @Override
    protected void create() {
    }

    @Override
    protected User update() {
        return null;
    }

    @Override
    protected User[] search(String value) {
        return null;
    }

    @Override
    protected void delete() {

    }
}
