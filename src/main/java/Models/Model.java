package Models;

public abstract class Model {
    protected abstract Model[] getAll();
    protected abstract Model get();
    protected abstract void create();
    protected abstract Model update();
    protected abstract void delete();
    protected abstract Model[] search(String value);

}
