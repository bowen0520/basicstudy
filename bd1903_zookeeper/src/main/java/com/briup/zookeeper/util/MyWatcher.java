package com.briup.zookeeper.util;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.util
 * @filename: MyWatcher.java
 * @create: 2019/10/17 15:27
 * @author: 29314
 * @description: .自定义监听类
 **/

public class MyWatcher implements Watcher{
    private ZooKeeper zk;
    private String handle;

    public MyWatcher() {
        zk = null;
        handle = null;
    }

    public MyWatcher(ZooKeeper zk, String handle) {
        this.zk = zk;
        this.handle = handle;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
        System.out.println("监听对象路径:" + path);
        Watcher.Event.KeeperState state = watchedEvent.getState();
        System.out.println("监听对象状态:" + state);
        Watcher.Event.EventType type = watchedEvent.getType();
        System.out.println("监听事件类型:" + type);
        if (zk!=null&&handle!=null) {
            try {
                if ("getData".equals(handle)) {
                    this.zk.getData(path, true, new Stat());
                } else if ("getChildren".equals(handle)) {
                    this.zk.getChildren(path, true);
                } else {
                    this.zk.exists(path, true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }
}
