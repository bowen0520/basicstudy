package hadoop.mapreduce.patentanalysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapper
 * @filename: PatentCitesMapper.java
 * @create: 2019/09/26 16:42
 * @author: 29314
 * @description: .分析专利被引用了多少次Mapper端
 **/

public class PatentCitedsMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
    private Text k2 = new Text();
    private IntWritable v2 = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] patents = value.toString().split(",");
        this.k2.set(patents[1]);
        this.v2.set(1);
        context.write(this.k2,this.v2);
    }
}
