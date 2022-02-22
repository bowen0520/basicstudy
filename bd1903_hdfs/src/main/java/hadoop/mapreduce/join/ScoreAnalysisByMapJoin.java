package hadoop.mapreduce.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.join.CompositeInputFormat;
import org.apache.hadoop.mapreduce.lib.join.TupleWritable;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Iterator;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.join
 * @filename: ScoreAnalysisByMapJoin.java
 * @create: 2019/10/10 20:16
 * @author: 29314
 * @description: .使用Map端join连接所有成绩
 **/

public class ScoreAnalysisByMapJoin extends Configured implements Tool {
    static class MyMapper extends Mapper<Text, TupleWritable,Text,Text> {
        private Text k2 = new Text();
        private Text v2 = new Text();

        @Override
        protected void map(Text key, TupleWritable value, Context context) throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();
            Iterator<Writable> iterator = value.iterator();
            while(iterator.hasNext()){
                sb.append(iterator.next().toString()).append(" ");
            }

            this.k2.set(key.toString());
            this.v2.set(sb.substring(0,sb.length()-1));
            context.write(this.k2,this.v2);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in1 = new Path(conf.get("in1"));
        Path in2 = new Path(conf.get("in2"));
        Path in3 = new Path(conf.get("in3"));
        Path out = new Path(conf.get("out"));

        String str = CompositeInputFormat.compose("inner", KeyValueTextInputFormat.class, in1, in2, in3);
        System.out.println("连接式："+str);
        // 通过系统配置信息，将该连接表达式传递到所有的Map任务上
        conf.set("mapreduce.join.expr",str);

        Job job = Job.getInstance(conf, "成绩连接");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(CompositeInputFormat.class);
        FileInputFormat.addInputPath(job,in1);
        FileInputFormat.addInputPath(job,in2);
        FileInputFormat.addInputPath(job,in3);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        //job.setNumReduceTasks(2);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ScoreAnalysisByMapJoin(),args));
    }
}
