package myapp.routes;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import kikaha.urouting.api.*;
import javax.inject.*;
import com.fizzed.rocker.runtime.RockerRuntime;
import myapp.models.HazelCast;


import java.io.File;
import java.util.*;

@Singleton
@Path( "/hazelcast" )
public class HazelCastResource
{

    @GET
    @Path( "/" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderHome() {
        RockerRuntime.getInstance().setReloading(true);

        HazelCast hc = new HazelCast();
        Config cfg = null;
        try {
            cfg = new XmlConfigBuilder("conf/hazelcast.xml").build();

            HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(cfg.getInstanceName());
            Map<Integer, String> mapCustomers = instance.getMap("customers");

            hc.size = mapCustomers.size();
        } catch (Exception e) {
            //System.out.println(e.toString());
            hc.size = -1;
        }

        return new rocker.RockerTemplate().templateName( "views/hazelcast.rocker.html" ).paramContent(hc);
    }

    @GET
    @Path( "/generate" )
    @Produces( Mimes.HTML )
    public rocker.RockerTemplate renderGenerateDummyDataToHazelCast() {

        HazelCast hc = new HazelCast();
        Config cfg = null;
        try {
            cfg = new XmlConfigBuilder("conf/hazelcast.xml").build();

            HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(cfg.getInstanceName());
            Map<Integer, String> mapCustomers = instance.getMap("customers");

            String uuid = UUID.randomUUID().toString();

            mapCustomers.put(mapCustomers.size()+1, UUID.randomUUID().toString());
            hc.size = mapCustomers.size();
        } catch (Exception e) {
            //System.out.println(e.toString());
            hc.size = -1;
        }

        return new rocker.RockerTemplate().templateName( "views/hazelcast.rocker.html" ).paramContent(hc);
    }

}