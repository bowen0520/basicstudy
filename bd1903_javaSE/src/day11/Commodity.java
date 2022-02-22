package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Commodity {
	private String name;
	private double weight;
	private double price;
	private int num;
	private String[] factories;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String[] getFactories() {
		return factories;
	}
	public void setFactories(String[] factories) {
		this.factories = factories;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.name.hashCode()+Double.valueOf(this.weight).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Commodity o = (Commodity)obj;
		return this.name.equals(o.getName())&&this.weight==o.getWeight();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "商品名称:"+name+" 重量:"+weight+" 价格:"+price+" 配件数量:"+num+" 配件制造厂商:"+Arrays.toString(factories);
	}
	
	
	
}

class Util1 {
	// 懒汉模式
	private static final Util1 u = new Util1();

	private Util1() {
	}

	public static Util1 getUtil() {
		return u;
	}
}

class util{
	/*
	 * public static void main(String[] args) { List<? extends Student> list = new
	 * ArrayList<Student>(); List<Student> list1 = new ArrayList<>(); list1.add(new
	 * Student()); list= list1; }
	 */
	
    void show(List<? extends Student> list){
    	list.forEach(System.out::println);
    }
    void show1(List<? super Student> list){
        list.forEach(System.out::println);
    }
}
class Person{}
class Student extends Person{}
class CollegeStudent extends Student{}
