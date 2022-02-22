package proxy_model.static_model;

/**
 * @program: BD1903.OOAD
 * @description: .
 * @author: Kevin
 * @create: 2019-08-22 09:20
 **/
public interface IStudentService{
    void insert(Student student);

    void delete(Student student);

    void update(Student student);

    Student select(String name);
}
