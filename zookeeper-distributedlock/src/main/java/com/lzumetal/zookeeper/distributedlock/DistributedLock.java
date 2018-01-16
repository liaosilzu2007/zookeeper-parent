package com.lzumetal.zookeeper.distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * Created by liaosi on 2018/1/16.
 */
public interface DistributedLock {

    /**
     * 获取锁，如果没有获取到就等待
     */
    public void acquireLock() throws Exception;

    /**
     * 获取锁，直至超时
     *
     * @param time     超时时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean acquireLock(long time, TimeUnit timeUnit) throws Exception;

    /**
     * 释放锁
     */
    public void releaseLock() throws Exception;


}
