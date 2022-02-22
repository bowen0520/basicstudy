package com.briup.zookeeper.day2;

import com.briup.zookeeper.util.MyWatcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper
 * @filename: WatcherTest.java
 * @create: 2019/10/16 17:05
 * @author: 29314
 * @description: .Watcher测试类
 **/

public class WatcherTest {
    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkWatcher zw = new ZkWatcher("192.168.112.121:2181");
        zw.connect();
        ZooKeeper zk = zw.getZk();

        zk.exists("/1", new MyWatcher(zk,"exists"));
        Thread.sleep(Long.MAX_VALUE);
    }
}
