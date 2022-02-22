package com.briup.bigdata.day01;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @program: test
 * @package: com.briup.bigdata.day01
 * @filename: Test.java
 * @create: 2019/11/14 16:25
 * @author: 29314
 * @description: .
 **/

public class Test {

    private int a;

    Test(int b) {
        b = a;
    }

    /*public static void main(String[] args) {
        Test test = new Test(3);
        System.out.println(test.a);
    }*/

    public static void main(String[] args) {

        Integer a = 2;
        Integer b = new Integer(2);
        System.out.println(a==b);
        System.out.println("____________");

        Map<Integer,Integer> map = new HashMap();
        Map<Integer,Integer> table = new Hashtable<>();
        Map<Integer,Integer> cmap = new ConcurrentHashMap<>();

        map.put(null,1);
        if(map.containsKey(null)){
            map.get(null);
        }

        System.out.println("_____________________________");
        //table.put(null,1);
        //get(1,2);
        //System.out.println(get());

        //List<Integer> list = new ArrayList<>();
        List<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        /*for(int i:list){
            list.remove(i);
            if(i==4){
                break;
            }
        }*/
        //ConcurrentHashMap<Integer,Integer> map = new ConcurrentHashMap<>();

        //list.forEach(System.out::println);
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            int n = iterator.next();
            System.out.println(n);
            list.remove(0);
            list.forEach(o->{
                System.out.print(o+" ");
            });
            System.out.println();
        }



        //list.forEach(System.out::println);
    }

    public static int get() {
        int j = 1;
        try {

            int i = 10 / 0;
        } catch (Exception e) {
            System.out.println("adas");
            return j++;
        } finally {
            return j++ + 1;
        }
    }

    public static int get(int a, char b) {
        return 1;
    }
}
