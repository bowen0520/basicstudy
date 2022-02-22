package com.briup.bigdata;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: bd1903_sparkStudy
 * @package: com.briup.bigdata
 * @filename: Test.java
 * @create: 2019/11/24 19:42
 * @author: 29314
 * @description: .
 **/

public class Test {

    public static void main(String[] args) {
        //People p = new Student("zs",13,123);
        //System.out.println(p.age);
        String s = "1233213";
        String s1 = "123" + "3213";
        String s2 = "123";
        String s3 = "3213";
        String s4 = s2 + s3;
        System.out.println(s==s1);
        System.out.println(s==s4);
        System.out.println(s1==s4);
        new LinkedList<String>();
        new ArrayList<String>();
    }
}
class People{
    String name;
    int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class Student extends People{
    int id;

    public Student(String name, int age, int id) {
        super(name, age);
        this.id = id;
    }
}
interface Lon{
    default int getSum(){
        return 1 + 2;
    }
}
class Singleton {

    private Singleton() {
    }

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }
}