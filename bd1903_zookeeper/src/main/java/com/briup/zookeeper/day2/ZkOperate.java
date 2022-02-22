package com.briup.zookeeper.day2;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper
 * @filename: ZkOperate.java
 * @create: 2019/10/16 14:14
 * @author: 29314
 * @description: .对于zookeeper节点的增删改查，zookeeper同时提供了同步和异步的api
 **/

public class ZkOperate {
    private ZooKeeper zk;

    public ZkOperate() {
        ZkWatcher zw = new ZkWatcher("192.168.112.121:2181");
        zw.connect();
        this.zk = zw.getZk();
        System.out.println("客户端会话id"+this.zk.getSessionId());
    }

    //创建节点：create /abc ""
    public void createNode(String nodePath,String data,boolean sync) throws KeeperException, InterruptedException {
        if(sync){//同步创建
            System.out.println("同步——————————————————————————————");
            String path = this.zk.create(nodePath,//节点路径
                    data.getBytes(),//传入的数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,//开放的权限
                    CreateMode.PERSISTENT//节点类型
            );
            System.out.println("同步创建的节点："+path);
        }else{//异步创建
            System.out.println("异步——————————————————————————————");
            this.zk.create(nodePath,//节点路径
                    data.getBytes(),//传入的数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,//开放的权限
                    CreateMode.PERSISTENT,//节点类型
                    new AsyncCallback.Create2Callback() {
                        @Override
                        public void processResult(int rc, String path, Object ctx, String name, Stat stat) {
                            System.out.println("异步创建的执行线程："+Thread.currentThread().getName());
                            KeeperException.Code code = KeeperException.Code.get(rc);
                            System.out.println("异步创建的返回码："+rc+" 内容："+code);
                            System.out.println("异步节点路径："+path);
                            System.out.println("异步节点名称："+name);
                            System.out.println("主线程传递的参数"+ctx);
                            System.out.println("异步创建的节点的源数据"+stat);
                        }
                    },
                    null
            );
        }
    }
    //递归创建节点
    /*public void createAllNode(String nodePath,String data,boolean sync) throws KeeperException, InterruptedException {
        if(sync){//同步创建
            this.zk.
            String path = this.zk.create(nodePath,//节点路径
                    data.getBytes(),//传入的数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,//开放的权限
                    CreateMode.PERSISTENT//节点类型
            );
            System.out.println("同步创建的节点："+path);
        }else{//异步创建
            this.zk.create(nodePath,//节点路径
                    data.getBytes(),//传入的数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,//开放的权限
                    CreateMode.PERSISTENT,//节点类型
                    new AsyncCallback.Create2Callback() {
                        @Override
                        public void processResult(int rc, String path, Object ctx, String name, Stat stat) {
                            System.out.println("异步创建的执行线程："+Thread.currentThread().getName());
                            KeeperException.Code code = KeeperException.Code.get(rc);
                            System.out.println("异步创建的返回码："+rc+" 内容："+code);
                            System.out.println("异步节点路径："+path);
                            System.out.println("异步节点名称："+name);
                            System.out.println("主线程传递的参数"+ctx);
                            System.out.println("异步创建的节点的源数据"+stat);
                        }
                    },
                    null
            );
        }
    }*/

    //删除节点 delete -v -1 /test
    public void deleteNode(String nodePath,boolean sync) throws KeeperException, InterruptedException {
        if (sync){//同步删除
            this.zk.delete(nodePath,-1);
        }else{//异步删除
            this.zk.delete(nodePath, -1, new AsyncCallback.VoidCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx) {
                    KeeperException.Code code = KeeperException.Code.get(rc);
                    if(code==KeeperException.Code.OK){
                        System.out.println(path+"删除成功");
                    }else if(code==KeeperException.Code.NONODE){
                        System.out.println(path+"节点不存在");
                    }else if(code==KeeperException.Code.NOTEMPTY){
                        System.out.println(path+"节点非空");
                    }
                }
            },null);
        }
    }

    //获取节点数据 get /test
    public void getData(String nodePath,boolean sync) throws KeeperException, InterruptedException {
        if(sync){//同步获取
            Stat stat = new Stat();
            byte[] data = this.zk.getData(nodePath, false, stat);
            System.out.println("同步获取的数据："+new String(data));
            System.out.println("同步获取的节点元数据的版本:"+ stat.getVersion());
            System.out.println("同步获取的节点元数据:"+ stat);
        }else {//异步获取
            this.zk.getData(nodePath, false, new AsyncCallback.DataCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                    KeeperException.Code code = KeeperException.Code.get(rc);
                    System.out.println("异步获取的执行结果:"+ code);
                    System.out.println("异步获取的数据:"+ new String(data));
                    System.out.println("异步获取的节点元数据的版本:"+ stat.getVersion());
                    System.out.println("异步获取的节点元数据:"+ stat);
                }
            },null);
        }
    }

    //设置节点数据 set /test "XXXX"
    public void setData(String nodePath,String data,boolean sync) throws KeeperException, InterruptedException {
        if(sync){//同步修改
            Stat stat = this.zk.setData(nodePath, data.getBytes(), -1);
            System.out.println("同步修改后获取的节点元数据的版本:"+ stat.getVersion());
        }else {//异步修改
            this.zk.setData(nodePath,data.getBytes(),-1,new AsyncCallback.StatCallback(){
                @Override
                public void processResult(int rc, String path, Object o, Stat stat) {
                    System.out.println("异步修改后获取的节点元数据的版本:"+ stat.getVersion());
                }
            },null);
        }
    }
    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkOperate zko = new ZkOperate();
//        zko.createNode("/test","test",true);
//        zko.createNode("/test/test1","test1",true);
//        zko.createNode("/test/test2","test2",false);

//        zko.deleteNode("/test1",true);
//        zko.deleteNode("/test2",false);

        /*zko.getData("/test",true);
        zko.getData("/test1",false);
        zko.getData("/test",false);*/
        zko.getData("/test",true);
        zko.setData("/test","newTest",true);
        zko.getData("/test",false);
        zko.setData("/test","test",false);
        Thread.sleep(6000);
    }
}
