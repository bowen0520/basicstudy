package hadoop.mapreduce.TotalSort;

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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.TotalSort
 * @filename: TotalSort1.java
 * @create: 2019/10/10 10:26
 * @author: 29314
 * @description: .全局排序实现1
 **/

public class TotalSort1 extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,Text, DoubleWritable>{
        private Text k2 = new Text();
        private DoubleWritable v2 = new DoubleWritable();
        DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] ss = value.toString().split("[\t]");
            this.k2.set(ss[0]);
            this.v2.set(Double.parseDouble(df.format(Double.parseDouble(ss[2]))));
            context.write(this.k2,this.v2);
        }
    }

    static class MyReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
        private Text k3 = new Text();
        private DoubleWritable v3 = new DoubleWritable();
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double max = -Double.MAX_VALUE;
            for(DoubleWritable temp:values){
                if(max<temp.get()){
                    max = temp.get();
                }
            }
            this.k3.set(key.toString());
            this.v3.set(max);

            context.write(this.k3,this.v3);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "全局排序1");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        job.setNumReduceTasks(2);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new TotalSort1(),args));
    }
}
