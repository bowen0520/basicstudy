package day19.tcp;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public Client() throws IOException {
        this.socket = new Socket("127.0.0.1",8991);
        this.printWriter = new PrintWriter(socket.getOutputStream(),true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void writeMessage(String filename) throws IOException {
        printWriter.println(filename);
        System.out.println("请求发出");
        String str = "";
        File file = new File(filename);
        if(!file.exists()) {
            file.createNewFile();
        }
        PrintWriter fileWriter = new PrintWriter(file);
        while((str = bufferedReader.readLine())!=null){
            fileWriter.println(str);
        }
        fileWriter.close();
        System.out.println("请求成功");
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new Client().writeMessage("c.txt");
    }
}
