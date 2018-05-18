package com.lzumetal.zookeeper.curator.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class CreateClentTest {

    @Test
    public void createByNew() {
        String connectString = "127.0.0.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
        System.out.println("============>use ZooKeeper, complete your operation");
        client.close();
    }

    @Test
    public void createByBuilder() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(1000 * 6)
                .connectionTimeoutMs(1000 * 6)
                .build();
        client.start();
        System.out.println("============>use ZooKeeper, complete your operation");
        client.close();
    }
}
