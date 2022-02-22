package hbase.option;

import java.io.IOException;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcUtils;

/**
 * @program: hbase.pre
 * @package: com.briup.bigdata.bd1903.hbase.pre
 * @filename: SumEndpointTest.java
 * @create: 2019.10.31 10:22
 * @author: Kevin
 * @description: .
 **/
public class SumEndpointTest{
    public static void main(String[] args) throws IOException{
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        TableName tableName = TableName.valueOf("test:tbl1");
        Table table = connection.getTable(tableName);
        final Sum.SumRequest request = Sum.SumRequest.newBuilder().setFamily("f1").setColumn("name").build();
        try {
            Map<byte[], Long> results = table.coprocessorService(
                Sum.SumService.class,
                null, /* start key */
                null, /* end key */
                aggregate->{
                    // BlockingRpcCallback<Sum.SumResponse> rpcCallback = new BlockingRpcCallback<>();
                    // aggregate.getSum(null,request,rpcCallback);
                    CoprocessorRpcUtils.BlockingRpcCallback<Sum.SumResponse> callback=new CoprocessorRpcUtils.BlockingRpcCallback<>();
                    aggregate.getSum(null,request,callback);
                    Sum.SumResponse response = callback.get();
                    return response.hasSum() ? response.getSum() : 0L;
                }
            );
            for (Long sum : results.values()) {
                System.out.println("Sum = " + sum);
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
