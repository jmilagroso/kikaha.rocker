package myapp.models;

public class User {
    final Long id = System.currentTimeMillis();
    public String name;
    public Integer age;

    public Long getId(){
        return this.id;
    }
}