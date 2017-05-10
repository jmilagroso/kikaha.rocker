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
import myapp.services.Builder;
import myapp.services.Paginator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentMap;

@Singleton
@Path( "/hazelcast" )
public class HazelCastResource {

    Builder builder = new Builder();
    String controller = "hazelcast";
    String title = "HazelCast title";
    String subtitle = "HazelCast subtitle";

    private List<Post> process() {
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

            return postsMap.get("posts");
        } catch (Exception e) {
            // Handle exception, just return empty List<Post>
            return new ArrayList<Post>();
        }
    }

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBot() {
        return new rocker.RockerTemplate()
                .templateName("views/hazelcast.rocker.html")
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
                .templateName("views/hazelcast.rocker.html")
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