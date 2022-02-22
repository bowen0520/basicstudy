package day12;

import java.util.*;

public class TestQueue {
    public static void main(String[] args) {
        /*MyQueue<Integer> que = new MyQueue<>();
        que.put(1);
        que.put(2);
        que.put(7);
        System.out.println(que.getSize());
        System.out.println(que.isEmpty());
        System.out.println(que.get());
        System.out.println(que.get());
        System.out.println(que.getSize());
        System.out.println(que.isEmpty());
        System.out.println(que.get());
        System.out.println(que.getSize());
        System.out.println(que.isEmpty());
        System.out.println(que.get());
        System.out.println(que.getSize());
        System.out.println(que.isEmpty());
        List<Fruits> list = new ArrayList<>();
        Fruits f1 = new Fruits("apple");
        Fruits f2 = new Fruits("grape");
        Fruits f3 = new Fruits("banana");
        Fruits f4 = new Fruits("pear");
        list.add(f1);
        list.add(f2);
        list.add(f3);
        list.add(f4);
        Collections.sort(list);
        list.forEach(System.out::println);*/


        int i = 10;
        assert i==11:"此处x不为11";
        System.out.println("true");

    }



    public <T> void help1(List<T> list1,List<T> list2){
        Set<T> set = new HashSet<>();
        list1.forEach((T o)->set.add(o));
        StringBuffer str = new StringBuffer();
        list2.forEach((T o)->{
            if(set.contains(o)){
                str.append(o+" ");
            }
        });
        if(str.length()==0){
            System.out.println(false);
        }else{
            System.out.println(str.toString());
        }
    }
    public <T> void help2(List<T> list,T t){
        list.forEach((T o)->{
            if(o.equals(t)){
                list.remove(o);
            }
        });
    }

    public Map<String,List<Integer>> help3(){
        Map<String,List<Integer>> map = new HashMap<>();
        Scanner s = new Scanner(System.in);
        for(int i = 0;i<1000;i++){
            System.out.println("请输入用户名");
            String name = s.next();

        }
        return map;
    }
}

class MyQueue<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private T t = null;
    public MyQueue() {
        this.head = new Node(t);
        this.tail = new Node(t);
        head.setNext(tail);
        tail.setLast(head);
        this.size = 0;
    }
    public MyQueue(Node node) {

    }

    public MyQueue(MyQueue que){

    }

    public void put(T t){
        Node node = new Node(t);
        node.setLast(tail.getLast());
        node.getLast().setNext(node);
        node.setNext(tail);
        tail.setLast(node);
        size++;
    }
    public T get(){
        if(size<=0){
            return null;
        }
        T t = head.getNext().getT();
        head.setNext(head.getNext().getNext());
        head.getNext().setLast(head);
        size--;
        return t;
    }
    public int getSize(){
        return size;
    }
    public boolean isEmpty(){
        return size<=0;
    }
}
class Node<T>{
    private T t;
    private Node<T> next;
    private Node<T> last;

    public Node(T t) {
        this.t = t;
        this.next = null;
        this.last = null;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getLast() {
        return last;
    }

    public void setLast(Node<T> last) {
        this.last = last;
    }
}

class Fruits implements Comparable{
    private String name;

    public Fruits(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        if(o==null){
            return -1;
        }
        Fruits f = (Fruits)o;
        String oName = f.getName();
        int min = this.name.length()>oName.length()?oName.length():this.name.length();
        for(int i = 0;i<min;i++){
            if(this.name.charAt(i)!=oName.charAt(i)){
                return this.name.charAt(i)-oName.charAt(i);
            }
        }
        return this.name.length()-oName.length();
    }

    @Override
    public String toString() {
        return "Fruits{" +
                "name='" + name + '\'' +
                '}';
    }
}


class TelephoneDirectory{
    private List<String> list;

    public TelephoneDirectory() {
        list = new ArrayList<>();
    }

    public boolean add(String tele){
        boolean flag = list.contains(tele);
        if(!flag){
            list.add(tele);
        }
        return flag;
    }

    public boolean delete(String tele){
        return list.remove(tele);
    }

    public boolean change(String oldTele,String newTele){
        int i = list.indexOf(oldTele);
        if(i>=0){
            list.set(i,newTele);
            return true;
        }else{
            return false;
        }
    }

    public void get(){
        list.forEach(System.out::println);
    }
}
