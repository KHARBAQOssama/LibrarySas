package Models;

public abstract class Model {
    protected abstract Model[] getAll();
    protected abstract Model get(int id);
    protected abstract Model create();
    protected abstract Model update();
    protected abstract void delete(int id);
    protected abstract Model[] search(String value);

}
