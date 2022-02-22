package day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Homework {
    public static void main(String[] args) {
        /*Homework homework = new Homework();
        int[] nums = {1,2,3,4,5};
        int[] b = {1,2,6,7};
        //System.out.println(Arrays.toString(homework.help1(nums)));
        int[][] an = homework.help2(nums,b);
        List<int[]> list = new ArrayList<>();
        for(int[] n:an){
            list.add(n);
        }
        list.stream()
                .filter((o)->(o[0]+o[1])%3==0)
                .sorted(Comparator.comparingInt(o -> o[0]))
                .forEach((n)->System.out.println(n[0]+":"+n[1]));*/
    }

    public int[] help1(int[] nums){
        Function<Integer,Integer> function = o-> (int)Math.pow(o,2);
        for(int i = 0;i<nums.length;i++){
            nums[i] = function.apply(nums[i]);
        }
        return nums;
    }
    public int[][] help2(int[] a,int[] b){
        MyFunction<int[],int[][]> myFunction = (o1,o2)->{
            int[][] an = new int[o1.length*o2.length][2];
            int index = 0;
            for(int i:o1){
                for(int j:o2){
                    an[index][0] = i;
                    an[index][1] = j;
                    index++;
                }
            }
            return an;
        };
        return myFunction.mix(a,b);
    }

    public void toStr(int[][] nums){
        for(int[] n:nums){
            System.out.println(n[0]+":"+n[1]);
        }
    }
}
