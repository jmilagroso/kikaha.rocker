package kikaha.app.zookeeper;

/*
 * @(#)ZKConnection.java
 * @author Binu George
 * Globinch.com
 * copyright http://www.java.globinch.com. All rights reserved.
 */
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZKConnection Class. Class that initialize connection to Ensemble
 *
 * @author Binu George
 * @since 2016
 * @version 1.0
 * http://www.java.globinch.com. All rights reserved
 */
public class ZKConnection {

    // Local Zookeeper object to access ZooKeeper ensemble
    private ZooKeeper zoo;
    final CountDownLatch connectionLatch = new CountDownLatch(1);

    /**
     *
     */
    public ZKConnection() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Initialize the Zookeeper connection
     * @param host
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ZooKeeper connect(String host) throws IOException,
            InterruptedException {

        zoo = new ZooKeeper(host, 2000, new Watcher() {

            public void process(WatchedEvent we) {

                if (we.getState() == KeeperState.SyncConnected) {
                    connectionLatch.countDown();
                }
            }
        });

        connectionLatch.await();
        return zoo;
    }

    // Method to disconnect from zookeeper server
    public void close() throws InterruptedException {
        zoo.close();
    }

}