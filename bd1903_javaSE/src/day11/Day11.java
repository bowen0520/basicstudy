package day11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day11 {
	public static void main(String[] args) {
		Student2 s1 = new Student2("liusan", 20, 90.0f);
		Student2 s2 = new Student2("lisi", 22, 90.0f);
		Student2 s3 = new Student2("wangwu", 20, 99.0f);
		Student2 s4 = new Student2("sunliu", 22, 100.0f);
		List<Student2> list = new ArrayList<Student2>();
		list.add(s1);
		list.add(s2);
		list.add(s3);
		list.add(s4);
		Collections.sort(list,new StudentComparable());
		for(Student2 s:list) {
			System.out.println(s.toString());
		}
		
		
		//new Day11().help3();
	}
	public void help1(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i<10;i++) {
			int n = (int)(Math.random()*100);
			if(n>=10) {
				list.add(n);
			}
		}
		list.forEach(System.out::println);
	}
	
	public int listTest(ArrayList<Integer> al, Integer s) {
		return al.indexOf(s);
	}
	
	public void help2() {
		String[] strs = {"12345","67891","12347809933","98765432102","67891","12347809933"};
		List<String> list = new LinkedList<String>();
		Set<String> set = new HashSet<String>();
		for(String s:strs) {
			if(set.add(s)) {
				list.add(s);
			}
		}
		Iterator<String> l = list.iterator();
		while(l.hasNext()) {
			System.out.println(l.next());
		}
		
		list.forEach(System.out::println);
		for(String s:list) {
			System.out.println(s);
		}
	}
	
	public void help3() {
		int i = 0;
		Set<Integer> red = new HashSet<Integer>();
		while(i<6) {
			int num = (int)(Math.random()*32+1);
			if(red.add(num)) {
				i++;
			}
		}
		System.out.print("红：");
		for(int n:red) {
			System.out.print(n+",");
		}
		System.out.println();
		int numb = (int)(Math.random()*15+1);
		System.out.println("蓝："+numb);
	}
}


class Student1 implements Comparable<Student1>{
	private String name;
	private int age;
	private float grade;
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public Student1(String name, int age, float grade) {
		super();
		this.name = name;
		this.age = age;
		this.grade = grade;
	}
	@Override
	public int compareTo(Student1 o) {
		// TODO Auto-generated method stub
		if(o==null) {
			return 1;
		}
		return this.grade!=o.getGrade()?(int)(o.getGrade()-this.grade):this.age-o.getAge();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" "+grade+" "+age;
	}
	
	
	
}
class Student2{
	private String name;
	private int age;
	private float grade;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public Student2(String name, int age, float grade) {
		this.name = name;
		this.age = age;
		this.grade = grade;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" "+grade+" "+age;
	}
}
class StudentComparable implements Comparator<Student2>{

	@Override
	public int compare(Student2 o1, Student2 o2) {
		
		return o1.getGrade()!=o2.getGrade()?(int)(o2.getGrade()-o1.getGrade()):o1.getAge()-o2.getAge();
	}
	
}
