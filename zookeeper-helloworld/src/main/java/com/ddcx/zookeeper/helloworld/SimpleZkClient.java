package com.ddcx.zookeeper.helloworld;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Created by liaosi on 2017/11/18.
 */
public class SimpleZkClient {

    private static final Logger log = LoggerFactory.getLogger(SimpleZkClient.class);

    private static String connectString = "server_01:2181,server_02:2181,server_03:2181";
    private static int timeoutMils = 2000;
    private ZooKeeper zooKeeper;


    @Before
    public void init() throws Exception {
        zooKeeper = new ZooKeeper(connectString, timeoutMils, new Watcher() {

            //收到事件通知后的回调函数（即我们自己的时间处理逻辑）
            @Override
            public void process(WatchedEvent event) {
                log.info(event.getPath() + "---->" + event.getType());
            }
        });

    }


    /**
     * 数据的增删改查
     */

    //创建节点
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        //参数1：节点的路径；参数2：节点的数据；参数3：节点的权限；参数4：节点的类型
        zooKeeper.create("/idea", "hello zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //上传的数据可以是任何类型，但都要转成byte数组
    }

    //获取子节点
    @Test
    public void getGetChild() throws KeeperException, InterruptedException {

        //true表示注册了创建zookeeper时的监听器，也可以重新注册一个新的监听器
        List<String> childrens = zooKeeper.getChildren("/", true);

        for (String children : childrens) {
            log.info(children);
        }

    }

    //判断节点是否存在
    @Test
    public void testExist() throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists("/idea", true);
        System.out.println(exists == null ? "Not exist" : "Exist");
    }

    //获取节点数据
    @Test
    public void testGetData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/idea", false, null);
        System.out.println(new String(data));
    }

    //删除节点
    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        //-1表示将所有节点都删除
        zooKeeper.delete("/idea", -1);
    }
}
