package hadoop.mapreduce.secondarysort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.secondarysort
 * @filename: MyPartitioner.java
 * @create: 2019/10/10 15:38
 * @author: 29314
 * @description: .自定义分区器
 **/

public class MyPartitioner extends Partitioner<YearSid, DoubleWritable> {

    @Override
    public int getPartition(YearSid yearSid, //mapper端出来的value
                            DoubleWritable doubleWritable,//mapper端出来的key
                            int i//分区数
    ) {
        return yearSid.getYear().hashCode()%i;
    }
}
