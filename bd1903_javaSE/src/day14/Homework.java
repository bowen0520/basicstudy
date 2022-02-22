package day14;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;

public class Homework {
    /*public static void main(String[] args) {
        Apple apple = new Apple(0);
        new Thread(new Consumer(apple),"消费者1").start();
        new Thread(new Consumer(apple),"消费者2").start();
        new Thread(new Producer(apple),"生产者1").start();
        new Thread(new Producer(apple),"生产者2").start();
    }*/

    /*static double d = 0.0;
    static double b = 0.0;
    public static void main(String[] args) {

        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized ("suo") {
                        d = Homework.add(d,1.0);
                        System.out.println(d);
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized ("suo") {
                        d = Homework.add(d,0.1);
                        System.out.println(d);
                    }
                }
            }
        }.start();
    }
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }*/

    /*static int n = 0;
    static boolean flag1 = true;
    static boolean flag2 = false;
    static boolean flag3 = false;
    static boolean flag4 = false;
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                        synchronized ("suo") {
                            if(flag1) {
                                flag1 = false;
                                flag2 = true;
                                System.out.println(n++);
                                "suo".notifyAll();
                            }else{
                                try {
                                    "suo".wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                        synchronized ("suo") {
                            if(flag2) {
                                flag2 = false;
                                flag3 = true;
                                System.out.println(n++);
                                "suo".notifyAll();
                            }else{
                                try {
                                    "suo".wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                        synchronized ("suo") {
                            if(flag3) {
                                flag3 = false;
                                flag4 = true;
                                System.out.println(n--);
                                "suo".notifyAll();
                            }else{
                                try {
                                    "suo".wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                        synchronized ("suo") {
                            if(flag4) {
                                flag1 = true;
                                flag4 = false;
                                System.out.println(n--);
                                "suo".notifyAll();
                            }else {
                                try {
                                    "suo".wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                }
            }
        }).start();
    }*/

    /*static int n = 0;
    static volatile boolean flag1 = true;
    static volatile boolean flag2 = false;
    static volatile boolean flag3 = false;
    static volatile boolean flag4 = false;
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(flag1) {
                        System.out.println(n++);
                        flag1 = false;
                        flag2 = true;
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(flag2) {
                        System.out.println(n++);
                        flag2 = false;
                        flag3 = true;
                    }
                }
            }
        }).start();
        new Thread(() -> {
            while(true) {
                if(flag3) {
                    System.out.println(n--);
                    flag3 = false;
                    flag4 = true;
                }
            }
        }).start();
        new Thread(() -> {
            while(true) {
                if(flag4) {
                    System.out.println(n--);
                    flag4 = false;
                    flag1 = true;
                }
            }
        }).start();
    }
*/
    static int n = 0;
    public static void main(String[] args) {

        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);
        Semaphore s3 = new Semaphore(0);
        Semaphore s4 = new Semaphore(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        s1.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(n++);
                    s2.release();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        s2.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(n++);
                    s3.release();
                }
            }
        }).start();
        new Thread(() -> {
            while(true) {
                try {
                    s3.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(n--);
                s4.release();
            }
        }).start();
        new Thread(() -> {
            while(true) {
                try {
                    s4.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(n--);
                s1.release();
            }
        }).start();
    }
}
class Apple{
    int num;

    public Apple(int num) {
        this.num = num;
    }
}
class Producer implements Runnable{
    Apple apple;

    public Producer(Apple apple) {
        this.apple = apple;
    }

    @Override
    public void run() {
        while(true){
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            synchronized ("apple") {
                if (apple.num < 10) {
                    int add = 10 - apple.num;
                    apple.num += add;
                    System.out.println("原来有" + (10 - add) + "个,"+Thread.currentThread().getName()+"生产了" + add + "个,现有" + apple.num + "个");
                }
            }
        }
    }
}
class Consumer implements Runnable{
    Apple apple;

    public Consumer(Apple apple) {
        this.apple = apple;
    }

    @Override
    public void run() {
        while(true){
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            synchronized ("apple") {
                if (apple.num > 0) {
                    apple.num--;
                    System.out.println(Thread.currentThread().getName()+"卖出一个，还剩" + apple.num + "个");
                }
            }
        }
    }

}
