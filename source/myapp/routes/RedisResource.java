package myapp.routes;

import com.fizzed.rocker.runtime.RockerRuntime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kikaha.urouting.api.*;
import javax.inject.*;

import myapp.models.*;
import myapp.models.Collection;
import myapp.services.Builder;
import myapp.services.Downloader;
import redis.clients.jedis.Jedis;
import java.util.*;

@Singleton
@Path( "/redis" )
public class RedisResource {

    Builder builder = new Builder();
    String controller = "redis";
    String title = "Redis title";
    String subtitle = "Redis subtitle";

    List<Post> posts;

    private List<Post> process() {
        RockerRuntime.getInstance().setReloading(true);
        Collection collection = new myapp.models.Collection();
        Gson gson = new Gson();

        try {
            // Single instance redis
            Jedis jedis = new Jedis("localhost", 6379);

            String redisKey = "posts";
            //jedis.del(redisKey.getBytes());
            if(jedis.get(redisKey.getBytes())==null) {
                posts = Downloader.init();
                if(!posts.isEmpty()) {
                    jedis.set(redisKey, gson.toJson(posts));
                }
            }

            List<Post> posts = new Gson().fromJson(jedis.get(redisKey), new TypeToken<ArrayList<Post>>(){}.getType());

            return posts;
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