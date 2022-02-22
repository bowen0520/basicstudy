package day19.udp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SendMessage {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        int message = 10000;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(message);
        dataOutputStream.flush();
        byte[] data= byteArrayOutputStream.toByteArray();
        InetAddress ia=InetAddress.getByName("127.0.0.1");
        DatagramPacket datagramPacket = new DatagramPacket(data,data.length,ia,8992);
        datagramSocket.send(datagramPacket);
        dataOutputStream.close();
        byteArrayOutputStream.close();
        datagramSocket.close();
    }
}
