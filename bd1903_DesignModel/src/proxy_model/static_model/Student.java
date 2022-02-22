package proxy_model.static_model;

/**
 * @program: BD1903.OOAD
 * @description: .
 * @author: Kevin
 * @create: 2019-08-22 09:18
 **/
public class Student{
    private String name;
    private int age;

    public Student(String name,int age){
        this.name=name;
        this.age=age;
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
        return "Student{"+"name='"+name+'\''+", age="+age+'}';
    }
}
