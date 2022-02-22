package day15;

import java.util.*;

public class Homework {
    /*static boolean flag = false;
    public static void main(String[] args) {
        Random random = new Random();

        Thread t = new Thread(new Runnable() {
            List<Integer> list = new ArrayList<>();
            @Override
            public synchronized void run() {
                while(true){
                    if(list.size()<100){
                        try {
                            Thread.sleep(random.nextInt(200));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int num = random.nextInt(101);
                        list.add(num);
                        System.out.print(num+" ");
                    }else{
                        System.out.println();
                        try {
                            flag = true;
                            wait(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.forEach((o)->System.out.print(o+" "));
                        break;
                    }
                }
            }
        });
        t.start();
        synchronized (t){
            while(true){
                t.notifyAll();
                if(flag){
                    break;
                }
            }
        }
    }*/

    /*private static int count = 10;
    public static void main(String[] args) {
        Print p = new Print(count);
        new Thread(p).start();
        new Thread(p).start();
        new Thread(p).start();
    }*/
    public static void main(String[] args) {
        int length = 30;
        boolean[] b = new boolean[1];
        b[0]=true;
        new Thread(new Rabbit(length,b)).start();
        new Thread(new Tortoise(length,b)).start();
    }

}
class Rabbit implements Runnable{
    private int length;
    private int runL;
    private boolean[] flag;

    public Rabbit(int length,boolean[] flag) {
        this.length = length;
        this.runL = 0;
        this.flag = flag;
    }

    @Override
    public void run() {
        while(flag[0]){
            runL+=1;
            synchronized ("suo") {
                if (runL == length) {
                    flag[0] = false;
                    System.out.println("兔子跑赢了");
                    break;
                }
            }
            System.out.println("兔子跑了"+runL+"米");
            try {
                if(runL%10==0){
                    Thread.sleep(2200);
                }else {
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Tortoise implements Runnable{
    private int length;
    private int runL;
    private boolean[] flag;

    public Tortoise(int length,boolean[] flag) {
        this.length = length;
        this.runL = 0;
        this.flag = flag;
    }

    @Override
    public void run() {
        while(flag[0]){
            runL+=1;
            synchronized ("suo") {
                if (runL == length) {
                    flag[0] = false;
                    System.out.println("乌龟跑赢了");
                    break;
                }
            }
            System.out.println("乌龟跑了"+runL+"米");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Print implements Runnable{
    private int count;

    public Print(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        while(true){
            synchronized ("lock") {
                if (count == 0) {
                    break;
                }else{
                    System.out.println(Thread.currentThread().getName()+"::ABC::"+count);
                    count--;
                }
            }
        }
    }
}

class MyUtil{
    private static MyUtil util;

    private MyUtil(){

    }

    public static MyUtil getMyUtil(){
        if(util==null){
            synchronized ("lock"){
                if(util==null){
                    util = new MyUtil();
                }
            }
        }
        return util;
    }
}
