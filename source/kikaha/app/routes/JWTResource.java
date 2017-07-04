package kikaha.app.routes;

import kikaha.app.services.Security;
import kikaha.config.Config;
import kikaha.urouting.api.*;
import javax.inject.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Path( "/jwt" )
public class JWTResource {

    @Inject
    Config config;

    // JWT Security
    private Security security;

    // TODO Find a way to load `loadConfig` in a middleware or constructor without invoking DI exception.

    // Prevents multiple call on Security class
    private boolean hasLoadedConfig;
    private void loadConfig() {
        if(!hasLoadedConfig) {
            security = new Security(config.getString("jwt.secret"), config.getInteger("jwt.expires-seconds", 3600));
            hasLoadedConfig = true;
        }
    }

    @GET
    @Path( "/test" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderTest() {

        loadConfig();

        return new rocker.RockerTemplate()
                .setTemplateName("kikaha/app/views/index.rocker.html")
                .setObjects("Token:"+security.token("SomeId123", "SomeClaimant", "SomeIssuer"));
    }

    // Sample generates new token with default expiration set in config (3600 sec).
    @GET
    @Path( "/{id}/{claim}/{issuer}/" )
    @Produces( Mimes.JSON )
    public Map<String, String> render(@PathParam("id") String id,
                                      @PathParam("claim") String claim,
                                      @PathParam("issuer") String issuer) {

        loadConfig();

        Map<String,String> map = new HashMap<>();
        map.put("token", security.token(id, claim, issuer));

        return map;
    }

    // Sample generates new token with expiration (in seconds) override.
    @GET
    @Path( "/{id}/{claim}/{issuer}/{expiresInSeconds}" )
    @Produces( Mimes.JSON )
    public Map<String, String> renderWithExpiration(@PathParam("id") String id,
                                      @PathParam("claim") String claim,
                                      @PathParam("issuer") String issuer,
                                      @PathParam("expiresInSeconds") Integer expiresInSeconds) {

        loadConfig();

        Map<String,String> map = new HashMap<>();
        map.put("token", security.token(id, claim, issuer, expiresInSeconds));

        return map;
    }

    // Sample token verification (as a protected resource)
    @GET
    @Path( "/verify/{id}/{claim}/{issuer}/{token}" )
    @Produces( Mimes.JSON )
    public Map<String, Boolean> verify(@PathParam("token") String token,
                                      @PathParam("id") String id,
                                      @PathParam("claim") String claim,
                                      @PathParam("issuer") String issuer) {

        loadConfig();

        boolean isValidToken = false;

        try {
            isValidToken = security.valid(token, id, claim, issuer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }

        Map<String,Boolean> map = new HashMap<>();
        map.put("verified", isValidToken);

        return map;
    }

}