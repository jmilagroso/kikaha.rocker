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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentMap;

// https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
// http://get.kikaha.io/docs/urouting-api
// http://docs.oracle.com/javase/8/docs/api/java/util/List.html#subList-int-int-
// https://github.com/fizzed/rocker/blob/master/docs/SYNTAX.md
// http://stackoverflow.com/questions/39329263/hazelcast-hazelcastserializationexception-failed-to-serialize-com-hazelcast-s

@Singleton
@Path( "/bot-hazelcast" )
public class BotHazelCastResource {

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBot() {

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
            short page = 1;
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

            return new rocker.RockerTemplate().templateName( "views/bot.rocker.html" ).paramContent(forum);
        } catch (Exception e) {
            return new rocker.RockerTemplate().templateName( "views/common/error.rocker.html" ).paramContent(e.getMessage().toString());
        }
    }

    @GET
    @Path( "/{page}" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderBotWithPage( @PathParam("page") short page) {

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

            return new rocker.RockerTemplate().templateName( "views/bot.rocker.html" ).paramContent(forum);
        } catch (Exception e) {
            return new rocker.RockerTemplate().templateName( "views/common/error.rocker.html" ).paramContent(e.getMessage().toString());
        }
    }
}