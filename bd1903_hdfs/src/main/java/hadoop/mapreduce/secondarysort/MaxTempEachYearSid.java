package hadoop.mapreduce.secondarysort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.secondarysort
 * @filename: MaxTempEachYearSid.java
 * @create: 2019/10/10 15:12
 * @author: 29314
 * @description: .计算每年每个气象站的最高温度
 **/

public class MaxTempEachYearSid extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,YearSid, DoubleWritable>{
        private YearSid k2 = new YearSid();
        private DoubleWritable v2 = new DoubleWritable();
        DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("[\t]");
            k2.setYear(strings[0]);
            k2.setSid(strings[1]);
            v2.set(Double.parseDouble(df.format(Double.parseDouble(strings[2])/10)));
            context.write(this.k2,this.v2);
        }
    }

    static class MyReducer extends Reducer<YearSid,DoubleWritable,YearSid,DoubleWritable>{
        private YearSid k3 = new YearSid();
        private DoubleWritable v3 = new DoubleWritable();

        @Override
        protected void reduce(YearSid key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double max = -Double.MAX_VALUE;
            for(DoubleWritable temp:values){
                if(temp.get()>max){
                    max = temp.get();
                }
            }
            this.k3.setYear(key.getYear());
            this.k3.setSid(key.getSid());
            this.v3.set(max);
            context.write(this.k3,this.v3);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "二次排序");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(YearSid.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(YearSid.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        job.setNumReduceTasks(2);

        // 设置分区器
        job.setPartitionerClass(MyPartitioner.class);
        //更改分区器
        /*job.setPartitionerClass(TotalOrderPartitioner.class);
        InputSampler.RandomSampler<YearSid,DoubleWritable> sampler=
                new InputSampler.RandomSampler(0.8,1000,2);
        InputSampler.writePartitionFile(job,sampler);
        String file = TotalOrderPartitioner.getPartitionFile(conf);
        job.addCacheFile(new URI(file));*/

        // 设置分组比较器
        job.setGroupingComparatorClass(MyComparator.class);

        //设置排序器
        job.setSortComparatorClass(MyComparator.class);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new MaxTempEachYearSid(),args));
    }
}
