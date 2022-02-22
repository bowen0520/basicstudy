package com.briup;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: bd1903_search_engine
 * @package: com.briup
 * @filename: test.java
 * @create: 2019/12/02 23:38
 * @author: 29314
 * @description: .
 **/

public class test implements Runnable{
    public static void main(String[] args) throws ClassNotFoundException {
        /*A a = new A();
        a.getClass();
        Class.forName("");
        String s = "sasa";
        int t = "adads".length();
        StringBuilder sb = new StringBuilder();
        StringBuffer sf = new StringBuffer();
        HashMap<Integer,Integer> hs = new HashMap<>();*/
        //ConcurrentHashMap
        LinkedHashMap lhm = new LinkedHashMap();
        Set<Integer> set = new HashSet<>();
        set.add(null);
        if(set.contains(null)){
            System.out.println(1);
        }

    }
    @Override
    public void run() {

    }
}

class A{}
class B extends A{}
class C extends B{}
