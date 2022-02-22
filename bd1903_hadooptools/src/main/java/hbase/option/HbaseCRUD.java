package hbase.option;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.NavigableMap;
import java.util.Set;

/**
 * @program: bd1903_hadooptools
 * @package: hbase.option
 * @filename: HbaseCRUD.java
 * @create: 2019/10/29 17:21
 * @author: 29314
 * @description: .hbase相关操作
 **/

public class HbaseCRUD {
    private Configuration conf;
    private Connection conn;
    private Admin admin;
    private Table table;

    public HbaseCRUD() throws IOException {
        this.conf = HBaseConfiguration.create();
        this.conn = ConnectionFactory.createConnection(conf);
        this.admin = this.conn.getAdmin();
    }
    //获取表
    public Table getTable(String ns, String tbl) throws IOException {
        String tblName = this.getTblName(ns, tbl);
        this.table = this.conn.getTable(TableName.valueOf(tblName));
        return this.table;
    }

    public String getTblName(String ns, String tbl) {
        tbl = (ns == null || "".equals(ns)) ? tbl : ns + ":" + tbl;
        return tbl;
    }

    //创建namespace
    public void createNamespace(String nsName) throws IOException {
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(nsName);
        NamespaceDescriptor descriptor = builder.build();
        this.admin.createNamespace(descriptor);
    }

    //创建表
    public void createTable(String nsName,String tblName,String... cfs) throws Exception {
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
        this.admin.createTable(mtd);
    }

    // drop_namespace 'ns4'
    public void deleteNamespace(String nsName) throws IOException{
        // NamespaceDescriptor nd=
        //     NamespaceDescriptor.create(nsName).build();

        // 通过获取ZK的数据/hbase/table/的子节点，通过提供的
        // 命名空间参数进行匹配，获取合适的表明，然后进行表的删除

        this.admin.deleteNamespace(nsName);
    }

    // drop 'ns4:tbl1'
    public void deleteTable(String nsName,String tblName) throws IOException{
        tblName=this.getTblName(nsName,tblName);

        TableName tn=TableName.valueOf(tblName);

        boolean exists=this.admin.tableExists(tn);

        if(exists){
            this.admin.disableTable(tn);
            this.admin.deleteTable(tn);
        }
    }

    // put 'ns4:tbl1','rk1','cf:c','v'[,ts]
    public void put(
            String nsName,  // 命令空间名
            String tblName, // 表名
            String rowkey,  // 行键
            String cf,      // 列族名
            String cn,      // 列名
            String val,     // 值
            Long ts         // 时间戳
    ) throws IOException{
        // 获取字符串的表名
        // tblName=this.getTblName(nsName,tblName);

        // 获取Table对象，对表进行了封装
        this.table=this.getTable(nsName,tblName);

        Put put=new Put(Bytes.toBytes(rowkey));

        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),ts,Bytes.toBytes(val));

        // Cell cell=new KeyValue(
        //     Bytes.toBytes(rowkey),
        //     Bytes.toBytes(cf),
        //     Bytes.toBytes(cn),
        //     ts,
        //     KeyValue.Type.Put,
        //     Bytes.toBytes(val)
        // );
        // put.add(cell);

        this.table.put(put);

        // 批量添加
        // this.table.put(list);
    }

    // delete '[ns:]tbl1','1001','f2:addr'
    public void delete(
            String nsName,
            String tblName,
            String rk,
            String cf,
            String cn,
            Long ts
    ) throws IOException{
        this.table=this.getTable(nsName,tblName);
        Delete delete=new Delete(Bytes.toBytes(rk));

        delete.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));
        // delete.addColumn()

        this.table.delete(delete);
    }

    // 修改数据，先获取旧数据，然后delete，对旧数据修改之后再put

    // scan '[ns:]tbl1',{COLUMNS=>['f1:age',...]}
    public void scan(
            String nsName,
            String tblName,
            String cf,
            String cn
    ) throws IOException{
        this.table=this.getTable(nsName,tblName);

        Scan scan=new Scan();

        // 添加列族和列名，用于扫描某个列中的数据
        if(cf!=null&&!"".equals(cf)&&cn!=null&&!"".equals(cn))
            scan=scan.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));

        // scan.setFilter(new KeyOnlyFilter());
        scan.setFilter(
                new RowFilter(
                        CompareOperator.GREATER_OR_EQUAL,
                        new BinaryComparator(Bytes.toBytes("1002"))
                ));

        ResultScanner results=this.table.getScanner(scan);

        results.forEach(this::printResult);

        // Iterator<Result> it=results.iterator();

        // while(it.hasNext()){
        //     Result next=it.next();
        //     this.printResult(next);
        //
        // }
    }

    // get '[ns:]tbl1','1002','f1:name',{TIMESTAMP=>ts}
    public void get(
            String ns,
            String tbl,
            String rk,
            String cf,
            String cn,
            Long ts
    ) throws IOException{
        this.table=this.getTable(ns,tbl);

        Get get=new Get(Bytes.toBytes(rk));

        if(cf!=null&&!"".equals(cf)&&cn!=null&&!"".equals(cn))
            get=get.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));

        // get.setFilter(new ValueFilter())

        Result result=this.table.get(get);

        this.printResult(result);
    }





    public void printResult(Result result){
        StringBuilder sb=new StringBuilder();
        String rk=new String(result.getRow());
        sb.append(rk).append("---");
        NavigableMap<byte[],NavigableMap<byte[],NavigableMap<Long,byte[]>>> cfKeyMap=result.getMap();
        Set<byte[]> cfs=cfKeyMap.keySet();
        cfs.forEach(cf->{
            sb.append(new String(cf)).append("---");
            NavigableMap<byte[],NavigableMap<Long,byte[]>> cnKeyMap=cfKeyMap.get(cf);
            Set<byte[]> cns=cnKeyMap.keySet();
            cns.forEach(cn->{
                sb.append(new String(cn)).append("---");
                NavigableMap<Long,byte[]> tsKeyMap=cnKeyMap.get(cn);
                Set<Long> tss=tsKeyMap.keySet();
                tss.forEach(ts->{
                    sb.append(ts).append("---");
                    byte[] val=tsKeyMap.get(ts);
                    String v=new String(val);
                    sb.append(v);
                });
            });
        });
        System.out.println(sb);
    }

    public void scan(
            String nsName,
            String tblName
    ) throws IOException{
        this.table=this.getTable(nsName,tblName);

        Scan scan=new Scan();

        // scan.setFilter(new KeyOnlyFilter());
        /*scan.setFilter(
                new RowFilter(
                        CompareOperator.GREATER_OR_EQUAL,
                        new BinaryComparator(Bytes.toBytes("1002"))
                ));*/

        ResultScanner results=this.table.getScanner(scan);

        results.forEach(this::printResult);
    }

    public static void main(String[] args) throws Exception {
        HbaseCRUD hb = new HbaseCRUD();


        //hb.createNamespace("tset1");

        //hb.createTable("tset1","tbl1","f1","f2");


        /*Table table = hb.getTable("test", "tbl1");
        Result result = table.get(new Get("name".getBytes()));
        NavigableMap<byte[], NavigableMap<byte[], byte[]>> noVersionMap = result.getNoVersionMap();
        noVersionMap.forEach((k, v) -> {
            System.out.print(new String(k) + "\t");
            v.forEach((vk, vv) -> {
                System.out.print(new String(vk) + "=" + new String(vv) + "\t");
            });
            System.out.println();
        });*/
    }
}
