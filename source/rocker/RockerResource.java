package rocker;

import kikaha.urouting.api.GET;
import kikaha.urouting.api.Path;
import kikaha.urouting.api.PathParam;
import kikaha.urouting.api.Response;
import javax.inject.Singleton;

@Singleton
public class RockerResource {

    final Object params = new Object();

    @GET
    @Path( "{templatePath}.do" )
    public Response renderTemplate(
            @PathParam( "templatePath" ) final String templatePath )
    {
        return RockerResponse.ok()
                .templateName( templatePath + ".rocker.html" )
                .paramObject( params );
    }
}