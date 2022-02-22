package hadoop.homework.scoreanalysis;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.homework
 * @filename: CalaScore.java
 * @create: 2019/10/08 14:05
 * @author: 29314
 * @description: .成绩计算
 **/

public class CalaScore extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,Text,Text>{
        Text k2 = new Text();
        Text v2 = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }
    }


    @Override
    public int run(String[] strings) throws Exception {
        return 0;
    }
}
