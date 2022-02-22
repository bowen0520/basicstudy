package day15;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @program: BD1903.JavaSE
 * @description: .生产者消费者演示
 * @author: Kevin
 * @create: 2019-08-12 16:23
 **/
public class ThreadTest5{
    public static void main(String[] args){
        Publisher p=new Publisher(new ArrayList<>());

        Thread t1=new Thread(new Producer1(p),"生产者1：");
        Thread t3=new Thread(new Producer1(p),"生产者2：");

        Thread t2=new Thread(new Consumer(p),"消费者1：");
        Thread t4=new Thread(new Consumer(p),"消费者2：");

        t1.start();
        t3.start();

        t2.start();
        t4.start();
    }
}

class Producer1 implements Runnable{
    private Publisher p;

    public Producer1(Publisher p){
        this.p=p;
    }

    @Override
    public void run(){
        while(true){
            try{
                Thread.sleep(1000);
                this.p.produce();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{
    private Publisher p;

    public Consumer(Publisher p){
        this.p=p;
    }

    @Override
    public void run(){
        while(true){
            try{
                Thread.sleep(1000);
                this.p.consume();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Publisher{
    private static final String[] BOOK_NAMES=
        {"Java","C++","Scala","C#","JavaScript"};

    private List<Book> list;


    public Publisher(List<Book> list){
        this.list=list;
    }

    public synchronized void produce() throws InterruptedException{
        // System.out.println(Thread.currentThread().getName()+System.currentTimeMillis());
        // 有书，容器的大小不等于0，就等待，不生产
        if(this.list.size()!=0){
            this.wait();
        }else{
            Random random=new Random();
            int anInt=random.nextInt(BOOK_NAMES.length);
            String bookName=BOOK_NAMES[anInt];
            Book book=new Book(bookName);
            this.list.add(book);
            System.out.println(
                Thread.currentThread().getName()+
                ":::生产了:::"+book);

            // ------------------唤醒---------------------
            // this.notify();
            this.notifyAll();
        }
    }

    public synchronized void consume() throws InterruptedException{
        // System.out.println(Thread.currentThread().getName()+System.currentTimeMillis());
        if(this.list.size()==0){
            this.wait();
        }else{
            Book book=this.list.get(0);
            this.list.remove(0);
            System.out.println(
                Thread.currentThread().getName()+
                "-------消费了------"+book);

            // -------------唤醒生产者生产-------------
            // this.notify();
            this.notifyAll();
        }

    }

    public List<Book> getList(){
        return list;
    }

    public void setList(List<Book> list){
        this.list=list;
    }
}


class Book{
    private String name;

    public Book(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return "Book{"+"name='"+name+'\''+'}';
    }
}
