package com.lzumetal.zookeeper.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: </p>
 *
 * @author: liaosi
 * @date: 2018-01-17
 */
public class InterProcessMutexExample {


    private static final int QTY = 5;
    private static final int REPETITIONS = 10;
    private static final String PATH = "/examples/locks";
    private static final String ZK_ADDRESS = "localhost:2181";


    public static void main(String[] args) throws Exception {
        final FakeLimitedResource resource = new FakeLimitedResource();
        ExecutorService threadPool = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            final int index = i;
            Runnable task = () -> {
                CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new ExponentialBackoffRetry(1000, 3));
                try {
                    client.start();
                    final ClientHandler clientHandler = new ClientHandler(client, PATH, resource, "Client " + index);
                    for (int j = 0; j < REPETITIONS; ++j) {
                        //clientHandler.doWork(10, TimeUnit.SECONDS);
                        clientHandler.doWorkWithoutLock();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    CloseableUtils.closeQuietly(client);
                }
            };
            threadPool.submit(task);
        }
        threadPool.shutdown();
        threadPool.awaitTermination(3, TimeUnit.MINUTES);
    }
}