package proxy_model.static_model;

/**
 * @program: BD1903.OOAD
 * @description: .
 * @author: Kevin
 * @create: 2019-08-22 09:36
 **/
public class StudentServiceTest{
    public static void main(String[] args){
        IStudentService iss=new StudentServiceImpl();

        StudentServiceProxy ssp=new StudentServiceProxy(iss);

        // iss.delete(new Student("张三",20));

        ssp.delete(new Student("张三",20));
    }
}
