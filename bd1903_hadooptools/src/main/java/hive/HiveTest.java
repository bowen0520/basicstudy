package hive;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bd1903.hive
 * @package: com.briup.bigdata.bd1903.hive
 * @filename: HiveTest.java
 * @create: 2019.11.05 16:22
 * @author: when
 * @description: .CRUD，Delete和Update需要表支持事务
 **/
public class HiveTest{
    private Statement stmt;

    public HiveTest() throws ClassNotFoundException, SQLException{
        String driver="org.apache.hive.jdbc.HiveDriver";
        String url="jdbc:hive2://192.168.112.120:10000";
        String user="when";
        String password="";

        Class.forName(driver);
        Connection connection=DriverManager.getConnection(url,user,password);
        this.stmt=connection.createStatement();
    }

    // 创建数据库
    public void createDatabase(String dbName) throws SQLException{
        String sql="create database "+dbName;
        boolean success=this.stmt.execute(sql);
        System.out.println(success);
    }

    // 创建数据库表
    // 数据库名、表名、字段-类型、
    // 字段终结符【\t】、集合项终结符【,】、Map键值分隔符【:】
    // create table bd1903.tbl1(id int,name string);
    public void createTable(
        String dbName,          // 数据库名
        String tblName,         // 表名
        List<String> fields,    // 字段名:数据类型 的集合
        String fieldsTerminate, // 字段分隔符
        String collItemsTerminate,  // 集合数据类型的元素分隔符
        String mapKeysTerminate     // Map集合的Key和Value的分隔符
    ) throws Exception{
        if(fields.size()<1) throw new Exception("至少一共一个字段及其类型！");

        StringBuilder sb=new StringBuilder();
        sb.append("create table ")
          .append(dbName).append(".").append(tblName).append("(");

        fields.forEach(str->{
            String[] strs=str.split(":");
            sb.append(strs[0]).append(" ").append(strs[1]).append(",");
        });

        sb.setLength(sb.length()-1); // 取出拼接的sql语句的字段的最后一个逗号
        sb.append(")");
        sb.append("row format delimited").append(" ")
          .append("fields terminated by '").append(fieldsTerminate).append("' ")
          .append("collection items terminated by '").append(collItemsTerminate).append("' ")
          .append("map keys terminated by '").append(mapKeysTerminate).append("'");

        System.out.println(sb.toString());

        this.stmt.execute(sb.toString());
    }

    // insert into 数据库.表名(List<String>) values(1,'zs'),(2,'ls');
    public void insert(
        String dbName,
        String tblName,
        List<String> fields,
        List<String> ... vals
    ) throws Exception{
        if(vals.length<1) throw new Exception("至少提供一组数据！");
        StringBuilder sb=new StringBuilder();
        sb.append("insert into ").append(dbName).append(".").append(tblName).append("(id,name)");
        sb.append(" values(1,'zs')");

        System.out.println(sb.toString());

        // if(fields!=null&&fields.size()>0){
        //     // 拼接表后面的括号
        //     sb.append("(");
        //     fields.forEach(field->sb.append(field).append(","));
        //     sb.setLength(sb.length()-1);
        //     sb.append(")");
        // }
        // sb.append(" ").append("values");
        // sb.append("(");
        // for(List<String> val: vals){
        //     val.forEach(s->{
        //         sb.append(s).append(",");
        //     });
        //     sb.setLength(sb.length()-1);
        //     sb.append("),(");
        //     sb.append(")").append(",");
        // }
        // sb.setLength(sb.length()-1);
        //
        // System.out.println(sb.toString());

        int i=this.stmt.executeUpdate(sb.toString());
        System.out.println(i);
    }

    public void select() throws SQLException {
        String sql = "select * from default.tbl2";
        ResultSet resultSet = this.stmt.executeQuery(sql);
        while (resultSet.next()){
            int id = resultSet.getInt(0);
            String name = resultSet.getString(1);
            Array courses = resultSet.getArray(2);
        }
    }


    public static void main(String[] args) throws Exception{
        HiveTest ht=new HiveTest();
        // ht.createDatabase("briup");

        // List<String> list=new ArrayList<>();
        // list.add("id:int");
        // list.add("name:string");
        // list.add("age:int");
        // list.add("courses:array<string>");
        // ht.createTable("briup","tbl2",list,"\\t",",",":");

        List<String> l1=new ArrayList<>();
        l1.add("id");
        l1.add("name");

        List<String> l2=new ArrayList<>();
        l2.add("1");
        l2.add("zs");

        List<String> l3=new ArrayList<>();
        l2.add("2");
        l2.add("ls");


        ht.insert("briup","tbl2",l1,l2,l3);
    }
}
