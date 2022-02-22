package day15;

enum Lock1{
    A
}

/**
 * @program: BD1903.JavaSE
 * @description: .线程演示4
 * @author: Kevin
 * @create: 2019-08-12 15:20
 **/
public class ThreadTest4{
    public static void main(String[] args){
        Ticket t=new Ticket();
        SaleRun sr=new SaleRun(t);

        new Thread(sr,"线程1").start();
        new Thread(sr,"线程2").start();
        new Thread(sr,"线程3").start();
        new Thread(sr,"线程4").start();





        // new SaleThread("线程1",t).start();
        // new SaleThread("线程2",t).start();
        // new SaleThread("线程3",t).start();
        // new SaleThread("线程4",t).start();
    }
}

class SaleRun implements Runnable{
    private Ticket t;

    public SaleRun(Ticket t){
        this.t=t;
    }

    @Override
    public /*synchronized*/ void run(){
        while(true){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            synchronized(this){
                if(t.getNum()>0) t.sale();
            }
        }
    }
}

class SaleThread extends Thread{
    private /*static*/ Ticket t;

    public SaleThread(String name,Ticket t){
        super(name);
        this.t=t;
    }

    @Override
    public synchronized void run(){  // 锁对象：this
        // Ticket t=new Ticket();
        while(true){
            try{
                sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            // synchronized(this){   //  出错原因：错对象不是同一个
            // synchronized(Lock1.A){   //
            if(t.getNum()>0) t.sale();
            // }
        }
    }
}

class Ticket{
    private int num;

    public Ticket(){
        this.num=10;
    }

    public synchronized void sale(){  // 同步方法隐式锁对象是this
        // synchronized(this){
        String name=Thread.currentThread().getName();
        System.out.println(name+":::卖出了："+(num--)+"号票！！！");
        // }
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num=num;
    }
}



