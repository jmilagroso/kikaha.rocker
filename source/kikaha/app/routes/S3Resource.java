package kikaha.app.routes;

import com.fizzed.rocker.Rocker;
import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.app.services.S3;
import kikaha.core.modules.http.WebResource;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.channels.StreamSinkChannel;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by jay on 6/2/17.
 */
@Singleton
@WebResource( path="/s3", method="GET" )
public class S3Resource implements HttpHandler {

    private static ByteBuffer EMPTY_BUF = ByteBuffer.allocate(0);

    // http://sakthipriyan.com/2016/02/17/scala-code-to-access-documents-in-aws-s3-bucket.html

    Logger logger = LoggerFactory.getLogger(UndertowResource.class);

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        try {
            AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

            S3.AwsS3Request awsS3Request = S3.get("README.md");

            logger.info("S3.get(S3.getS3Uri():"+S3.getS3Uri());

            BoundRequestBuilder boundRequestBuilder = asyncHttpClient.prepareGet(S3.getS3Uri() + "/jay/README.md");
            for(String key: awsS3Request.getHeaders().keySet()) {
                logger.info(key+"=>"+awsS3Request.getHeaders().get(key));
                boundRequestBuilder.addHeader(key, awsS3Request.getHeaders().get(key));
            }

            boundRequestBuilder.execute(new AsyncCompletionHandler<Response>(){
            //asyncHttpClient.prepareGet(S3.put(S3.getS3Uri() + "/pandora", "\"<html><body><h1>Hello HTML World!</h1></body></html>\"").toString()).execute(new AsyncCompletionHandler<Response>(){

                @Override
                public State onBodyPartReceived(HttpResponseBodyPart content) throws Exception {

                    //exchange.getResponseSender().send(new String(content.getBodyPartBytes()));
                    exchange.getResponseChannel().write(content.getBodyByteBuffer());

                    return State.CONTINUE;
                }

                @Override
                public void onThrowable(Throwable t){
                    // Something wrong happened.
                    logger.error(t.getMessage());
                }

                @Override
                public Response onCompleted(org.asynchttpclient.Response response) throws Exception {

                    //exchange.getResponseSender().close();
                    exchange.getResponseChannel().writeFinal(EMPTY_BUF);
                    return null;
                }
            });

            exchange.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}