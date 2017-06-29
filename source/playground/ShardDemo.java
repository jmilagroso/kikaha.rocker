package playground;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.relops.snowflake.Snowflake;

/**
 * Created by jay on 6/28/17.
 */
public class ShardDemo {
    // https://medium.com/@Pinterest_Engineering/sharding-pinterest-how-we-scaled-our-mysql-fleet-3f341e96ca6f
    public static void main(String args[]) {

        List<String> mySQLMachines = Arrays.asList(new String[] {
                "192.168.0.21",
                "192.168.0.22",
                "192.168.0.23",
                "192.168.0.24",
                "192.168.0.25",
                "192.168.0.26",
                "192.168.0.27",
                "192.168.0.28",

        });

        Integer clusterACount = 0;
        Integer clusterBCount = 0;
        Integer clusterCCount = 0;
        Integer clusterDCount = 0;
        Integer clusterECount = 0;
        Integer clusterFCount = 0;
        Integer clusterGCount = 0;
        Integer clusterHCount = 0;

        List<Long> randomIds = new LinkedList<>();

        int recordCount = 100000;
        Snowflake s = new Snowflake(1);

        for(int i = 0; i < recordCount; i++) {
            // The node id is a manually assigned value between 0 and 1023 which is used to
            // differentiate different snowflakes when used in a multi-node cluster.
            long id = s.next();
            randomIds.add(id);
            System.out.println(id);
        }

        for(Long id: randomIds) {
            int shard = (int)(id % mySQLMachines.size());

            switch (shard) {
                case 0: clusterACount++; break;
                case 1: clusterBCount++; break;
                case 2: clusterCCount++; break;
                case 3: clusterDCount++; break;
                case 4: clusterECount++; break;
                case 5: clusterFCount++; break;
                case 6: clusterGCount++; break;
                case 7: clusterHCount++; break;
                default: throw new RuntimeException("Should not happen");
            }

            int db = (int)(id % 255);

            System.out.println("UUID:"+id + " | DB:mysql-host-"+mySQLMachines.get(shard) + " | db:" + db);
        }

        System.out.println("Number of records that will be routed to each 'DB Cluster' with " + recordCount + " records.");
        System.out.println("ClusterA:" + clusterACount);
        System.out.println("ClusterB:" + clusterBCount);
        System.out.println("ClusterC:" + clusterCCount);
        System.out.println("ClusterD:" + clusterDCount);
        System.out.println("ClusterE:" + clusterECount);
        System.out.println("ClusterF:" + clusterFCount);
        System.out.println("ClusterG:" + clusterGCount);
        System.out.println("ClusterH:" + clusterHCount);
    }

}
