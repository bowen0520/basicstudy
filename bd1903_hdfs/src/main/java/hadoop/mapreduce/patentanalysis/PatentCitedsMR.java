package hadoop.mapreduce.patentanalysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.homework
 * @filename: PatentAnalysisMR.java
 * @create: 2019/09/26 11:18
 * @author: 29314
 * @description: .专利数据分析（mapperreduce）分析专利被引用了多少次
 **/

public class PatentCitedsMR extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new PatentCitedsMR(),args));
    }
    // 作业配置，作业配置都是配置在run方法中；
    // 在整个MR程序运行的时候，run方法中的代码会在客户端执行；
    // 编写的Map任务和Reduce任务会在集群上执行；
    // 所以在run方法中出现的打印语句可以打印到控制台上，而在
    // Map或者Reduce任务中出现的打印语句不会被打印到控制台上，
    // 而是以日志的形式被记录日志文件中；
    @Override
    public int run(String[] strings) throws Exception {
        // 将程序打包至客户端【集群中的任意节点】上运行时
        //下面的代码会根据客户端配置自动读取配置文件
        Configuration conf = this.getConf();
        // 关于MapReduce程序运行时的结果存放路径在HDFS集群上一定不能
        // 预先存在，如果存在则该程序运行报异常；
        // MR程序的运行结果会被存放在指定的目录中；
        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "引用专利");
        job.setJarByClass(this.getClass());

        //Mapper任务配置
        //配置mapper任务要运行的类
        job.setMapperClass(PatentCitedsMapper.class);
        //配置Mapper任务输出的k2，v2的数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //配置mapper端读取原始数据的格式
        //配置Mapper任务以什么样的方式读取原始数据
        job.setInputFormatClass(TextInputFormat.class);
        //配置文件的输入路径
        TextInputFormat.addInputPath(job,in);

        //Reducer配置任务
        job.setReducerClass(PatentCitedsReducer.class);
        //配置reducer任务输出的k3，v3数据类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //配置reducer任务以什么样的方式输出原始数据
        job.setOutputFormatClass(TextOutputFormat.class);
        //配置文件的输出路径
        TextOutputFormat.setOutputPath(job,out);
        System.out.println("配置任务完成");
        //CombineFileInputFormat.setMaxInputSplitSize(job,12222L);
        //TextInputFormat.setMaxInputSplitSize(job,121312);
        return job.waitForCompletion(true)?0:1;
    }
}