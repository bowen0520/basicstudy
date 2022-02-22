package proxy_model.static_model;

/**
 * @program: BD1903.OOAD
 * @description: .代理类
 * @author: Kevin
 * @create: 2019-08-22 09:30
 **/
public class StudentServiceProxy implements IStudentService{
    private IStudentService iss;

    public StudentServiceProxy(IStudentService ssi){
        this.iss=ssi;
    }

    @Override
    public void insert(Student student){
        StudentLogger.log("添加学生信息："+student);
        this.iss.insert(student);
        StudentLogger.log("已经添加学生信息："+student);

    }

    @Override
    public void delete(Student student){
        StudentLogger.log("删除学生信息："+student);
        this.iss.delete(student);
        StudentLogger.log("已经删除学生信息："+student);
    }

    @Override
    public void update(Student student){
        StudentLogger.log("更新学生信息："+student);
        this.iss.update(student);
        StudentLogger.log("已经更新学生信息："+student);
    }

    @Override
    public Student select(String name){
        StudentLogger.log("根据："+name+"查询学生");
        Student student=this.iss.select(name);
        StudentLogger.log("已经查到学生信息："+student);
        return student;
    }
}
