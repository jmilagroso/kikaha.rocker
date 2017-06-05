package kikaha.app.routes;

import com.fizzed.rocker.Rocker;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.app.services.S3;
import kikaha.core.modules.http.WebResource;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;

/**
 * Created by jay on 6/2/17.
 */
@Singleton
@WebResource( path="/s3", method="GET" )
public class S3Resource implements HttpHandler {

    // http://sakthipriyan.com/2016/02/17/scala-code-to-access-documents-in-aws-s3-bucket.html

    Logger logger = LoggerFactory.getLogger(UndertowResource.class);

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        try {
            AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

            S3.AwsS3Request awsS3Request = S3.get("README.md");

            String fileName = "README.md";

            BoundRequestBuilder boundRequestBuilder = asyncHttpClient.prepareGet(S3.getS3Uri() + "/"+S3.getS3bucket()+"/"+fileName);
            for(String key: awsS3Request.getHeaders().keySet()) {
                //logger.info(key+"=>"+awsS3Request.getHeaders().get(key));
                boundRequestBuilder.addHeader(key, awsS3Request.getHeaders().get(key));
            }

            boundRequestBuilder.execute(new AsyncCompletionHandler<Response>(){

                @Override
                public void onThrowable(Throwable t){
                    // Something wrong happened.
                    logger.error(t.getMessage());
                }

                @Override
                public Response onCompleted(org.asynchttpclient.Response response) throws Exception {
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