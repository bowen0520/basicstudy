package day13;

public class UseException{
    public static final float pi = 3.14f;
    public static void main(String[] args) throws MyException {
        double[] arr = {1,-2,3};
        System.out.println(new UseException().help2(arr));
    }
    public void help1(int r) throws MyException {
        if(r>0){
            System.out.println(pi*r*r);
        }else{
            throw new MyException("半经错误异常");
        }
    }
    public double help2(double[] nums) throws MyException {
        int l = nums.length;
        if(l == 0){
            throw new MyException("数组为空异常");
        }
        double sum = 0;
        for(int i = 0;i<l;i++){
            if(nums[i]<0){
                throw new MyException("参数为负异常");
            }else{
                sum+=nums[i];
            }
        }
        return sum/l;
    }
}



class MyException extends Exception{
    private String message;

    public MyException(String message) {
        super(message);
    }
}
