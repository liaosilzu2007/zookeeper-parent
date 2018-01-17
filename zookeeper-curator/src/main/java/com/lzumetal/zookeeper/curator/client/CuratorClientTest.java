package com.lzumetal.zookeeper.curator.client;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 *
 * @author: liaosi
 * @date: 2018-01-17
 */
public class CuratorClientTest {

    private static final Logger log = LoggerFactory.getLogger(CuratorClientTest.class);

    private static final String IPS = "localhost:2181";
    private static final int MAXELAPSEDTIMEMS = 4000;
    private static final int SLEEPMSBETWEENRETRIES = 2000;
    private static final String ZK_PATH = "/zktest";

    public static void main(String[] args) throws Exception {

        /**
         *  方法打印结果：
         *
         *  $ create /zktest hello
         *  $ ls /
         *  [zktest, zookeeper]
         *  $ get /zktest
         *  hello
         *  $ set /zktest world
         *  $ get /zktest
         *  world
         *  $ delete /zktest
         *  $ ls /
         *  [zookeeper]
         */

        // 1.Connect to zk
        CuratorFramework client = ClientSingleton.getClient(IPS, MAXELAPSEDTIMEMS, SLEEPMSBETWEENRETRIES);

        // 2.Client API test
        // 2.1 Create node
        String data1 = "hello";
        print("create", ZK_PATH, data1);
        client.create()
                .creatingParentsIfNeeded()
                .forPath(ZK_PATH, data1.getBytes());

        // 2.2 Get node and data
        print("ls", "/");
        print(client.getChildren().forPath("/"));
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // 2.3 Modify data
        String data2 = "world";
        print("set", ZK_PATH, data2);
        client.setData().forPath(ZK_PATH, data2.getBytes());
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // 2.4 Remove node
        print("delete", ZK_PATH);
        client.delete().forPath(ZK_PATH);
        print("ls", "/");
        print(client.getChildren().forPath("/"));

        client.close();
    }

    private static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        log.warn(text.toString());
    }

    private static void print(Object result) {
        log.warn(result instanceof byte[] ? new String((byte[]) result) : result.toString());
    }
}
