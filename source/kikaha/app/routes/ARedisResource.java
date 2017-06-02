package kikaha.app.routes;

import com.fizzed.rocker.Rocker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.app.models.Post;
import kikaha.app.services.Builder;
import kikaha.app.services.Downloader;
import kikaha.core.modules.http.WebResource;
import kikaha.urouting.SimpleExchange;
import kikaha.urouting.UndertowHelper;
import org.aredis.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
@WebResource( path="/aredis", method="GET" )
public class ARedisResource implements HttpHandler {

    private static Logger logger = LoggerFactory.getLogger(ARedisResource.class);

    // Please configure the factory with an existing or new executor for Async API or Subscription
    // It is better to always pass an exeuctor to the factory.
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static AsyncRedisFactory f = new AsyncRedisFactory(executor);

    static {
        executor.allowCoreThreadTimeOut(true);
    }

    private static Builder builder = new Builder();
    private static String controller = "aredis";
    private static String title = "ARedis title";
    private static String subtitle = "ARedis subtitle";

    @Inject
    UndertowHelper requestHelper;

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {

        SimpleExchange simplified = requestHelper.simplify( exchange );
        Integer page = (simplified.getQueryParameters().size() > 0) ? simplified.getQueryParameter( "page", Long.class ).intValue() : 1;

        try {
            // Get a client to DB 1 instead of the default of 0
            AsyncRedisClient aredis = f.getClient("localhost/0");

            aredis.submitCommand(new RedisCommandInfo(RedisCommand.GET, "posts"), new AsyncHandler<RedisCommandInfo>() {
                @Override
                public void completed(RedisCommandInfo result, Throwable e) {
                    if(e == null) {

                        List<Post> tmp = new ArrayList<Post>();

                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");

                        if(result == null || result.getResult() == null) {
                            // Download data
                            tmp = Downloader.init();
                            // Put to Redis
                            aredis.sendCommand(RedisCommand.SET, "posts", new Gson().toJson(tmp));
                        } else {
                            // Get
                            tmp = new Gson().fromJson(result.getResult().toString(), new TypeToken<ArrayList<Post>>(){}.getType());
                        }

                        // HTML / Rocker
                        exchange.getResponseSender().send(Rocker.template("kikaha/app/views/redis.rocker.html", builder.builder(tmp, 5, page),
                                title,
                                subtitle,
                                controller,
                                builder.paginator.currentPage,
                                builder.paginator.pageCount).render().toString());

                        // JSON
                        // exchange.getResponseSender().send(new Gson().toJson(tmp));
                    }
                }
            });

            exchange.dispatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}