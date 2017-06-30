package playground;

import com.relops.snowflake.Snowflake;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by jay on 6/29/17.
 */
public class ZKDemo {
    // Logger
    private static Logger logger = LoggerFactory.getLogger(ZKDemo.class);

    // MySQL Cluster Config Separator
    private static final char MySQLClusterConfigSeparator = '!';

    private static final String     PATH = "/example/cache";

    private static TestingServer server = null;
    private static CuratorFramework    client = null;
    private static PathChildrenCache cache = null;
    private static Stat stat = null;

    public static void main(String args[]) {

        try {

            client = CuratorFrameworkFactory.newClient("localhost:2181",
                    new ExponentialBackoffRetry(
                            1000,
                            3));
            client.start();

            // Let's make sure we're connected to zookeeper first.
            client.blockUntilConnected();

            cache = new PathChildrenCache(client, PATH, true);
            cache.start();

            // Get metadata info
            stat = client.getZookeeperClient().getZooKeeper().exists(PATH, true);

            // Arbitrary parameter, in case we need to update the cluster config values.
            // TODO Handle the update invocation.
            boolean update = false;

            // Data from zk node as cached.
            String result = null;

            if (stat != null) {
                if(update) {
                    int version = client.getZookeeperClient().getZooKeeper().exists(PATH, true).getVersion();
                    client.getZookeeperClient().getZooKeeper().setData(PATH, dummyData(), version);
                }
                result = new String(get(), "UTF-8");
            } else {
                // No node exists, create first then proceed with saving..
                createParents(dummyData());
                result = new String(get(), "UTF-8");
            }

            // Let's shard
            shard(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void shard(String data) {
        String[] clusters = data.split(String.valueOf(MySQLClusterConfigSeparator));

        // Dummy record to be inserted on the "database"
        int recordCount = 4096;
        Snowflake s = new Snowflake(1);

        for(int i = 0; i < recordCount; i++) {
            // The node id is a manually assigned value between 0 and 1023 which is used to
            // differentiate different snowflakes when used in a multi-node cluster.
            long id = s.next();

            int shard = (int)(id % clusters.length);

            // Each 'Shard' (1 Machine) has N sharded dbs.
            // Number of database(s) per machine is relative to
            // number of of machines available.
            int db = (int)(id % clusters.length);

            System.out.println("UUID:"+id + " | DB:mysql-host-"+clusters[shard] + " | db:" + db);
        }
    }

    private static byte[] dummyData() {
        // Byte array of the mysql cluster config data.
        byte[] bytesArray = null;
        // Prepare data to be saved/updated
        String[] mysqlClusters = new String[]{
                "host:192.168.0.1,user:app1,password:app1,port:3306",
                "host:192.168.0.2,user:app2,password:app2,port:3306",
                "host:192.168.0.3,user:app3,password:app3,port:3306",
                "host:192.168.0.4,user:app4,password:app4,port:3306",
                "host:192.168.0.5,user:app5,password:app5,port:3306",
        };
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

        return bytesArray;
    }

    private static byte[] get() {
        try {
            return client.getZookeeperClient().getZooKeeper().getData(PATH, null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void createParents(byte[] bytesArray) {
        try {
            client.create().creatingParentsIfNeeded().forPath(PATH, bytesArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
