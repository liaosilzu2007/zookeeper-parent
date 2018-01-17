package com.lzumetal.zookeeper.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: </p>
 *
 * @author: liaosi
 * @date: 2018-01-17
 */
public class ClientHandler {

    private final InterProcessMutex lock;
    private final FakeLimitedResource resource;
    private final String clientName;

    public ClientHandler(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try {
            System.out.println(clientName + " has the lock");
            resource.use(); //access resource exclusively
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
    }
}
