package com.briup.zookeeper.day3;

import org.apache.zookeeper.CreateMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.day3
 * @filename: Server.java
 * @create: 2019/10/17 16:05
 * @author: 29314
 * @description: .服务端
 **/

public class Server {
    public static void main(String[] args) {
        int port = 8888;
        ServerSocket ss = null;
        while(true){
            try {
                ss = new ServerSocket(port);
                break;
            } catch (IOException e) {
                System.err.println("端口被占用");
                port++;
            }
        }
        System.out.println("端口为："+port);

        //向zk注册服务器监听的端口
        try {
            ConfigExector.regist("/server","127.0.0.1:"+port, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket accept = ss.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                            System.out.println("客户端信息："+br.readLine());
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
