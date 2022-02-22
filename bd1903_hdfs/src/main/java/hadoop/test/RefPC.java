package hadoop.test;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.test
 * @filename: RefPC.java
 * @create: 2020/07/07 18:23
 * @author: 29314
 * @description: .测试父类引用子类对象时，调用子父类都有的方法，是调用哪个
 **/

class A{
    public void test(){
        System.out.println("A");
    }
}
class B extends A{
    @Override
    public void test() {
        System.out.println("B");
    }
}

public class RefPC {
    public static void main(String[] args) {
        A a = new B();
        a.test();
    }
}
