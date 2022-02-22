package proxy_model.dynamic_model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StudentServiceProxy{
    public static void main(String[] args){
        // 目标对象
        IStudentService iss=new StudentServiceImpl();

        Class<? extends IStudentService> issClass=iss.getClass();

        // 目标类的类加载器
        ClassLoader cl=issClass.getClassLoader();

        // 目标类的所有接口
        Class<?>[] interfaces=issClass.getInterfaces();

        Object o=Proxy.newProxyInstance(cl,interfaces,new MyHandler(iss));
        System.out.println(o);

        // 获取代理类对象
        IStudentService proxyObject=
            (IStudentService)Proxy.newProxyInstance(
                cl,interfaces,new MyHandler(iss));

        System.out.println(proxyObject);

        proxyObject.delete(new Student("张三",20));

        Student student=proxyObject.select("张三");

        System.out.println(student);
    }
}

class MyHandler implements InvocationHandler{
    private IStudentService iss; // 目标

    public MyHandler(IStudentService iss){
        this.iss=iss;
    }

    @Override
    public Object invoke(Object proxy,Method method,Object[] args) throws Throwable{
        String methodName=method.getName();

        StudentLogger.log("正在调用："+methodName+"方法！");

        return method.invoke(iss,args);
    }
}
