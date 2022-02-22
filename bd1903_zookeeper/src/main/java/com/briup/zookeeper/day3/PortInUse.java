package com.briup.zookeeper.day3;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.day3
 * @filename: PortInUse.java
 * @create: 2019/10/17 16:16
 * @author: 29314
 * @description: .模拟端口占用
 **/

public class PortInUse {
    public static void main(String[] args) throws IOException {
        ServerSocket ss1 = new ServerSocket(8888);
        ServerSocket ss2 = new ServerSocket(8889);
    }
}
