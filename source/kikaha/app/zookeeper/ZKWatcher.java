package kikaha.app.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

/**
 * @author bpaikay
 *
 */
public class ZKWatcher implements Watcher, StatCallback {
    CountDownLatch latch;

    /**
     *
     */
    public ZKWatcher() {
        latch = new CountDownLatch(1);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Watcher fired on path: " + event.getPath() + " state: " +
                event.getState() + " type " + event.getType());
        latch.countDown();
    }

    public void await() throws InterruptedException {
        latch.await();
    }

}