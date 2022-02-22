package proxy_model.dynamic_model;

public interface IStudentService{
    void insert(Student student);

    void delete(Student student);

    void update(Student student);

    Student select(String name);
}
