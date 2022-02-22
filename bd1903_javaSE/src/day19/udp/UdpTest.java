package day19.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @program: BD1903.JavaSE
 * @description: .UDP编程演示
 * @author: Kevin
 * @create: 2019-08-16 15:16
 **/
public class UdpTest{}

class Sender{
    public static void main(String[] args) throws IOException{
        // 1.定义DatagramSocket对象，用于发送数据
        DatagramSocket ds=new DatagramSocket();

        // 2.定义要发送的数据报包，创建DatagramPacket对象
        // 在发送端定义的byte数组中包含要发送的数据的
        byte[] data="你好，全世界！".getBytes();
        InetAddress ia=InetAddress.getByName("127.0.0.1");
        DatagramPacket dp=
            new DatagramPacket(data, data.length,ia,23456);

        ds.send(dp);
    }
}

class Receiver{
    public static void main(String[] args) throws IOException{
        // 接收端创建DatagramSocket对象，但是需要指定端口；
        DatagramSocket ds=new DatagramSocket(23456);

        // 定义DatagramPacket对象，用于接收发送端发送的包裹
        byte[] bytes=new byte[1024];
        DatagramPacket dp=new DatagramPacket(bytes, bytes.length);

        // 接收发送端发送的数据
        ds.receive(dp);

        // 接收到的数据被存储在dp中
        // 获取发送端的IP
        String ip=dp.getAddress().getHostAddress();

        // 获取数据
        String data=new String(dp.getData(),0,dp.getLength());

        System.out.println(ip+":::"+data);
    }
}
