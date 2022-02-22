package hbase.withmapreducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @program: bd1903.hbase
 * @package: com.briup.bigdata.bd1903.hbase
 * @filename: HBaseDataMove.java
 * @create: 2019.10.31 17:25
 * @author: Kevin
 * @description: .
 **/
public class HBaseDataMove extends Configured implements Tool{

    static class HBaseDataMoveMapper
        extends TableMapper<ImmutableBytesWritable,Put>{
        @Override
        protected void map(
            ImmutableBytesWritable k1,Result v1,Context context) throws IOException, InterruptedException{

            Put put=new Put(k1.get());

            NavigableMap<byte[],NavigableMap<byte[],NavigableMap<Long,byte[]>>> map=v1.getMap();

            map.forEach((cf,val1)->{
                if(Bytes.equals(cf,"f1".getBytes())){
                    val1.forEach((cn,val2)->{
                        if(Bytes.equals(cn,"name".getBytes())){
                            val2.forEach((ts,val)->{
                                put.addColumn(cf,cn,ts,val);
                            });
                        }
                    });
                }
            });

            context.write(k1,put);
        }
    }

    @Override
    public int run(String[] strings) throws Exception{
        Configuration conf=this.getConf();

        Job job=Job.getInstance(conf,"数据迁移");
        job.setJarByClass(this.getClass());

        Scan scan=new Scan();
        TableMapReduceUtil.initTableMapperJob("test:tbl1",scan,HBaseDataMoveMapper.class,ImmutableBytesWritable.class,Put.class,job);
        TableMapReduceUtil.initTableReducerJob("test:tbl2",null,job);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new HBaseDataMove(),args));
    }
}
