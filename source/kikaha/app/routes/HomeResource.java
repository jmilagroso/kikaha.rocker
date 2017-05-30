package kikaha.app.routes;

import kikaha.urouting.api.*;
import javax.inject.*;

@Singleton
@Path( "/" )
public class HomeResource {

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate render() {
        return new rocker.RockerTemplate()
                .setTemplateName("kikaha/app/views/index.rocker.html")
                .setObjects("Current time in millis is");
    }

}