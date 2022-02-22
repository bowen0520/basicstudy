package day12;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;

public class Reflect {
    /*
    public static void main(String[] args) throws ClassNotFoundException {
        //1.通过类名调用.class属性，也适用于基本数据类型
        Class<Person> pc = Person.class;
        Class<Integer> ic = int.class;
        System.out.println(pc);
        System.out.println(ic);

        //2.通过对象调用getClass（）方法，不适用基本数据类型
        Person p = new Person();
        Class<? extends Person> pc2 = p.getClass();
        System.out.println(pc2);
        //3.在程序中不能直接引入该类的类名，而是通过字符串的形式提供该类的类全名

        String stu = "day12.Student";
        Class<?> aClass = Class.forName(stu);
        System.out.println(stu);

    }*/

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Class<Stu> stu = Stu.class;
        Constructor<Stu> constructor = stu.getConstructor(String.class,int.class,int[].class);
        Scanner s = new Scanner(System.in);
        System.out.println("输入名字");
        String name = s.next();
        System.out.println("输入年龄");
        int age= s.nextInt();
        int[] grade = new int[3];
        System.out.println("输入成绩：");
        for(int i = 0;i<3;i++){
            grade[i] = s.nextInt();
        }
        Stu s1 = (Stu)constructor.newInstance(name,age,grade);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("Student")));
        bufferedWriter.write(s1.toString());
        bufferedWriter.close();
    }
}
class Stu {
    private String name;
    private int age;
    private int[] grades;

    public Stu(String name, int age, int[] grades) {
        this.name = name;
        this.age = age;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "stu{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", grades=" + Arrays.toString(grades) +
                '}';
    }
}


class P{
    public double height;
    private double weight;
    String gender;
    protected String id;
}
class S extends P{

}
