package myapp.routes;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import kikaha.urouting.api.*;
import javax.inject.*;
import com.fizzed.rocker.runtime.RockerRuntime;
import myapp.models.HazelCast;
import java.util.Map;
import java.util.UUID;


@Singleton
@Path( "/hazelcast" )
public class HazelCastResource
{

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderHome() {
        RockerRuntime.getInstance().setReloading(true);

        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");

        HazelCast hc = new HazelCast();
        hc.size = mapCustomers.size();

        return new rocker.RockerTemplate().templateName( "views/hazelcast.rocker.html" ).paramContent(hc);
    }

    @GET
    @Path( "/generate" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderGenerateDummyDataToHazelCast() {

        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");

        String uuid = UUID.randomUUID().toString();

        mapCustomers.put(mapCustomers.size()+1, UUID.randomUUID().toString());

        HazelCast hc = new HazelCast();
        hc.size = mapCustomers.size();

        return new rocker.RockerTemplate().templateName( "views/hazelcast.rocker.html" ).paramContent(hc);
    }

}