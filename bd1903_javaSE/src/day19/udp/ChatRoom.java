package day19.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @program: BD1903.JavaSE
 * @description: .基于UDP的聊天室
 * @author: Kevin
 * @create: 2019-08-16 15:57
 **/
public class ChatRoom{
    public static void main(String[] args) throws SocketException{
        new ReceiveThread(12345).start();
        new SendThread("172.16.23.255",12345).start();
    }
}

// 多线程
class SendThread extends Thread{
    private DatagramSocket ds;
    private String ip;
    private int port;

    SendThread(String ip,int port) throws SocketException{
        this.ds=new DatagramSocket();
        this.ip=ip;
        this.port=port;
    }

    @Override
    public void run(){
        try{
            // 接收键盘录入，然后将消息发送出去
            // 一直在执行，接口键盘录入时等待
            BufferedReader br=
                new BufferedReader(
                    new InputStreamReader(System.in));
            String message;
            while((message=br.readLine())!=null){
                byte[] bytes=message.getBytes();
                // 声明接收端的IP地址封装成InetAddress对象
                InetAddress ia=InetAddress.getByName(ip);
                // 构建DatagramPacket对象，封装消息
                DatagramPacket dp=
                    new DatagramPacket(bytes,bytes.length,ia,port);
                // 发送数据包
                this.ds.send(dp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

// 群聊，接收端只是接收消息做显示
class ReceiveThread extends Thread{
    private DatagramSocket ds;

    ReceiveThread(int port) throws SocketException{
        this.ds=new DatagramSocket(port);
    }

    @Override
    public void run(){
        System.out.println("==================接收端已启动=====================");
        // 定义字节数组，用于存放接收到的数据
        byte[] bytes=new byte[1024];

        while(true){
            try{
                // 定义DatagramPacket对象，用于接收数据
                DatagramPacket dp=new DatagramPacket(bytes, bytes.length);

                // 接收数据
                // 阻塞式方法
                this.ds.receive(dp);

                // 获取发送者的IP
                String ip=dp.getAddress().getHostAddress();

                // 获取发送者发送的消息
                String data=new String(dp.getData(),0,dp.getLength());

                // 数据处理
                System.out.println(ip+"：......... "+data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

