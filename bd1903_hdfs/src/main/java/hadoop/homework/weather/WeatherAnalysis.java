package hadoop.homework.weather;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.combiner
 * @filename: WeatherAnalysis.java
 * @create: 2019/10/09 15:30
 * @author: 29314
 * @description: .原始天气数据提取
 **/

public class WeatherAnalysis extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,Text, Text> {
        private Text k2=new Text();
        private Text v2=new Text();

        @Override
        protected void map(LongWritable k1,Text v1,Context context) throws IOException, InterruptedException{
            String line = v1.toString();
            if(check(line)){
                this.k2.set(line.substring(15,19));
                this.v2.set(line.substring(0,15)+"\t"+line.substring(87,92));
                context.write(this.k2,this.v2);
            }
        }

        private boolean check(String line){
            if(line.length()<93) return false;
            // 如果采集到的数据是+9999，不符合条件
            if("+9999".equals(line.substring(87,92))) return false;
            // 数据质量不佳，不符合条件
            if(!"01459".contains(line.substring(92,93))) return false;
            return true;
        }
    }

    @Override
    public int run(String[] strings) throws Exception{
        Configuration conf=this.getConf();
        Path in=new Path(conf.get("in"));
        //Path in1=new Path(conf.get("in1"));
        //Path in2=new Path(conf.get("in2"));
        //Path in3=new Path(conf.get("in3"));

        Path out=new Path(conf.get("out"));
        String check = conf.get("check");

        Job job=Job.getInstance(conf,"气象站气温分析");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);
        //TextInputFormat.addInputPath(job,in1);
        //TextInputFormat.addInputPath(job,in2);
        //TextInputFormat.addInputPath(job,in3);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        if("text".equals(check)) {
            job.setOutputFormatClass(TextOutputFormat.class);
        }else if("seq".equals(check)){
            job.setOutputFormatClass(SequenceFileOutputFormat.class);
        }
        FileOutputFormat.setOutputPath(job, out);
        // 设置Reduce的个数
        //job.setNumReduceTasks(5);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new WeatherAnalysis(),args));
    }
}
