package myapp.routes;

import kikaha.urouting.api.*;
import javax.inject.*;
import com.fizzed.rocker.runtime.RockerRuntime;
import myapp.models.Author;
import myapp.models.User;

@Singleton
@Path( "/" )
public class HomeResource {

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderHome() {
        RockerRuntime.getInstance().setReloading(true);

        // Sample data
        User u = new User();
        u.name = "Peter";
        u.age = 31;

        // Sample data
        Author a = new Author();
        a.id = 1;
        a.name = "John";
        a.place = "CA";
        a.role = "Guest";

        return new rocker.RockerTemplate().templateName( "views/index.rocker.html" ).setParamContent(u, a);
    }

}