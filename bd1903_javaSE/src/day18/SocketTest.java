package day18;

import java.io.*;
import java.net.*;

public class SocketTest {

    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
        System.out.println(inetAddress.getAddress());
        System.out.println(inetAddress.getHostAddress());
        System.out.println(inetAddress.getHostName());

        //定义URL
        URL url = new URL("www.baidu.com");
        //获取连接对象
        URLConnection urlConnection = url.openConnection();
        //打开连接对象
        urlConnection.connect();
        //获取与连接对象的流
        InputStream i = urlConnection.getInputStream();


    }
}

class Server{

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5432);
        Socket accept = serverSocket.accept();
        InetAddress inetAddress = accept.getInetAddress();
        String ip = inetAddress.getHostAddress();
        System.out.println(ip+"连接成功");
        InputStream inputStream = accept.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s = bufferedReader.readLine();
        System.out.println(s);
        PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
        printWriter.println("Hello Client");

    }

}

class Client{
    public static void main(String[] args) throws IOException {
        //在创建Socket对象时，就通过指定的ip和端口号去连接服务器
        Socket s = new Socket("127.0.0.1",5432);
        if(s.isConnected()){
            OutputStream os = s.getOutputStream();
            PrintWriter printWriter = new PrintWriter(os,true);
            printWriter.println("Hello Server");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String s1 = bufferedReader.readLine();
            System.out.println(s1);
            bufferedReader.close();
            printWriter.close();
        }
        s.close();
    }
}
