package myapp.routes;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import kikaha.urouting.api.*;
import javax.inject.*;

import myapp.models.Author;
import myapp.models.Collection;
import myapp.models.Post;
import myapp.services.Builder;
import myapp.services.Downloader;

import java.util.ArrayList;
import java.util.List;

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
        try {
            cfg = new XmlConfigBuilder("conf/hazelcast.xml").build();
            HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(cfg.getInstanceName());

            ConcurrentMap<String, List<Post>> postsMap = instance.getMap("posts.map");
            ConcurrentMap<String, List<Author>> authorsMap = instance.getMap("posts.author");

            if(postsMap.isEmpty()){
                List<Post> posts = Downloader.init();
                postsMap.put("posts", posts);
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
    public rocker.RockerTemplate render() {
        return new rocker.RockerTemplate()
                .setTemplateName("views/hazelcast.rocker.html")
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
                .setTemplateName("views/hazelcast.rocker.html")
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