package com.lzumetal.zookeeper.curator.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 *
 * @author: liaosi
 * @date: 2018-01-17
 */
public class ClientSingleton {

    private volatile static CuratorFramework client;
    private static final Logger log = LoggerFactory.getLogger(ClientSingleton.class);

    private ClientSingleton(String ips, int maxElapsedTimeMs, int sleepMsBetweenRetries) {
        log.info("开始连接到Zookeeper服务器：{}", ips);
        /*
         * maxElapsedTimeMs：最长重试时间
         * sleepMsBetweenRetries：重试的间隔时间
         */
        RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(maxElapsedTimeMs, sleepMsBetweenRetries);
        client = CuratorFrameworkFactory.builder()
                .connectString(ips)
                .connectionTimeoutMs(4000)
                .sessionTimeoutMs(8000)
                .retryPolicy(retryUntilElapsed)
                .build();
        client.start();
    }

    public static CuratorFramework getClient(String ips, int maxElapsedTimeMs, int sleepMsBetweenRetries) {
        if (client == null) {
            synchronized (ClientSingleton.class) {
                if (client == null) {
                    new ClientSingleton(ips, maxElapsedTimeMs, sleepMsBetweenRetries);
                }
            }
        }

        return client;
    }

}
