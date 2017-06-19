package kikaha.app.routes;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import kikaha.config.Config;
import kikaha.urouting.api.*;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwaggerResource  {

    // Logger
    Logger logger = LoggerFactory.getLogger(SwaggerResource.class);

    @Inject
    Config config;

    private final JsonFactory factory = new JsonFactory();
    private final ObjectMapper mapper = new ObjectMapper(factory);
    private final TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};


    @GET
    @Path( "/api-docs/model")
    @Produces( Mimes.JSON )
    public Map<String,Object> renderModel(@Context HttpServerExchange exchange) {

        String scheme = exchange.getRequestScheme();
        String host = null;
        int port = 80;
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("swagger-petstore.json")))
        ) {
            Map<String,Object> model = mapper.readValue(br, typeRef);

            URL url = new URL(exchange.getRequestURL());
            host = url.getHost();
            port = url.getPort();

            model.put("host", host + ((port <=0) ? "" : ":"+port));

            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Unable to fetch swagger.json");
    }

    @GET
    @Path( "/api-docs/ui")
    public Response renderUI(@Context HttpServerExchange exchange) {
        String scheme = exchange.getRequestScheme();
        String host = null;
        int port = 80;
        try {
            URL url = new URL(exchange.getRequestURL());
            host = url.getHost();
            port = url.getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return DefaultResponse.response()
                .statusCode( 302 )
                .header( "Location", "/webjars/swagger-ui/3.0.10/index.html?url=" +
                        scheme + "://" + host + ((port <=0) ? "" : ":"+port) + "/api-docs/model");
    }
}
