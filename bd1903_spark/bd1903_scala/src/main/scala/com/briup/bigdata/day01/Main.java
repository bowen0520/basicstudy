package com.briup.bigdata.day01;

/**
 * @program: bd1903_spark
 * @package: com.briup.bigdata.day01
 * @filename: Main.java
 * @create: 2019/11/16 17:59
 * @author: 29314
 * @description: .
 **/

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int[] arr = new int[n];
        for(int i = 0;i< n;i++){
            arr[i] = s.nextInt();
        }
        int[] minarr = new int[n];
        int[] maxarr = new int[n];
        int j = 1;
        minarr[0] = arr[0];
        for(;j<n;j++){
            minarr[j] = Math.min(arr[j],minarr[j-1]);
        }
        j = n-2;
        maxarr[n-1] = arr[n-1];
        for(;j>=0;j--){
            maxarr[j] = Math.max(arr[j],maxarr[j+1]);
        }
        for(int k = 1;k<n-1;k++){
            if(arr[k]>minarr[k-1]&&arr[k]<maxarr[k+1]){
                System.out.println("true");
            }
        }
        System.out.println("false");
    }
}
