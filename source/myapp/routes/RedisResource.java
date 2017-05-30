package myapp.routes;

import com.fizzed.rocker.runtime.RockerRuntime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisStringAsyncCommands;
import kikaha.urouting.api.*;
import javax.inject.*;

import myapp.models.*;
import myapp.services.Builder;
import myapp.services.Downloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Singleton
@Path( "/redis" )
public class RedisResource {

    private static Logger logger = LoggerFactory.getLogger(RedisResource.class);

    // Redis [Host + Port] Override
    private static RedisURI redisURI = new RedisURI();

    private static Builder builder = new Builder();
    private static String controller = "redis";
    private static String title = "Redis title";
    private static String subtitle = "Redis subtitle";

    private List<Post> process() {
        try {
            // Redis Client
            redisURI.setHost("localhost"); // @override
            redisURI.setPort(6379);        // @override
            RedisClient client = RedisClient.create(redisURI);

            StatefulRedisConnection<String, String> connection = client.connect();
            RedisStringAsyncCommands<String, String> async = connection.async();
            RedisFuture<String> get = async.get("posts");

            if(get.get()==null) {
                RedisFuture<String> set = async.set("posts", new Gson().toJson(Downloader.init()));
            }

            return new Gson().fromJson(get.get(), new TypeToken<ArrayList<Post>>(){}.getType());
        } catch (Exception e) {
            // Handle exception, just return empty List<Post>
            return new ArrayList<Post>();
        }
    }

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate render()  {
        return new rocker.RockerTemplate()
                .setTemplateName("views/redis.rocker.html")
                .setObjects(new Object[] {
                        builder.builder(this.process(), 5, 1),
                        title,
                        subtitle,
                        controller,
                        builder.paginator.currentPage,
                        builder.paginator.pageCount

                });
    }

    @GET
    @Path( "/{page}" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderWithPage( @PathParam("page") Integer page) {
        return new rocker.RockerTemplate()
                .setTemplateName("views/redis.rocker.html")
                .setObjects(new Object[] {
                        builder.builder(this.process(), 5, page),
                        title,
                        subtitle,
                        controller,
                        builder.paginator.currentPage,
                        builder.paginator.pageCount

                });
    }

}