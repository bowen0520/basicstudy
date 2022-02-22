package day20;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class Homework {
    //static Map<Integer,String> map = new HashMap<>();
    static List<String> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//        list.forEach(o-> System.out.println(o));
//        list.forEach(System.out::println);
//        Iterator<Integer> iterator = list.iterator();
//        list.sort((o1,o2)-> o1 - o2);
//        list.sort(Comparator.comparingInt(o -> o));
//        list.forEach(System.out::println);
//        list.sort(Comparator.comparingInt(o -> o));
        getMessage();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("输入页码：");
            int line = scanner.nextInt();
            /*if (map.containsKey(line)) {
                String str = map.get(line);
                System.out.println(str);
            } else {
                System.out.println("不存在改页");
            }*/
            int index = 5*(line-1);
            if(list.size()>index){
                for(int i = 0;index+i<list.size()&&i<5;i++){
                    System.out.println(list.get(index+i));
                }
            }else{
                System.out.println("不存在改页");
            }
        }
    }
    /*
    public static void getMessage() throws IOException {
        File file = new File("code.txt");
        if((!file.exists())||(!file.isFile())){
            return ;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int line = 0;
        String message = null;
        while((message = bufferedReader.readLine())!=null) {
            if (line % 5 == 0) {
                map.put((line / 5) + 1, message + "\r\n");
            } else {
                map.put((line / 5) + 1, map.get((line / 5) + 1) + message + "\r\n");
            }
            line++;
        }
        bufferedReader.close();
    }
    */
    public static void getMessage() throws IOException {
        File file = new File("code.txt");
        if((!file.exists())||(!file.isFile())){
            return ;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String message = null;
        while((message = bufferedReader.readLine())!=null) {
            list.add(message);
        }
        bufferedReader.close();
    }
}
