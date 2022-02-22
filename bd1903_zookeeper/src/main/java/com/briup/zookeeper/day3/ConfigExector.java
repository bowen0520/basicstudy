package com.briup.zookeeper.day3;

import com.briup.zookeeper.util.ZooKeeperUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.day3
 * @filename: ConfigExector.java
 * @create: 2019/10/17 15:16
 * @author: 29314
 * @description: .配置信息执行器
 **/

public class ConfigExector {
    private static ZooKeeper zk;
    private static String createNodePath;

    static {
        try {
            zk = ZooKeeperUtil.getZK("192.168.112.121");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void regist(String nodePath, String data, CreateMode cm) throws Exception {
        Stat exists = zk.exists(nodePath, false);
        if(exists!=null){
            throw new Exception("节点已存在");
        }
        zk.create(nodePath,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,cm);
    }

    public static String load(String nodePath) throws Exception {
        Stat exists = zk.exists(nodePath, false);
        if(exists!=null){
            throw new Exception("节点已存在");
        }
        byte[] data = zk.getData(nodePath, false, new Stat());
        return new String(data);
    }
}
