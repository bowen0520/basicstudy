package day11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Homework_t3 {
	public static void main(String[] args) {
		
		Set<Object> set = new HashSet<Object>();
		for(Object o:set) {
			System.out.println(o);
		}
		Iterator<Object> iterator = set.iterator();
		while(iterator.hasNext()) {
			Object x = iterator.next();
			System.out.println(x.toString()+" ");
		}
		set.forEach(System.out::println);
		//new Homework_t3().help2(123321);
		System.out.println(new Homework_t3().help2(1321));
	}
	//给一个不多于5位的正整数，要求：一、求它是几位数，二、逆序打印出各位数字
	public void help1(int num) {
		StringBuffer str = new StringBuffer(String.valueOf(num));
		str = str.reverse();
		System.out.println(str.length()+" "+str.toString());
	}
	//一个5位数，判断它是不是回文数。即12321是回文数，个位与万位相同，十位与千位相同。
	public boolean help2(long num) {
		StringBuffer str = new StringBuffer(String.valueOf(num));
		String strTemp = str.toString();
		str = str.reverse();
		return str.toString().equals(strTemp);
	}
	//有一个已经排好序的数组。现输入一个数，要求按原来的规律将它插入数组中。
	public void help3() {
		int[] arr = {1,2,3,4,5,7,8,9};
		int[] newArr = new int[arr.length+1];
	}
	
}
