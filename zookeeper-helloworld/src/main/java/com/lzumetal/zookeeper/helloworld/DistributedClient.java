package com.lzumetal.zookeeper.helloworld;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by liaosi on 2017/11/18.
 */
public class DistributedClient {

    private static final Logger log = LoggerFactory.getLogger(DistributedClient.class);

    private static String connectString = "server01:2181,server02:2181,server03:2181";
    /*
     * minSessionTimeout, maxSessionTimeout -- 一般，客户端连接zookeeper的时候，都会设置一个session timeout，
     * 如果超过这个时间client没有与zookeeper server有联系，则这个session会被设置为过期(如果这个session上有临时节点，
     * 则会被全部删除，这就是实现集群感知的基础)。
     */
    private static int timeoutMillis = 2000;
    private static String serverGroupNode = "/servers";

    //为什么使用voaltile？
    private volatile List<String> serverNames;
    private ZooKeeper zooKeeper;

    public void getConnect() throws Exception {
        zooKeeper = new ZooKeeper(connectString, timeoutMillis, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("观察到事件：" + event.getType() + "--->" + event.getPath());
                try {
                    //重新更新服务器列表，因为在重新更新服务器列表的方法中，又注册了此监听器，所以依然会在监听
                    getServerList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取服务器的信息列表
    public void getServerList() throws KeeperException, InterruptedException {
        List<String> childrens = zooKeeper.getChildren(serverGroupNode, true);  //true表示对节点的删除、子节点的创建和删除时间是否更新
        serverNames = new ArrayList<>();
        for (String children : childrens) {
            byte[] data = zooKeeper.getData(serverGroupNode + "/" + children, false, null); //false表示对节点数据的更新删除等是否监听
            String serverName = new String(data);
            serverNames.add(serverName);
        }
        System.out.println(serverNames);
    }

    public void handlBussiness() throws InterruptedException {
        System.out.println("客户端开始工作....");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("客户端完成了工作！");
    }

    public static void main(String[] args) throws Exception {


        //1.获取ZooKeeper的连接
        DistributedClient distributedClient = new DistributedClient();
        distributedClient.getConnect();

        //2.获取servers的子节点信息（并监听），从中获取服务器列表
        distributedClient.getServerList();

        //3.业务处理
        distributedClient.handlBussiness();
    }
}
