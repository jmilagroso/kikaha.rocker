package kikaha.app.routes;

import com.relops.snowflake.Snowflake;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import kikaha.app.zookeeper.ZKClientManagerImpl;
import kikaha.core.modules.http.WebResource;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import playground.ZKDemo;

import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jay on 6/29/17.
 */
@Singleton
@WebResource( path="/zookeeper", method="GET" )
public class ZookeeperResource implements HttpHandler {
    // Logger
    private static Logger logger = LoggerFactory.getLogger(ZookeeperResource.class);

    // MySQL Cluster Config Separator
    private static final char MySQLClusterConfigSeparator = '!';
    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        // Please consider run blocking operations on Undertow's Worker Threads, instead of Undertow's IO Thread.
        // Even under mid-level load on your server, you could face some IO issues if you don't do it.
        // Using Undertow's API, one should always rely on the following pattern:
        if ( !exchange.isInIoThread() ){
            exchange.dispatch( this );
            return;
        }

        // Zookeeper client manager impl.
        ZKClientManagerImpl zkClientManager = new ZKClientManagerImpl();

        // Zookeeper node path.
        String path = "/MySQLClusters";

        // "MySQL Cluster" config string array.
        // TODO Fetch this from a .yml/.conf/.json over a type-safe config library.
        String[] mysqlClusters = new String[]{
                "host:192.168.0.1,user:app1,password:app1,port:3306",
                "host:192.168.0.2,user:app2,password:app2,port:3306",
                "host:192.168.0.3,user:app3,password:app3,port:3306",
                "host:192.168.0.4,user:app4,password:app4,port:3306",
                "host:192.168.0.5,user:app5,password:app5,port:3306",
        };

        // Byte array of the mysql cluster config data.
        byte[] bytesArray = null;

        try {
            // Byte array output stream container.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            for(String cluster : mysqlClusters) {
                try {
                    // Cluster separator.
                    cluster += MySQLClusterConfigSeparator;
                    // Write the each string cluster to byte
                    // array output stream (byte[])
                    output.write(cluster.getBytes());
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
            // Output stream get byte array.
            bytesArray = output.toByteArray();
            // Remove the last pipe from the byte array.
            bytesArray = Arrays.copyOfRange(bytesArray, 0, bytesArray.length - 1);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Arbitrary parameter, in case we need to update the cluster config values.
        // TODO Handle the update invocation.
        boolean update = false;

        try {
            Stat stat = zkClientManager.getZNodeStats(path);
            if (stat != null) {
                // What if we need to update the data?
                if(update) {
                    zkClientManager.update(path, bytesArray);
                }
                // Access node path data.
                String nodeData = (String)zkClientManager.getZNodeData(path,false);
                logger.info("MySQL Cluster Config(s) from ZK = "+nodeData);

                mimicDBSharding(nodeData);
            } else {
                // Node path doesn't exist, let's create it.
                zkClientManager.create(path, bytesArray);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            zkClientManager.closeConnection();
        }
    }

    private final static void mimicDBSharding(String mysqlDBCluster) {
        logger.info("mysqlDBCluster = "+mysqlDBCluster);
        logger.info(String.valueOf(mysqlDBCluster.split(String.valueOf(MySQLClusterConfigSeparator)).length));

        String[] clusters = mysqlDBCluster.split(String.valueOf(MySQLClusterConfigSeparator));

        List<Long> randomIds = new LinkedList<>();

        // Dummy record to be inserted on the "database"
        int recordCount = 4096;
        Snowflake s = new Snowflake(1);

        for(int i = 0; i < recordCount; i++) {
            // The node id is a manually assigned value between 0 and 1023 which is used to
            // differentiate different snowflakes when used in a multi-node cluster.
            long id = s.next();
            randomIds.add(id);
        }

        for(Long id: randomIds) {
            int shard = (int)(id % clusters.length);

            // Each 'Shard' (1 Machine) has N sharded dbs.
            // Number of database(s) per machine is relative to
            // number of of machines available.
            int db = (int)(id % clusters.length);

            System.out.println("UUID:"+id + " | DB:mysql-host-"+clusters[shard] + " | db:" + db);
        }
    }
}
