package com.briup.bigdata.reflex;

import java.lang.reflect.Field;
import java.util.Vector;

/**
 * @program: bd1903_spark
 * @package: com.briup.bigdata.reflex
 * @filename: Test.java
 * @create: 2019/11/20 20:21
 * @author: 29314
 * @description: .
 **/

public class Test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        int i = 12;
        i += i -= i *= i;
        System.out.println(i);
        //Student s = new Student();
        //Student student = setValue(s, "name", "zs");
        //System.out.println(student.toString());
    }

    public static  <T> T setValue(T t,String k,String v) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = t.getClass();
        Field declaredField = aClass.getDeclaredField(k);
        declaredField.setAccessible(true);
        declaredField.set(t,v);
        return t;
    }
}
