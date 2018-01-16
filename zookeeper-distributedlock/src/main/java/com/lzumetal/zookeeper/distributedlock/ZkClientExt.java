package com.lzumetal.zookeeper.distributedlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * author: liaosi
 * date: 2018-01-16
 */
public class ZkClientExt {

    private static final Logger log = LoggerFactory.getLogger(ZkClientExt.class);

    private static String connectString = "server01:2181,server02:2181,server03:2181";
    private static int timeoutMillis = 2000;


    public void delete(String ourPath) {
    }

    public String createEphemeralSequential(String path, Object o) {
        return null;
    }

    public List<String> getChildren(String basePath) {

        return null;
    }

    public void unsubscribeDataChanges(String previousSequencePath, IZkDataListener previousListener) {
    }

    public void subscribeDataChanges(String previousSequencePath, IZkDataListener previousListener) {
    }

    public void createPersistent(String basePath, boolean b) {
    }
}
