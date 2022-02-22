package day16;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class FileTest {
    public static void main(String[] args) throws IOException {
        //FileTest fileTest = new FileTest();
        /*
        File tempFile = File.createTempFile("abc", "aca",fileTest.getFile());
        System.out.println(tempFile);
        tempFile.deleteOnExit();

        System.out.println(fileTest.getFile().getTotalSpace());
        System.out.println(fileTest.getFile().getFreeSpace());
        System.out.println(fileTest.getFile().getUsableSpace());*/
        /*File file1 = new File("D:/book/a.txt");
        File file2 = new File("E:/book");
        file1.createNewFile();
        file2.mkdir();
        System.out.println(file1.getName());
        System.out.println(file2.getName());
        new File(file2,"");
        fileTest.copyFile(file1,file2);*/
        //fileTest.copyD("D:\\book","E:\\");
        //fileTest.delete("C:\\a\\b");
        String a = new String("aaa");
        String b = new String("aaa");
        String c= "ccc";
        String d = "ccc";
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(a==b);
        System.out.println(c.hashCode());
        System.out.println(d.hashCode());
        System.out.println(c==d);
    }
    public File getFile() {
        //通过File(String pathname)创建
        File file = new File("D:\\a.txt");
        return file;
    }
    public void copy(String path1,String path2) throws IOException {
        File file1 = new File(path1);//获取源文件
        File file2 = new File(path2);
        if(!file1.exists()||!file2.exists()){//判断源文件是否存在
            throw new IOException("文件不存在，无法复制");
        }
        if(file1.isFile()){
            copyFile(file1,file2);
        }else{
            Queue<File> que1 = new LinkedList<>();
            Queue<File> que2 = new LinkedList<>();
            File file = new File(file2,file1.getName());
            file.mkdir();
            que1.add(file1);
            que2.add(file);
            while(!que1.isEmpty()){
                File fileTemp1 = que1.poll();
                File fileTemp2 = que2.poll();
                File[] files = fileTemp1.listFiles();
                for(File f:files){
                    File newFile;
                    if(f.isDirectory()){
                        newFile = new File(fileTemp2,f.getName());
                        newFile.mkdir();
                        que1.offer(f);
                        que2.offer(newFile);
                    }else{
                        copyFile(f,fileTemp2);
                    }
                }
            }
        }
    }

    public void delete(String path){
        File file = new File(path);
        if(file.toURI().toString().startsWith("file:/C:")){
            System.out.println("不能操作c盘");
            return ;
        }
        if(file.isFile()){
            file.delete();
        }else{
            delete(file);
        }
    }

    public void delete(File file){
        File[] files = file.listFiles();
        for(File f:files){
            if(f.isFile()){
                f.delete();
            }else{
                delete(f);
            }
        }
        file.delete();
    }

    public void copyD(String path1,String path2) throws IOException {
        File file1 = new File(path1);//获取源文件
        File file2 = new File(path2);
        if(!file1.exists()||!file2.exists()){//判断源文件是否存在
            throw new IOException("文件不存在，无法复制");
        }
        copyD(file1,file2);
    }

    public void copyD(File file1,File file2) throws IOException {
        if(file1.isFile()){
            copyFile(file1,file2);
        }else{
            File file = new File(file2,file1.getName());
            file.mkdir();
            File[] files = file1.listFiles();
            for(File f:files){
                if(f.isDirectory()){
                    copyD(f,file);
                }else{
                    copyFile(f,file);
                }
            }
        }
    }

    public void copyFile(File file1,File file2) throws IOException {
        String name = file1.getName();
        File file = new File(file2,name);
        file.createNewFile();
        FileInputStream inputStream = new FileInputStream(file1);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int length = 0;
        while((length = inputStream.read(b))!=-1){
            outputStream.write(b);
        }
        inputStream.close();
        outputStream.close();
    }
}
