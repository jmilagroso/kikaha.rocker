package kikaha.app.routes;

import com.fizzed.rocker.Rocker;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import kikaha.core.modules.http.WebResource;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;



@Singleton
@WebResource( path="/async", method="GET" )
public class AsyncResource implements HttpHandler {

    Logger logger = LoggerFactory.getLogger(UndertowResource.class);

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        try {
            AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
            asyncHttpClient.prepareGet("http://registry.jsonresume.org/jmilagroso.json").execute(new AsyncCompletionHandler<Response>(){

                @Override
                public void onThrowable(Throwable t){
                    // Something wrong happened.
                    logger.error(t.getMessage());
                }

                @Override
                public Response onCompleted(org.asynchttpclient.Response response) throws Exception {

                     logger.info(response.getResponseBody(Charset.defaultCharset()));

                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                    exchange.getResponseSender().send(Rocker.template(
                            "kikaha/app/views/async.rocker.html", response.getResponseBody(Charset.defaultCharset()))
                            .render().toString()
                    );

                    return null;
                }
            });

            exchange.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
