package myapp.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jay on 5/5/17.
 */
public class Author /*extends ArrayList implements DataSerializable*/ implements Serializable {
    public Integer id;
    public String name;
    public String role;
    public String place;
    public String avatar_url;

    /*@Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(size());
        for(Object item: this){
            out.writeObject(item);
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int size = in.readInt();
        for(int k=0;k<size;k++){
            add(in.readObject());
        }
    }*/
}
