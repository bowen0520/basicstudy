package day19.udp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class GetMessage {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(8992);
        DatagramPacket datagramPacket = new DatagramPacket(new byte[1024],1024);
        datagramSocket.receive(datagramPacket);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        int i = dataInputStream.readInt();
        System.out.println(i);
        File file = new File("a.txt");
        PrintWriter p = new PrintWriter(file);
        p.println(i);
        p.flush();
        p.close();
        dataInputStream.close();
        byteArrayInputStream.close();
        datagramSocket.close();
    }
}
/*
编写一个数据报通信程序，一端发送int类型的10000，另一端接收并写入文件
 */
