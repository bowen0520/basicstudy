package com.briup.zookeeper.day2;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @program: bd1903_zookeeper
 * @package: main.java.com.briup.zookeeper
 * @filename: TestConnect.java
 * @create: 2019/10/15 16:57
 * @author: 29314
 * @description: .zookeep连接测式类
 **/

public class TestConnect {
    private static CountDownLatch cdl = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        //zookeeper对象的创建是异步执行的
        //在new ZooKeeper对象的过程中主线程会新开启一个子线程
        //子线程来连接ZooKeeper的服务器时，如果连接成功，则返回该ZooKeeper对象
        ZooKeeper zk = new ZooKeeper("192.168.112.122", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("创建对象的线程："+Thread.currentThread().getName());
                System.out.println("观察的事件"+watchedEvent);
                cdl.countDown();
            }
        });
        System.out.println(zk);
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(zk);
    }
}
