package myapp.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

/**
 * Created by jay on 5/9/17.
 */
@JsonAutoDetect
public class Probe implements Serializable {
    public int sex;
    public int age;
    public int x;
    public int y;
    public double lat;
    public double lng;
    public int associated;

    public int getSex() {
        return this.sex;
    }

    public int getAge() {
        return this.age;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public int getAssociated() {
        return this.associated;
    }
}
