package day19.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GetFile {
    private ServerSocket serverSocket;
    private File dir;

    public GetFile() throws IOException {
        this.serverSocket = new ServerSocket(8991);
        dir = new File("E:\\a");
    }

    public void sendFileMessage() {
        while(true){
            try {
                Socket accept = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                String name = bufferedReader.readLine();
                System.out.println(name);
                System.out.println("请求收到");
                PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
                File file = findFile(dir, name);
                System.out.println(file);
                if(file!=null) {
                    BufferedReader fileRead = new BufferedReader(new FileReader(file));
                    String message = "";
                    while((message = fileRead.readLine())!=null){
                        printWriter.println(message);
                    }
                    System.out.println("文件发送完毕");
                    fileRead.close();
                }else {
                    System.out.println("文件不存在");
                    printWriter.println("文件不存在");
                }
                accept.close();
            }catch (IOException e){
                System.out.println("断开连接");
            }
        }
    }

    public File findFile(File file,String name){
        File[] files = file.listFiles();
        File fs = null;
        for(File f:files){
            if(f.isFile()){
                if(f.getName().equals(name)){
                    return f;
                }
            }else{
                fs = findFile(f, name);
                if(fs!=null){
                    return fs;
                }
            }
        }
        return fs;
    }

    public static void main(String[] args) throws IOException {
        new GetFile().sendFileMessage();
    }
}
/*
采用套接字的链接方式编写一个程序，允许客户向服务器提出一个名字，
如果这个文件存在，就把该文件内容发送给客户，否则回答文件不存在
 */
