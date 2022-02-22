package testjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testDriverManager {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //驱动版本8.0.x，驱动类的类全名
        //com.mysql.cj.jdbc.Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        //获取链接
        String url="jdbc:mysql://192.168.112.130:8017/db_test?characterEncoding=utf-8&useSSL=true&serverTimezone=GMT";
        String user="root";
        String passwd="root";
        Connection conn = DriverManager.getConnection(url,user,passwd);

        //获取Statement
        Statement statement = conn.createStatement();
        String slct = "select last_name,salary from s_emp";
        //获取PreparedStatement对象，需要提供预处理的SQL语句
        String ist = "insert s_emp(id,last_name,salary) value(?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(ist);
        //给占位符赋值
        preparedStatement.setInt(1,300);
        preparedStatement.setString(2,"zhangsan");
        preparedStatement.setDouble(3,21312.0);

        //执行sql语句
        //statement.executeQuery() ResultSet,用于执行DQL
        //statement.executeUpdate() int,执行DML
        //statement.execute() boolean,执行DDL，该方法的返回值指的是SQl语句是否执行失败
        ResultSet resultSet = statement.executeQuery(slct);
        int i = preparedStatement.executeUpdate();
        System.out.println(i);

        //处理结果集
        while(resultSet.next()){
            //如果字段有别名，需要使用别名来获取
            String name = resultSet.getString("last_name");
            double aDouble = resultSet.getDouble(2);
            System.out.println(name + ":" + aDouble);
        }

        //关闭连接
        resultSet.close();
        preparedStatement.close();
        statement.close();
        conn.close();
    }

    public void test(){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            //驱动版本8.0.x，驱动类的类全名
            //com.mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //获取链接
            String url = "jdbc:mysql://192.168.112.130:8017/db_test?characterEncoding=utf-8&useSSL=true&serverTimezone=GMT";
            String user = "root";
            String passwd = "root";
            conn = DriverManager.getConnection(url, user, passwd);
            //关闭自动提交，必须手动提交
            conn.setAutoCommit(false);

            //获取Statement
            //获取PreparedStatement对象，需要提供预处理的SQL语句
            String ist = "insert s_emp(id,last_name,salary) value(?,?,?)";
            preparedStatement = conn.prepareStatement(ist);
            //给占位符赋值
            preparedStatement.setInt(1, 300);
            preparedStatement.setString(2, "zhangsan");
            preparedStatement.setDouble(3, 21312.0);

            //执行sql语句
            //statement.executeQuery() ResultSet,用于执行DQL
            //statement.executeUpdate() int,执行DML
            //statement.execute() boolean,执行DDL，该方法的返回值指的是SQl语句是否执行失败
            int i = preparedStatement.executeUpdate();
            int a = 1/0;

            //提交事物
            conn.commit();
            System.out.println(i);
        }catch (Exception e){
            //捕获异常，将事务回滚
            try {
                //事物回滚
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            //关闭连接
            if(preparedStatement!=null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
