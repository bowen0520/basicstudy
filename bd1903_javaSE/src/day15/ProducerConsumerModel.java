package day15;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
需求：定义一个容器，存放book对象，生产一个book就卖一本，如果有书就卖，不生产，如果没书就生产不卖
 */
public class ProducerConsumerModel {
    static int id = 0;
    public static void main(String[] args) {
        Factory<Car> factory = new Factory<>(10);
        new Thread(new Producer<Car>(factory) {
            @Override
            public Car getT() {
                return new Car("宝马",id++);
            }
        }).start();
        new Thread(new Producer<Car>(factory) {
            @Override
            public Car getT() {
                return new Car("宝马",id++);
            }
        }).start();
        new Thread(new Consumption<>(factory)).start();
        new Thread(new Consumption<>(factory)).start();
    }
}

class Car{
    private String name;
    private int id;

    public Car(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name + id;
    }
}

class Factory<T>{
    private List<T> list;
    private int size;
    public Factory(int size) {
        this.size = size;
        this.list = new ArrayList<>(this.size);
    }

    public void produce(T t) throws InterruptedException {
        synchronized (this) {
            while (list.size()>=this.size) {
                wait();
            }
            list.add(t);
            System.out.println(Thread.currentThread().getName()+"生产了："+t.toString());
            notifyAll();
        }
    }
    public void consumption() throws InterruptedException {
        synchronized (this) {
            while (list.size()<=0) {
                wait();
            }
            System.out.println(Thread.currentThread().getName()+"消费了："+list.remove(list.size()-1).toString());
            notifyAll();
        }
    }
}
interface Get<T>{
    public T getT();
}

abstract class Producer<T> implements Runnable,Get{
    private Factory<T> factory;
    public Producer(Factory<T> factory) {
        this.factory = factory;
    }
    abstract public T getT();

    @Override
    public void run() {
        while(true){
            try {
                factory.produce(getT());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumption<T> implements Runnable{
    private Factory<T> factory;
    public Consumption(Factory<T> factory) {
        this.factory = factory;
    }
    @Override
    public void run() {
        while(true){
            try {
                factory.consumption();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}