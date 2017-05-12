package myapp.services;

import myapp.models.Collection;

import javax.inject.*;
import java.util.List;

/**
 * Created by jay on 5/10/17.
 */
public class Builder {

    @Inject
    public Paginator paginator;

    @Inject
    public Collection builder(List<?> data, Integer perPage, Integer page) {
        int size = data.size();
        // Paginator
        paginator = new Paginator();
        paginator.handle(perPage,  size, page);

        Collection collection = new Collection();
        collection.data = (size > 0) ? data.subList(paginator.startIndex, paginator.endIndex) : null;

        return collection;
    }

}
