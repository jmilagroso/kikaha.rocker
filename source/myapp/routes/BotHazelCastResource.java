package myapp.routes;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import kikaha.urouting.api.*;
import javax.inject.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import myapp.models.Author;
import myapp.models.Forum;
import myapp.models.Post;
import myapp.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentMap;

@Singleton
@Path( "/bot-hazelcast" )
public class BotHazelCastResource {

    private rocker.RockerTemplate handler(short page) {
        Config cfg = null;
        Forum forum = new Forum();
        try {
            cfg = new XmlConfigBuilder("conf/hazelcast.xml").build();

            HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(cfg.getInstanceName());

            ConcurrentMap<String, List<Post>> postsMap = instance.getMap("posts.map");
            ConcurrentMap<String, List<Author>> authorsMap = instance.getMap("posts.author");

            if(postsMap.size() == 0 && authorsMap.size()==0){
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

                postsMap.put("posts", posts);
                authorsMap.put("authors", authors);
            }

            List<Post> posts = postsMap.get("posts");

            // Pagination
            short perPage = 5;
            short totalData = (short)posts.size();
            short pageCount = (short)(totalData/perPage);
            short currentPage = (page > 1) ? page : 1;

            forum.posts = posts.subList(currentPage-1, totalData/perPage);
            forum.title = "Forums";
            forum.subtitle = "Subtitle";
            forum.page = currentPage;
            forum.pageCount = pageCount;
            forum.url = "bot-hazelcast";

            // A dummy data to validate multiple parameter content through Rocker.
            User u = new User();
            u.name = "Peter";
            u.age = 31;

            return new rocker.RockerTemplate().templateName("views/bothazelcast.rocker.html").setParamContent(forum, u);
        } catch (Exception e) {
            return new rocker.RockerTemplate().templateName( "views/common/error.rocker.html" ).paramContent(e.getMessage().toString());
        }
    }

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBot() {
        short page = 1;
        return handler(page);
    }

    @GET
    @Path( "/{page}" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBotWithPage( @PathParam("page") short page) {
        return handler(page);
    }
}