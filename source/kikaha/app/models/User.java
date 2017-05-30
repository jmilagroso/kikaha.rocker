package kikaha.app.models;

public class User {
    final Long id = System.currentTimeMillis();
    public String name;
    public Integer age;

    public Long getId(){
        return this.id;
    }

    protected void finalize() throws Throwable {
        this.name = null;
        this.age = null;

        super.finalize();
    }
}