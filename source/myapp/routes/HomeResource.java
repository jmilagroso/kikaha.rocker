package myapp.routes;

import kikaha.urouting.api.*;
import javax.inject.*;
import com.fizzed.rocker.runtime.RockerRuntime;

@Singleton
@Path( "/" )
public class HomeResource {

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderHome() {
        RockerRuntime.getInstance().setReloading(true);

        return new rocker.RockerTemplate().templateName( "views/index.rocker.html" ).setParamContent("Current time in millis is");
    }

}