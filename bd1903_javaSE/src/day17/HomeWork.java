package day17;

import java.io.*;
import java.util.*;

public class HomeWork {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /*
        Scanner scanner = new Scanner(System.in);
        String path = scanner.next();
        printJava(path);
        */

        /*
        Queue<Character> queue = new LinkedList<>();
        FileReader fileReader = new FileReader("E:\\briup\\javaSE\\day17\\homework\\io流作业.TXT");
        Map<Character,Integer> map = new HashMap<>();
        FileWriter fileWriter = new FileWriter("count.txt");
        Thread t1 = new Thread(new Read(queue,fileReader));
        Thread t2 = new Thread(new Statistics(queue,map));
        Thread t3 = new Thread(new Write(map,fileWriter));
        t1.start();
        t2.start();
        while(t1.isAlive()||t2.isAlive()){}
        t3.start();
        while(t3.isAlive()){}
        fileReader.close();
        fileWriter.close();
        */
        File file = new File("D:\\ideal-workplace\\Test\\config.txt");
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file,true));
        dataOutputStream.writeInt(10);
        dataOutputStream.flush();
        dataOutputStream.close();
        useData();

        //useByte();

        //writeObject();
        //readObject();

        //changeCode();
    }
    public static void changeCode2() throws IOException {
        File file1 = new File("code.txt");
        File file2 = new File("newcode.txt");
        byte[] b = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(file1);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int length = 0;
        while((length = fileInputStream.read(b))!=-1){
            byteArrayOutputStream.write(b,0,length);
        }
        byte[] gbks = byteArrayOutputStream.toString("GBK").getBytes("UTF-8");
        fileOutputStream.write(gbks);

        byteArrayOutputStream.flush();
        fileOutputStream.flush();
        byteArrayOutputStream.close();
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void changeCode() throws IOException {
        File file1 = new File("code.txt");
        File file2 = new File("newcode.txt");
        char[] b = new char[1024];
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file1),"GBK"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2),"UTF-8"));
        int length = 0;
        while((length = bufferedReader.read(b))!=-1){
            bufferedWriter.write(b,0,length);
        }
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();
    }

    public static void writeObject() throws IOException {
        File file = new File("Object.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        MyList<Student> myList = new MyList<>(new ArrayList<>());
        myList.add(new Student("张三",18,'男'));
        myList.add(new Student("李四",23,'男'));
        myList.add(new Student("小花",16,'女'));
        myList.add(new Student("小芳",22,'女'));
        objectOutputStream.writeObject(myList);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
    public static void readObject() throws IOException, ClassNotFoundException {
        File file = new File("Object.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Object o = objectInputStream.readObject();
        MyList<Student> myList = (MyList<Student>) o;
        System.out.println(myList.toString());
        objectInputStream.close();
    }

    public static void useData() throws IOException {
        File file = new File("D:\\ideal-workplace\\Test\\config.txt");
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file,true));
        int i = dataInputStream.readInt();
        System.out.println(i);
        if(i==0){
            System.out.println("免费使用结束请购买");
        }else{
            i--;
        }
        dataOutputStream.writeInt(i);
        dataOutputStream.flush();
        dataOutputStream.close();
        dataInputStream.close();
    }

    public static void useByte() throws IOException {
        File file = new File("a.txt");
        byte[] bytes = new byte[5];
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int length = 0;
        while((length=fileInputStream.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,length);
        }
        System.out.println(byteArrayOutputStream.toString("UTF-8"));
        byteArrayOutputStream.flush();
        fileInputStream.close();
        byteArrayOutputStream.close();
    }

    public static void printJava(String path) throws FileNotFoundException {
        File file = new File(path);
        if(!file.exists()){
            throw new FileNotFoundException("文件不存在");
        }
        printJava(file);
    }
    public static void printJava(File file){
        File[] files = file.listFiles();
        for(File f:files){
            if(f.getName().endsWith(".java")){
                System.out.println(f.getName());
            }
            if(f.isDirectory()){
                printJava(f);
            }
        }
    }
}

class MyList<T> implements Serializable{
    List<T> list;

    public MyList(List<T> list) {
        this.list = list;
    }

    public boolean add(T t){
        return list.add(t);
    }

    public boolean remove(T t){
        return list.remove(t);
    }

    public T remove(int i){
        return list.remove(i);
    }

    public T remove(){
        return list.remove(list.size()-1);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach((o)->{
            stringBuilder.append(o.toString()+"\n");
        });
        return stringBuilder.toString();
    }
}

class Student implements Serializable{
    String name;
    int age;
    char gender;

    public Student(String name, int age, char gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return name + " " + age + " " + gender;
    }
}

class Write implements Runnable{
    Map<Character,Integer> map;
    FileWriter fileWriter;

    public Write(Map<Character, Integer> map, FileWriter fileWriter) {
        this.map = map;
        this.fileWriter = fileWriter;
    }


    @Override
    public void run() {
        Set<Map.Entry<Character, Integer>> entries = map.entrySet();
        entries.forEach((o)->{
            try {
                fileWriter.write(o.getKey()+" "+o.getValue()+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

class Statistics implements Runnable{
    Queue<Character> queue;
    Map<Character,Integer> map;

    public Statistics(Queue<Character> queue, Map<Character, Integer> map) {
        this.queue = queue;
        this.map = map;
    }

    @Override
    public void run() {
        while(true){
            synchronized ("suo") {
                if (queue.isEmpty()) {
                    try {
                        "suo".wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(queue.isEmpty()){
                break;
            }
            while(!queue.isEmpty()){
                char c = queue.poll();
                if(map.containsKey(c)){
                    map.put(c,map.get(c)+1);
                }else{
                    map.put(c,1);
                }
            }
        }
    }
}

class Read implements Runnable{
    Queue<Character> queue;
    FileReader fileReader;

    public Read(Queue<Character> queue, FileReader fileReader) {
        this.queue = queue;
        this.fileReader = fileReader;
    }

    @Override
    public void run() {
        while(true){
            char[] chars = new char[100];
            try {
                int length = fileReader.read(chars);
                if(length==-1){
                    break;
                }
                for(int i = 0;i<length;i++){
                    queue.offer(chars[i]);
                }
                synchronized ("suo"){
                    "suo".notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

