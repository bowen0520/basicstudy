package day8;

public class HomeWork {
    public static void main(String[] args) {
        B b = new B();
        System.out.println(b.a);
        b.b();

    }
}
class A{
    /*private*/ int a;
    /*private*/ void b(){}
}
class B extends A{

}