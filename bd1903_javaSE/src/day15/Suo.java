package day15;

public class Suo {
    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();
        String s = "ssads";
        Object o  = s;
    }
}

class MyThread extends Thread{
    @Override
    public void run() {
        while(true) {
            Util u = Util.getUtil();
            System.out.println(Thread.currentThread().getName()+" "+u);
        }
    }
}
interface N{
    final int a = 0;
}
class Util{
    private static Util u;

    private Util(){}

    public static Util getUtil(){
        if(u==null){
            synchronized ("suo") {
                if(u==null) {
                    u = new Util();
                }
            }
        }
        return u;
    }
}