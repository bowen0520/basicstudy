package day11;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.DoubleToIntFunction;

public class Question1 {
    public static void main(String[] args) {
        //int[] arr = {1,2,5,7,8};
        int a = 0;

        int i = 2000000000;
        System.out.println(i);
        int[] arr = {9,7,3,1};
        System.out.println(Arrays.toString(new Question1().help1(arr,0)));
    }
    /*
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while(s.hasNext()) {
            int X = s.nextInt();
            int Y = s.nextInt();
            int S = s.nextInt();
            int T = Math.abs(X)+Math.abs(Y);
            if (S < (X + Y)) {
                System.out.println(false);
            } else {
                System.out.println(true);
            }
        }
    }
     */

    public int[] help1(int[] arr,int num){
        int n = arr[arr.length-1]-arr[0];
        int[] newArr = new int[arr.length+1];
        int index = 0;
        boolean flag = false;
            for(int i = 0;i<newArr.length;i++){
                boolean temp = index>=arr.length||(n>0?num<arr[index]:num>arr[index]);
                if((!flag)&&temp){
                    newArr[i] = num;
                    flag = true;
                }else{
                    newArr[i] = arr[index];
                    index++;
                }
            }
        return newArr;
    }
}
