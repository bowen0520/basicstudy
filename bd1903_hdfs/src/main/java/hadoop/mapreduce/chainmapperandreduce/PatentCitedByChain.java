package hadoop.mapreduce.chainmapperandreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.map.TokenCounterMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.chainmapperandreduce
 * @filename: PatentCitedByChain.java
 * @create: 2019/10/10 09:29
 * @author: 29314
 * @description: .使用hadoop自带的mapreduce链式调用实现专利分析
 **/

public class PatentCitedByChain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR,",");

        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "链式调用");
        job.setJarByClass(this.getClass());

        ChainMapper.addMapper(
                job, TokenCounterMapper.class,
                Text.class,Text.class,
                Text.class, IntWritable.class,
                conf
        );

        ChainReducer.setReducer(
                job, IntSumReducer.class,
                Text.class,IntWritable.class,
                Text.class,IntWritable.class,
                conf
        );

        ChainReducer.addMapper(
                job, InverseMapper.class,
                Text.class,IntWritable.class,
                IntWritable.class,Text.class,
                conf
        );

        job.setInputFormatClass(KeyValueTextInputFormat.class);
        FileInputFormat.addInputPath(job,in);

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job,out);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new PatentCitedByChain(),args));
    }
}
