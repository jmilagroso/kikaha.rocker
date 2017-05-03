package minerva;

import com.fizzed.rocker.*;
//import kikaha.mustache.*;
import kikaha.urouting.api.*;

import javax.activation.MimeType;
import javax.inject.*;
import java.io.IOException;
import java.util.*;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;

import com.fizzed.rocker.runtime.RockerRuntime;
import rocker.*;

@Singleton
@Path( "home" )
public class HomeResource {

    /*@GET
    @Path( "chart" )
    @Produces( Mimes.HTML )
    public MustacheTemplate renderProfileTemplate() {
        return new MustacheTemplate()
                .templateName( "home.mustache" )
                .paramObject( new Home() );
    }*/

    @GET
    @Path( "rocker" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderMe() {
        RockerRuntime.getInstance().setReloading(true);

        User x = new User();
        x.name = "Peter";
        x.age = 31;

        return new rocker.RockerTemplate().templateName( "views/index.rocker.html" ).paramContent(x);
    }

}