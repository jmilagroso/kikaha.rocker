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
    public rocker.RockerTemplate render() {
        RockerRuntime.getInstance().setReloading(true);


        return new rocker.RockerTemplate()
                .setTemplateName( "views/index.rocker.html" )
                .setObjects("Current time in millis is");
    }

}