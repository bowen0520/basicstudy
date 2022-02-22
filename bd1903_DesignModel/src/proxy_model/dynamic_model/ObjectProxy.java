package proxy_model.dynamic_model;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ObjectProxy{
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException{
        String className="com.briup.bigdata.bd1903.ooad.proxy_model.dynamic_model.StudentServiceImpl";

        Class<?> sc=Class.forName(className);

        // 如果没有无参构造器，就需要获取全部的构造器
        // 如果有无参构造器，则直接创建
        Object target=sc.newInstance();

        System.out.println(target);

        ClassLoader cl=sc.getClassLoader();

        Class<?>[] interfaces=sc.getInterfaces();

        //
        Object proxyInstance=Proxy.newProxyInstance(cl,interfaces,(proxy,method,args1)->{
            // System.out.println(proxy);
            // StudentLogger.log("代理对象是："+proxy);
            StudentLogger.log("调用了："+method.getName()+"方法！！！");
            StudentLogger.log("参数是："+Arrays.toString(args1));

            // return proxy;
            return method.invoke(target,args1);
        });

        System.out.println(proxyInstance);
    }
}
