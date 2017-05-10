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
    public Forum builder(List<?> data, Integer perPage, Integer page) {
        // Paginator
        paginator = new Paginator();
        paginator.handle(perPage,  data.size(), page);

        Forum forum = new Forum();
        forum.data = data.subList(paginator.startIndex, paginator.endIndex);

        return forum;
    }

}
