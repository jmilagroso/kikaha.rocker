package kikaha.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {
    public Integer id;
    public Integer author_id;
    public String title;
    public String body;
    public String image_url;
    public String created_at;

    @SerializedName("authors")
    public Author author;

}
