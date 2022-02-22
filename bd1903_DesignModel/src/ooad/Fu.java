package ooad;

/**
 * @program: BD1903.OOAD
 * @description: .
 * @author: Kevin
 * @create: 2019-08-22 11:39
 **/
public class Fu{
    public Fu(){
        this.show();
        //
        System.out.println("父类的构造器。。。this："+this);
        //
    }

    public void show(){
        System.out.println("这是父类中的show方法");
    }
}

class Zi {
    private Fu f;

    public Zi(Fu f){
        this.f=f;
    }

    public Zi(){
        this.show();
        System.out.println("，，，，子类的构造器。。。this："+this);
    }

    public void show(){
        System.out.println("这是子类中的show方法。。。。。。");
    }

    public static void main(String[] args){
        Zi zi=new Zi();
        zi.show();

        System.out.println("-------------------------");

        Fu fu=new Fu();
        fu.show();

        System.out.println("-------------------------");

        // Fu fz=new Zi();
        // fz.show();
    }
}
