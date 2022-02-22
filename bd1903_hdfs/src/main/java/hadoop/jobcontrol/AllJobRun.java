package hadoop.jobcontrol;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.join.CompositeInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.jobcontrol
 * @filename: AllJobRun.java
 * @create: 2019/10/11 20:10
 * @author: 29314
 * @description: .作业工作流控制
 **/

public class AllJobRun extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in = new Path(conf.get("in"));//作业1的输入
        Path out1 = new Path(conf.get("out1"));//作业1的输出，作业2，3的输入
        Path out2 = new Path(conf.get("out2"));//作业2的输出，作业4的输入
        Path out3 = new Path(conf.get("out3"));//作业3的输出，作业4的输入
        Path out4 = new Path(conf.get("out4"));//作业4的输出

        //作业1的配置
        System.out.println("开始配置作业1");
        Job job1=Job.getInstance(conf,"计算92年每个气象站的气温");
        job1.setJarByClass(this.getClass());

        job1.setMapperClass(GetSidTemp.MyMapper.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(DoubleWritable.class);
        job1.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job1,in);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(DoubleWritable.class);
        job1.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job1,out1);
        System.out.println("作业1配置完成");
        //作业2的配置
        System.out.println("开始配置作业2");
        Job job2=Job.getInstance(conf,"计算92年每个气象站的最高气温");
        job2.setJarByClass(this.getClass());

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);
        job2.setInputFormatClass(KeyValueTextInputFormat.class);
        KeyValueTextInputFormat.addInputPath(job2,out1);

        job2.setReducerClass(GetSidMaxTemp.MyReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);
        job2.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job2,out2);
        System.out.println("作业2配置完成");
        //作业3的配置
        System.out.println("开始配置作业3");
        Job job3=Job.getInstance(conf,"计算92年每个气象站的平均气温");
        job3.setJarByClass(this.getClass());

        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(Text.class);
        job3.setInputFormatClass(KeyValueTextInputFormat.class);
        KeyValueTextInputFormat.addInputPath(job3,out1);

        job3.setReducerClass(GetSidAvgTemp.MyReducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        job3.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job3,out3);
        System.out.println("作业3配置完成");
        //作业4的配置
        System.out.println("开始配置作业4");
        String str = CompositeInputFormat.compose("inner", KeyValueTextInputFormat.class, out2, out3);
        conf.set("mapreduce.join.expr",str);

        Job job4 = Job.getInstance(conf, "获取每个气象站最高和平均温度");
        job4.setJarByClass(this.getClass());

        job4.setMapperClass(GetSidMaxAndAvgTemp.MyMapper.class);
        job4.setMapOutputKeyClass(Text.class);
        job4.setMapOutputValueClass(Text.class);
        job4.setInputFormatClass(CompositeInputFormat.class);
        FileInputFormat.addInputPath(job4,out2);
        FileInputFormat.addInputPath(job4,out3);

        job4.setOutputKeyClass(Text.class);
        job4.setOutputValueClass(Text.class);
        job4.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job4,out4);
        System.out.println("作业4配置完成");
        //配置工作流
        //将不可控制的job变为可控制
        ControlledJob cj1 = new ControlledJob(conf);
        cj1.setJob(job1);
        ControlledJob cj2 = new ControlledJob(conf);
        cj2.setJob(job2);
        ControlledJob cj3 = new ControlledJob(conf);
        cj3.setJob(job3);
        ControlledJob cj4 = new ControlledJob(conf);
        cj4.setJob(job4);

        //添加依赖关系
        cj2.addDependingJob(cj1);
        cj3.addDependingJob(cj1);
        cj4.addDependingJob(cj2);
        cj4.addDependingJob(cj3);

        JobControl jc = new JobControl("作业控制");
        jc.addJob(cj1);
        jc.addJob(cj2);
        jc.addJob(cj3);
        jc.addJob(cj4);
        //提交作业
        Thread t = new Thread(jc);
        t.start();
        System.out.println("所有作业配置完成");
        do{
            for(ControlledJob j: jc.getRunningJobList()){
                j.getJob().monitorAndPrintJob();
            }
        }while(!jc.allFinished());

        return 0;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new AllJobRun(),args));
    }
}
