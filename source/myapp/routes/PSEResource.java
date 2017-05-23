package myapp.routes;

import com.fizzed.rocker.Rocker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import kikaha.core.modules.http.WebResource;
import lombok.val;
import myapp.models.Author;
import myapp.models.PSE;
import myapp.models.Post;
import myapp.services.Downloader;

import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fizzed.rocker.Rocker.template;


/**
 * Created by jay on 5/23/17.
 */
@Singleton
@WebResource( path="/pse", method="GET" )
public class PSEResource implements HttpHandler {

    private static URL url;
    private static HazelcastInstance instance;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static void initializeHazelCast() {
        System.out.println("CALLED ONCE");
        instance = Hazelcast.getHazelcastInstanceByName(getHazelCastConfig().getInstanceName());
        populatePSE(instance.getMap("PSE"), instance.getMap("PSE.FN"));
    }

    private static Config getHazelCastConfig() {
        try {
            return new XmlConfigBuilder("conf/hazelcast.xml").build();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to initialize hazelcast: " + e.getMessage(), e);
        }
    }

    private static void populatePSE(final ConcurrentMap<String, List<PSE>> pseMap, final ConcurrentMap<String, PSE> pseFNMap) {
        try {
            System.out.println("POPULATING PSE!!!");

            url = new URL("http://www.pse.com.ph/stockMarket/home.html?method=getSecuritiesAndIndicesForPublic&ajax=true");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Referer", "http://www.pse.com.ph/stockMarket/home.html");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            InputStream stream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);

            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String strFileContents = "";
            while((bytesRead = bufferedInputStream.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }
            List<PSE> pse = new Gson().fromJson(strFileContents, new TypeToken<ArrayList<PSE>>(){}.getType());
            PSE firstNode = pse.get(0); // Let's use first node
            pse.remove(0); // First node contains current date, remove this.

                /*
                Maximum number of seconds for each entry to stay idle in the map. Entries that are

                idle(not touched) for more than <max-idle-seconds> will get
                automatically evicted from the map. Entry is touched if get, put or containsKey is called.
                Any integer between 0 and Integer.MAX_VALUE. 0 means infinite. Default is 0.

                <max-idle-seconds>60</max-idle-seconds>
                */
            pseMap.put("PSE", pse);
            pseFNMap.put("PSE.FN", firstNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        try {
            if (!isInitialized.getAndSet(true)) {
                initializeHazelCast();
            }

            ConcurrentMap<String, List<PSE>> pseMap = instance.getMap("PSE");
            ConcurrentMap<String, PSE> pseFNMap     = instance.getMap("PSE.FN");

            if (pseMap.isEmpty()) {
                populatePSE(pseMap, pseFNMap);
            }

            String response = Rocker.template(
                    "views/pse.rocker.html",
                    pseMap.get("PSE"),
                    pseFNMap.get("PSE.FN")).render().toString();

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
            exchange.getResponseSender().send(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
