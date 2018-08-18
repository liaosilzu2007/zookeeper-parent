package com.lzumetal.zookeeper.curator.watcher;

import com.lzumetal.zookeeper.curator.client.ClientSingleton;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: </p>
 *
 * @author: liaosi
 * @date: 2018-01-17
 */
public class CuratorWatcherTest {

    /**
     * Zookeeper info
     */
    private static final String ZK_ADDRESS = "localhost:2181";
    private static final String ZK_PATH = "/zktest";
    private static final int MAX_ELAPSED_TIME_MS = 4000;
    private static final int SLEEP_MS_BETWEEN_RETRIES = 2000;

    public static void main(String[] args) throws Exception {

        // 1.Connect to zk
        CuratorFramework client = ClientSingleton.getClient(ZK_ADDRESS, MAX_ELAPSED_TIME_MS, SLEEP_MS_BETWEEN_RETRIES);
        System.out.println("zk client start successfully!");

        // 2.Register watcher
        PathChildrenCache watcher = new PathChildrenCache(client, ZK_PATH, true);   // true：if cache data

        watcher.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        TimeUnit.MINUTES.sleep(5);
        client.close();
    }

}
