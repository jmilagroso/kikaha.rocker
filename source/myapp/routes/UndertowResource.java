package myapp.routes;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.core.modules.http.WebResource;
import myapp.models.BufferEncoder;
import myapp.models.FastByteArrayOutputStream;
import myapp.models.Probe;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.text.NumberFormat;
import java.util.List;

@Singleton
@WebResource( path="/undertow", method="GET" )
public class UndertowResource implements HttpHandler {
    private final static NumberFormat nf = NumberFormat.getInstance();

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {

        GenericType<List<Probe>> genericProbe = new GenericType<List<Probe>>(){};
        List<Probe> probes = ClientBuilder.newClient()
                .target("http://localhost:9000/compress")
                .request().accept(MediaType.APPLICATION_JSON)
                .get(genericProbe);


        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/jwk-set+json;charset=UTF-8");

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 150000 );
        BufferEncoder e = new BufferEncoder(fastByteArrayOutputStream);

        exchange.getResponseSender().send(e.encode(probes));
    }




}
