package com.briup.zookeeper.day2;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper
 * @filename: ZkOperateRecursion.java
 * @create: 2019/10/16 16:15
 * @author: 29314
 * @description: .递归对节点操作
 **/

public class ZkOperateRecursion {
    private ZooKeeper zk;

    public ZkOperateRecursion() {
        ZkWatcher zw = new ZkWatcher("192.168.112.121:2181");
        zw.connect();
        this.zk = zw.getZk();
        System.out.println("客户端会话id"+this.zk.getSessionId());
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkOperateRecursion zkOperateRecursion = new ZkOperateRecursion();
        zkOperateRecursion.getChildren("/1",true);
        //zkOperateRecursion.getChildren("/test",false);
        Thread.sleep(6000);
    }

    public void getChildren(String nodePath,boolean sync) throws KeeperException, InterruptedException {
        byte[] data = this.zk.getData(nodePath, false, new Stat());
        System.out.println("节点"+nodePath+"的内容为："+new String(data));
        if (sync){//同步执行
            List<String> children = this.zk.getChildren(nodePath, false);
            children.forEach(o->{
                String newPath = nodePath+(nodePath.equals("/")?"":"/")+o;
                try {
                    this.getChildren(newPath,true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }else {//异步执行
            this.zk.getChildren(nodePath, false, new AsyncCallback.Children2Callback() {
                @Override
                public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                    children.forEach(System.out::println);
                }
            },null);

        }
        this.zk.delete(nodePath,-1);
    }
}
