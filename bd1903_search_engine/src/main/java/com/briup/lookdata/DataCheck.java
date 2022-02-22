package com.briup.lookdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.NavigableMap;

/**
 * @program: bd1903_search_engine
 * @package: com.briup.lookdata
 * @filename: DataCheck.java
 * @create: 2019/11/11 18:35
 * @author: 29314
 * @description: .查看hbase数据库信息
 **/

public class DataCheck {
    public static String zk1 = "172.16.0.4:2181,172.16.0.5:2181,172.16.0.6:2181,172.16.0.7:2181";
    public static String master1 = "computer1.cloud.briup.com";

    public static String zk2 = "192.168.112.120:2181,192.168.112.121:2181,192.168.112.122:2181";
    public static String master2 = "when2";


    /*private Configuration conf;
    private Connection conn;
    private Table table;
    private Admin admin;

    public DataCheck() throws IOException {
        this.conf = getHbaseConf(zk1,master1);
        this.conn = ConnectionFactory.createConnection(conf);
        this.admin = this.conn.getAdmin();
    }*/
    //获取配置Configuration对象
    public Connection getHbaseConn(String zks,String master) throws IOException {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", zks);
        conf.set("hbase.master.dns.interface", master);
        return ConnectionFactory.createConnection(conf);
    }
    //获取完整的表名
    public String getTblName(String ns, String tbl) {
        tbl = (ns == null || "".equals(ns)) ? tbl : ns + ":" + tbl;
        return tbl;
    }

    //创建namespace
    public void createNamespace(Connection conn,String nsName) throws IOException {
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(nsName);
        NamespaceDescriptor descriptor = builder.build();
        conn.getAdmin().createNamespace(descriptor);
        System.out.println("命名空间"+nsName+"创建完成");
    }

    //创建表
    public void createTable(Connection conn,String nsName,String tblName,String... cfs) throws Exception {
        if(cfs.length<1){
            throw new Exception("至少提供一个列族名！");
        }
        tblName = this.getTblName(nsName,tblName);
        TableDescriptor build = TableDescriptorBuilder.newBuilder(TableName.valueOf(tblName)).build();

        TableDescriptorBuilder.ModifyableTableDescriptor mtd = (TableDescriptorBuilder.ModifyableTableDescriptor) build;
        for(String cf:cfs){
            ColumnFamilyDescriptorBuilder builder =
                    ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf));
            ColumnFamilyDescriptor cfd = builder.build();
            mtd.setColumnFamily(cfd);
        }
        conn.getAdmin().createTable(mtd);
        System.out.println("表格"+tblName+"创建完成");
    }

    //获取表
    public Table getTable(Connection conn,String ns, String tbl) throws IOException {
        String tblName = this.getTblName(ns, tbl);
        return conn.getTable(TableName.valueOf(tblName));
    }
    //打印Result结果集
    public void printResult(Result result){
        StringBuilder sb =new StringBuilder();
        sb.append(Bytes.toString(result.getRow())).append("{");
        NavigableMap<byte[],NavigableMap<byte[], NavigableMap<Long,byte[]>>> cfKeyMap=result.getMap();

        cfKeyMap.forEach((cf,o1)->{
            sb.append(Bytes.toString(cf)).append("{");
            o1.forEach((cn,o2)->{
                sb.append(Bytes.toString(cn)).append("{");
                o2.forEach((ts,o3)->{
                    sb.append(ts).append(":").append(Bytes.toString(o3)).append(",");
                });
                sb.setLength(sb.length()-1);
                sb.append("}");
            });
            sb.append("}");
        });
        sb.append("}");
        System.out.println(sb.toString());
    }
    //查看完整表信息
    public void scan(Connection conn,String nsName, String tblName) throws IOException {
        Table table = this.getTable(conn, nsName, tblName);
        Scan scan=new Scan();
        ResultScanner results=table.getScanner(scan);
        //int count = 0;
        /*for(Result result:results){
            count++;
        }*/
        results.forEach(this::printResult);
        //System.out.println(count);
    }
    //复制表
    public void copytables(Connection conn1,String nsName1, String tblName1,Connection conn2,String nsName2, String tblName2) throws IOException {
        Table table = this.getTable(conn1, nsName1, tblName1);
        Scan scan=new Scan();
        ResultScanner results=table.getScanner(scan);

        for (Result res:results){
            NavigableMap<byte[],NavigableMap<byte[], NavigableMap<Long,byte[]>>> map=res.getMap();
            map.forEach((cf,val1)->{
                val1.forEach((cn,val2)->{
                    val2.forEach((ts,val)->{
                        try {
                            put(conn2,nsName2,tblName2,res.getRow(),cf,cn,val,ts);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        }
        System.out.println("copy完成");
    }
    //向表中插入数据
    public void put(
            Connection conn,//hbase数据库连接
            String nsName,  // 命令空间名
            String tblName, // 表名
            byte[] rowkey,  // 行键
            byte[] cf,      // 列族名
            byte[] cn,      // 列名
            byte[] val,     // 值
            Long ts         // 时间戳
    ) throws IOException{
        // 获取Table对象，对表进行了封装
        Table table = this.getTable(conn, nsName, tblName);
        Put put=new Put(rowkey);
        put.addColumn(cf,cn,ts,val);
        table.put(put);
        System.out.println("put完成");
    }
    //获取数据
    public void get(
            Connection conn,//hbase数据库连接
            String nsName,  // 命令空间名
            String tblName, // 表名
            byte[] cf,      // 列族名
            byte[] cn      // 列名
    ) throws IOException{
        // 获取Table对象，对表进行了封装
        Table table = this.getTable(conn, nsName, tblName);
        ResultScanner scanner = table.getScanner(new Scan());
        int count = 0;
        for(Result result:scanner){
            byte[] value = result.getValue(cf, cn);
            /*NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(cf);
            familyMap.forEach((k,v)->{
                System.out.print(Bytes.toString(k)+":"+Bytes.toString(v)+" ");
            });
            System.out.println();*/
            //System.out.println(Bytes.toInt(value));
            //System.out.println(new String(value));
            System.out.println(Bytes.toString(value));
            /*if(Bytes.toString(value).equals("10")){
                count++;
            }*/
        }
        //System.out.println(count);
    }
    //删除表
    public void deleteTable(Connection conn,String nsName,String tblName) throws IOException{
        tblName=this.getTblName(nsName,tblName);
        TableName tn=TableName.valueOf(tblName);
        boolean exists=conn.getAdmin().tableExists(tn);
        if(exists){
            conn.getAdmin().disableTable(tn);
            conn.getAdmin().deleteTable(tn);
            System.out.println("delete完成");
        }
    }

    public static void main(String[] args) throws Exception {
        DataCheck check = new DataCheck();

        Connection conn1 = check.getHbaseConn(zk1, master1);
        Connection conn2 = check.getHbaseConn(zk2, master2);

        //check.createNamespace(conn1,"when");
        //check.createTable(conn1,"when","tbl_invert_index","page");

        check.copytables(conn2,"when","tbl_dg",conn1,"when","tbl_invert_index");
//bd1902_zuzi:invertindex_result
        //check.scan(conn1,"when","invertindex_result");
        //check.scan(conn1,"bd1902_zuzi","invertindex_result");
        //check.scan(conn1,"when","tbl_invert_index");

        //System.out.println("");
        //check.scan(conn1,null,"table_info");
        //check.put(conn1,null,"table_info",Bytes.toBytes("when:tbl_invert_index"),
               //Bytes.toBytes("info"),Bytes.toBytes("owner"),Bytes.toBytes("when"),
                //System.currentTimeMillis());
        //new DataCheck().createTable("when","tbl_dc","ols");
        //new DataCheck().scan("when","tbl_dc");

        //check.scan(conn1,"bd1902_zuzi","invertindex_result");
        //check.scan(conn1,"when","tbl_dg");
        //check.scan(conn2,"when","tbl_dg");
        //check.deleteTable(conn2,"when","tbl_dc");
        //check.scan(conn1,null,"huawei_webpage");
        //check.createTable(conn2,"when","tbl_dc","p","ol","r");
        //check.copytables(conn1,null,"aliyun_webpage",conn2,"when","webpage");
        //check.scan(conn2,"when","webpage");
//        byte[] bytes = "r".getBytes();
//        System.out.println(bytes);
//        System.out.println(Bytes.toString(bytes));
        //check.get(conn2,"when","webpage",Bytes.toBytes("f"),Bytes.toBytes("st"));
        //check.get(conn1,null,"aliyun_webpage","f".getBytes(),"st".getBytes());
        //check.get(conn2,"when","tbl_dc",Bytes.toBytes("r"),Bytes.toBytes("rank"));
        //System.out.println("/");
        //System.out.println("asda/aaaa".split("/").length);

        //System.out.println(check.getURL("com.aliyun.edu:https/course/explore/programlanguage"));
    }
/*
    public String getURL(String old){
        String[] split = old.split(":");
        if(split.length==2){
            String[] split0 = split[0].split("\\.");

            int index = split[1].indexOf("/");
            String head = split[1].substring(0,index).trim();
            String end = split[1].substring(index).trim();

            StringBuilder sb = new StringBuilder();
            sb.append(head).append("://");
            for(int i = split0.length-1;i>=0;i--){
                sb.append(split0[i]).append(".");
            }
            sb.setLength(sb.length()-1);
            sb.append(end);
            return sb.toString();
        }
        return null;
    }*/
}
