package com.briup.zookeeper.day3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @program: bd1903_zookeeper
 * @package: com.briup.zookeeper.day3
 * @filename: Client.java
 * @create: 2019/10/17 16:25
 * @author: 29314
 * @description: .客户端
 **/

public class Client {
    public static void main(String[] args) {
        String data = null;
        try {
            data = ConfigExector.load("/server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] split = data.split(":");

        try {
            Socket socket = new Socket(split[0],Integer.parseInt(split[1]));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
            pw.println("dadadadadadadadadadad");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
