package kikaha.app.routes;

import io.netty.handler.codec.http.HttpHeaders;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.app.services.S3;
import kikaha.core.modules.http.WebResource;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.HttpResponseBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.channels.StreamSinkChannel;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

@Singleton
@WebResource( path="/s3", method="GET" )
public class S3Resource implements HttpHandler {

    private final static Logger logger = LoggerFactory.getLogger(S3Resource.class);
    private final static AsyncHttpClient httpClient = asyncHttpClient(config()
            .setMaxConnections(2000)
            .setMaxConnectionsPerHost(2000)
            .setPooledConnectionIdleTimeout(100)
            .setConnectionTtl(500)
    );

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        // Please consider run blocking operations on Undertow's Worker Threads, instead of Undertow's IO Thread.
        // Even under mid-level load on your server, you could face some IO issues if you don't do it.
        // Using Undertow's API, one should always rely on the following pattern:
        if ( !exchange.isInIoThread() ){
            exchange.dispatch( this );
            return;
        }

        try {
            S3.AwsS3Request awsS3Request = S3.get("README.md");

            //if content length is known (or will be known later... set a transfer encoding)
            exchange.getResponseHeaders().add(Headers.TRANSFER_ENCODING, Headers.IDENTITY.toString());
            exchange.getResponseHeaders().add(Headers.CONNECTION, Headers.KEEP_ALIVE.toString());
            final StreamSinkChannel channel = exchange.getResponseChannel();

            BoundRequestBuilder boundRequestBuilder = httpClient.prepareGet(S3.getS3Uri() + "/jay/README.md");
            for(String key: awsS3Request.getHeaders().keySet()) {
                //logger.info(key+"=>"+awsS3Request.getHeaders().get(key));
                boundRequestBuilder.addHeader(key, awsS3Request.getHeaders().get(key));
            }

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
            e.printStackTrace();
        }
    }
}