package kikaha.app.routes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kikaha.app.services.Security;
import kikaha.urouting.api.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Path( "/jwt" )
public class JWTResource {

    private static Logger logger = LoggerFactory.getLogger(JWTResource.class);

    @GET
    @Path( "/{id}/{claim}/{issuer}" )
    @Produces( Mimes.JSON )
    public Map<String, String> render(@PathParam("id") String id,
                                      @PathParam("claim") String claim,
                                      @PathParam("issuer") String issuer) {

        final String token = JWT.create()
                .withJWTId(id)
                .withClaim("name", claim)
                .withIssuer(issuer)
                .sign(Security.algorithm);

        Map<String,String> map = new HashMap<>();
        map.put("token", token);

        return map;
    }

    @GET
    @Path( "/verify/{token}/{id}/{claim}/{issuer}" )
    @Produces( Mimes.JSON )
    public Map<String, Boolean> verify(@PathParam("token") String token,
                                      @PathParam("id") String id,
                                      @PathParam("claim") String claim,
                                      @PathParam("issuer") String issuer) {

        boolean isValidToken = false;

        try {
            isValidToken = Security.valid(token, id, claim, issuer);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
        }

        Map<String,Boolean> map = new HashMap<>();
        map.put("verified", isValidToken);

        return map;
    }

}