package day12;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @program: BD1903.JavaSE
 * @description: .反射演示
 * @author: Kevin
 * @create: 2019-08-07 09:40
 **/
public class ReflectTest{
    public static void main(String... args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException{
        // 1.通过类名调用.class属性，也适用于基本数据类型
        Class<Person> pc=Person.class;
        //获取类对象
        Person person=pc.newInstance();
        //获取无参构造器
        Constructor<Person> constructor=pc.getConstructor();

        // System.out.println(pc);
        // System.out.println("---------------------------------");

        // 2.通过对象调用getClass()方法，不适用于基本数据类型
        // Student student=new Student("张三",10);
        // Class<? extends Student> sc=student.getClass();
        //
        // Person person=new Person();
        // Class<? extends Person> pc1=person.getClass();
        //
        // System.out.println(sc);
        // System.out.println(pc1);
        //
        // System.out.println("-------------------------------");

        // 3.在程序中不能直接引入该类的类名，而是通过字符串
        // 的形式提供该类的类全名
        String studentClassStr="com.briup.bigdata.bd1903.java.day12.Student";
        Class<?> c1=Class.forName(studentClassStr);
        // System.out.println(c1);
        Object o=c1.newInstance();
        // System.out.println(o);

        Constructor<?> cs1=c1.getConstructor();
        // System.out.println(cs1);
        Object o1=cs1.newInstance();
        // System.out.println(o1);

        Constructor<?> cs2=c1.getConstructor(String.class,int.class);
        // System.out.println(cs2);
        Object o2=cs2.newInstance("张三",10);
        System.out.println(o2);

        // 获取成员变量，只能获取由public修饰的成员变量；
        // Field[] fields=c1.getFields();
        // for(Field field: fields){
        //     System.out.println(field);
        // }

        // Field nameField=c1.getField("name");
        // System.out.println(nameField);

        // Field[] allFields=c1.getDeclaredFields();
        // for(Field field: allFields){
        //     // 暴力访问
        //     field.setAccessible(true);
        //     System.out.println(field.get(o2));
        // }

        // Method[] methods=c1.getMethods();
        // methods=c1.getDeclaredMethods();
        // for(Method method: methods){
        //     System.out.println(method);
        // }

        Method setNameMethod=c1.getMethod("setName",String.class);
        setNameMethod.invoke(o2,"李四");

        Method setAgeMethod=c1.getMethod("setAge",int.class);
        setAgeMethod.invoke(o2,20);

        // Method showMethod=c1.getMethod("show",int.class);
        // System.out.println(showMethod);
        // showMethod.invoke(null,1001);

        // Method declaredShowMethod=c1.getDeclaredMethod("show");
        // declaredShowMethod.setAccessible(true);
        // declaredShowMethod.invoke(o2);
        //
        // System.out.println(o2);

        Class<?> c2=c1.getSuperclass();
        System.out.println(c2);
        Object p=c2.newInstance();

        Class<?>[] interfaces=c2.getInterfaces();

        for(Class<?> anInterface: interfaces){
            System.out.println(anInterface);
            Field[] fields=anInterface.getFields();
            for(Field field: fields){
                System.out.println(field);
            }

            // Method[] methods=anInterface.getMethods();
            // for(Method method: methods){
            //     System.out.println(method);
            // }
        }



        // Field[] fields=c2.getDeclaredFields();
        // for(Field field: fields){
        //     int modifiers=field.getModifiers();
        //     System.out.println(
        //         Modifier.isStatic(modifiers)+"\t"+modifiers+"\t"+field);
        // }

        // Field bytesField=c2.getField("bytes");
        //
        // Object arr=Array.newInstance(byte.class,1024);
        //
        // for(int x=0;x<1000;x++){
        //     Array.set(arr,x,(byte)x);
        // }
        //
        // bytesField.set(p,arr);
        //
        // Object o=bytesField.get(p);

        // for(int x=0;x<Array.getLength(o);x++){
        //     Object o1=Array.get(o,x);
        //     System.out.println(o1);
        // }
        System.out.println("---------------------------");

        // Array.newInstance默认创建的是一个一维数组，其中第二个
        // 参数表示该数组的长度；如果第二个参数表示为可变长参数列表，
        // 则该可变长参数列表中的参数表示每个维度的长度；
        // Object arr=Array.newInstance(int.class,2);
        // // System.out.println(Array.getLength(arr));
        // Array.set(arr,0,10);
        // Array.set(arr,1,20);
        // // Array.set(arr,2,30); // java.lang.ArrayIndexOutOfBoundsException
        // Object o=Array.get(arr,1);
        // System.out.println(o);

        // System.out.println("--------------------------------------");
        //
        // String aStr="com.briup.bigdata.bd1903.java.day12.A";
        //
        // Class<?> ac=Class.forName(aStr);
        //
        // Object acObj=ac.newInstance();
        //
        // System.out.println(ac);
        //
        // System.out.println(acObj);
        //
        // Method showMethod=ac.getMethod("show",Object.class);
        //
        // System.out.println(showMethod);
        //
        // Method show1Method=ac.getMethod("show1",Object.class);
        // System.out.println(show1Method.getName());


    }
}

interface B{
    int x=10;
    void showy();
}

class A<T>{
    public void show(T t){
        System.out.println(t);
    }

    public <Q> void show1(Q q){
        System.out.println(q);
    }
}


class Person implements B{
    public double height;
    protected String id;
    String gender;
    private double weight;
    public byte[] bytes;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public double getHeight(){
        return height;
    }

    public void setHeight(double height){
        this.height=height;
    }

    public double getWeight(){
        return weight;
    }

    public void setWeight(double weight){
        this.weight=weight;
    }

    public String getGender(){
        return gender;
    }

    public void setGender(String gender){
        this.gender=gender;
    }

    @Override
    public void showy(){

    }
}

class Student extends Person{
    private String name;
    private int age;

    public Student(){}

    public Student(String name,int age){
        this.name=name;
        this.age=age;
    }

    public static void show(int x){
        System.out.println(x);
    }

    private void show(){
        System.out.println("show 私有方法！");
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age=age;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName()+
               "@"+
               Integer.toHexString(this.hashCode())+
               "::"+
               this.name+"::"+this.age;
    }
}
