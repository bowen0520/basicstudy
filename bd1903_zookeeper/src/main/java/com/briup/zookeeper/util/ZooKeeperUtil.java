package com.briup.zookeeper.util;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.util
 * @filename: ZooKeeperUtil.java
 * @create: 2019/10/17 15:25
 * @author: 29314
 * @description: .ZooKeeper的工具类
 **/

public class ZooKeeperUtil {
    public static ZooKeeper getZK(String ip) throws IOException {
        return new ZooKeeper(ip, 5000, new MyWatcher());
    }

    public static ZooKeeper getZK(String ip, Watcher watcher) throws IOException {
        return new ZooKeeper(ip, 5000, watcher);
    }
}
