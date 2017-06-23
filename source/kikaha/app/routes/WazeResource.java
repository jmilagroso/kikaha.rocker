package kikaha.app.routes;

import io.netty.handler.codec.http.HttpHeaders;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.app.services.S3;
import kikaha.core.modules.http.WebResource;
import kikaha.urouting.SimpleExchange;
import kikaha.urouting.UndertowHelper;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.HttpResponseBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.channels.StreamSinkChannel;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

@Singleton
@WebResource( path="/waze", method="GET" )
public class WazeResource implements HttpHandler {

    private final static Logger logger = LoggerFactory.getLogger(WazeResource.class);
    private final static AsyncHttpClient httpClient = asyncHttpClient(config()
            .setMaxConnections(2000)
            .setMaxConnectionsPerHost(2000)
            .setPooledConnectionIdleTimeout(100)
            .setConnectionTtl(500)
    );

    @Inject
    UndertowHelper requestHelper;

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        // Please consider run blocking operations on Undertow's Worker Threads, instead of Undertow's IO Thread.
        // Even under mid-level load on your server, you could face some IO issues if you don't do it.
        // Using Undertow's API, one should always rely on the following pattern:
        if ( !exchange.isInIoThread() ){
            exchange.dispatch( this );
            return;
        }

        SimpleExchange simpleExchange = requestHelper.simplify( exchange );

        String startLon = (simpleExchange.getQueryParameters().size() > 0) ? simpleExchange.getQueryParameter( "startLon", String.class ).toString() : "0";
        String startLat = (simpleExchange.getQueryParameters().size() > 0) ? simpleExchange.getQueryParameter( "startLat", String.class ).toString() : "0";
        String endLon = (simpleExchange.getQueryParameters().size() > 0) ? simpleExchange.getQueryParameter( "endLon", String.class ).toString() : "0";
        String endLat = (simpleExchange.getQueryParameters().size() > 0) ? simpleExchange.getQueryParameter( "endLat", String.class ).toString() : "0";

        String url = "https://www.waze.com/row-RoutingManager/routingRequest?" +
                "from=x%3A"+startLon+"+y%3A"+startLat+"&" +
                "to=x%3A"+endLon+"+y%3A"+endLat+"&" +
                "at=0&returnJSON=true&" +
                "returnGeometries=true&" +
                "returnInstructions=true&" +
                "timeout=60000&" +
                "nPaths=3&" +
                "clientVersion=4.0.0&" +
                "options=AVOID_TRAILS";
        logger.info("url:"+url);

        try {
            //if content length is known (or will be known later... set a transfer encoding)
            exchange.getResponseHeaders().add(Headers.TRANSFER_ENCODING, Headers.IDENTITY.toString());
            exchange.getResponseHeaders().add(Headers.CONNECTION, Headers.KEEP_ALIVE.toString());
            final StreamSinkChannel channel = exchange.getResponseChannel();

            BoundRequestBuilder boundRequestBuilder = httpClient.prepareGet(url);
            boundRequestBuilder.addHeader("Accept", "application/json");

            boundRequestBuilder.execute(new AsyncCompletionHandler<Response>(){
                @Override
                public final State onHeadersReceived(final HttpHeaders headers) throws Exception {
                    String contentLength = headers.get(Headers.CONTENT_LENGTH_STRING);
                    if (contentLength != null) {
                        exchange.getResponseHeaders().remove(Headers.TRANSFER_ENCODING);
                        exchange.getResponseHeaders().remove(Headers.CONNECTION);
                        exchange.getResponseHeaders().add(Headers.CONNECTION, Headers.KEEP_ALIVE.toString());
                        exchange.setResponseContentLength(Long.parseLong(contentLength));
                    }
                    return super.onHeadersReceived(headers);
                }

                @Override
                public final State onBodyPartReceived(final HttpResponseBodyPart content) throws Exception {
                    channel.write(content.getBodyByteBuffer());

                    return State.CONTINUE;
                }

                @Override
                public final void onThrowable(final Throwable t){
                    // Something wrong happened.
                    logger.error(t.getMessage());
                }

                @Override
                public final Response onCompleted(final org.asynchttpclient.Response response) throws Exception {

                    channel.shutdownWrites();
                    channel.flush();
                    channel.close();

                    return null;
                }
            });

            exchange.dispatch();
        } catch (Exception e) {
            logger.debug(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}