package myapp.routes;

import com.fizzed.rocker.runtime.RockerRuntime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kikaha.urouting.api.*;
import javax.inject.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import myapp.models.Author;
import myapp.models.Forum;
import myapp.models.Post;
import myapp.models.User;
import redis.clients.jedis.Jedis;
import java.util.*;

@Singleton
@Path( "/bot-redis" )
public class BotRedisResource {

    private rocker.RockerTemplate handler(short page) {
        RockerRuntime.getInstance().setReloading(true);
        Forum forum = new Forum();
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

            short perPage = 5;
            short totalData = (short)posts.size();
            short pageCount = (short)(totalData/perPage);
            short currentPage = (page > 1) ? page : 1;

            forum.posts = posts.subList(currentPage-1, totalData/perPage);
            forum.title = "Forums";
            forum.subtitle = "Subtitle";
            forum.page = currentPage;
            forum.pageCount = pageCount;
            forum.url = "bot-redis";

            // A dummy data to validate multiple parameter content through Rocker.
            User u = new User();
            u.name = "Peter";
            u.age = 31;

            return new rocker.RockerTemplate().templateName("views/botredis.rocker.html").setParamContent(forum, u);
        } catch (Exception e) {
            return new rocker.RockerTemplate().templateName( "views/common/error.rocker.html" ).paramContent(e.getMessage().toString());
        }
    }

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBot()  {
        short page = 1;
        return handler(page);
    }

    @GET
    @Path( "/{page}" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBotWithPage( @PathParam("page") short page)  {
       return handler(page);
    }
}