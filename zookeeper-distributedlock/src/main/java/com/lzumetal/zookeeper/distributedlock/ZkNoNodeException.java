package com.lzumetal.zookeeper.distributedlock;

/**
 * author: liaosi
 * date: 2018-01-16
 */
public class ZkNoNodeException extends RuntimeException {

    public ZkNoNodeException(String message) {
        super(message);
    }
}
