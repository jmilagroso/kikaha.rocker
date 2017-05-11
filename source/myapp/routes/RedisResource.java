package myapp.routes;

import com.fizzed.rocker.runtime.RockerRuntime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kikaha.urouting.api.*;
import javax.inject.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import myapp.models.*;
import myapp.models.Collection;
import myapp.services.Builder;
import redis.clients.jedis.Jedis;
import java.util.*;

@Singleton
@Path( "/redis" )
public class RedisResource {

    Builder builder = new Builder();
    String controller = "redis";
    String title = "Redis title";
    String subtitle = "Redis subtitle";

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
                // Get posts JSON and translate
                GenericType<List<Post>> genericTypePost = new GenericType<List<Post>>(){};
                List<Post> posts = ClientBuilder.newClient()
                        .target("http://maqe.github.io/json/posts.json")
                        .request().accept(MediaType.APPLICATION_JSON)
                        .get(genericTypePost);

                GenericType<List<Author>> genericTypeAuthor = new GenericType<List<Author>>(){};
                List<Author> authors = ClientBuilder.newClient()
                        .target("http://maqe.github.io/json/authors.json")
                        .request().accept(MediaType.APPLICATION_JSON)
                        .get(genericTypeAuthor);

                Map<Integer, Author> map = new HashMap<Integer, Author>();
                for(Author a: authors) {
                    map.put(a.id, a);
                }

                // Post-Author association.
                for(Post p: posts) {
                    if(map.containsKey(p.author_id)) {
                        p.author = map.get(p.author_id);
                    }
                }

                jedis.set(redisKey, gson.toJson(posts));
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
    public rocker.RockerTemplate renderBot()  {
        return new rocker.RockerTemplate()
                .templateName("views/redis.rocker.html")
                .setParamContent(new Object[] {
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
    public rocker.RockerTemplate renderBotWithPage( @PathParam("page") Integer page) {
        return new rocker.RockerTemplate()
                .templateName("views/redis.rocker.html")
                .setParamContent(new Object[] {
                        builder.builder(this.process(), 5, page),
                        title,
                        subtitle,
                        controller,
                        builder.paginator.currentPage,
                        builder.paginator.pageCount

                });
    }

}