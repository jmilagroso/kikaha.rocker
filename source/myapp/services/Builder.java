package myapp.services;

import myapp.models.Forum;
import myapp.models.Post;
import javax.inject.*;
import java.util.List;

/**
 * Created by jay on 5/10/17.
 */
public class Builder {

    @Inject
    public Paginator paginator;

    @Inject
    public Forum builder(List<Post> posts, Integer perPage, Integer page) {
        // Paginator
        paginator = new Paginator();
        paginator.handle(perPage,  posts.size(), page);

        Forum forum = new Forum();
        forum.posts = posts.subList(paginator.startIndex, paginator.endIndex);

        return forum;
    }

}
