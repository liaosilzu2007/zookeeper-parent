package com.lzumetal.zookeeper.helloworld;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liaosi on 2017/11/18.
 */
public class DistributedServer {

    private static final Logger log = LoggerFactory.getLogger(DistributedServer.class);

    private static String connectString = "server01:2181,server02:2181,server03:2181";
    private static int timeoutMillis = 2000;
    private static String serverGroupNode = "/servers";
    private ZooKeeper zooKeeper;

    public void getConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, timeoutMillis, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "--->" + event.getPath());
            }
        });
    }

    public void registServer(String hostname) throws Exception {
        Stat stat = zooKeeper.exists(serverGroupNode, false);
        if (stat == null) {
            zooKeeper.create(serverGroupNode, "servers".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String nodeName = zooKeeper.create(serverGroupNode + "/" + hostname, hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(String.format("服务器：%s 注册到了ZooKeeper，节点路径是：%s", hostname, nodeName));

    }

    public void handlBussiness(String hostname) throws InterruptedException {
        System.out.println(String.format("服务器：%s 节点启动了，开始处理业务", hostname));
        Thread.sleep(Long.MAX_VALUE);
    }



    public static void main(String[] args) throws Exception {
        //1.创建到ZooKeeper的连接
        DistributedServer server = new DistributedServer();
        server.getConnect();

        //2.利用连接注册服务器信息
        server.registServer(args[0]);

        //3.处理业务逻辑
        server.handlBussiness(args[0]);
    }


}
