package com.briup.zookeeper.day2;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper
 * @filename: ZkWatcher.java
 * @create: 2019/10/15 17:26
 * @author: 29314
 * @description: .自定义watcher
 **/

public class ZkWatcher implements Watcher {
    private String connStr;
    private CountDownLatch cdl;
    private ZooKeeper zk;

    public ZkWatcher(String connStr) {
        this.connStr = connStr;
        this.cdl = new CountDownLatch(1);
    }

    public void connect(){
        try {
            this.zk = new ZooKeeper("192.168.112.122", 5000, this);
            //让主线程暂停
            this.cdl.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        /*System.out.println("创建对象的线程："+Thread.currentThread().getName());
        System.out.println("观察的事件"+watchedEvent);*/
        if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
            this.cdl.countDown();
        }
    }

    public ZooKeeper getZk() {
        return zk;
    }
}
