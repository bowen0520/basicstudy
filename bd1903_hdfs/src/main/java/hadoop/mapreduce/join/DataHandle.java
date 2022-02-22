package hadoop.mapreduce.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.join
 * @filename: DataHandle.java
 * @create: 2019/10/10 20:01
 * @author: 29314
 * @description: .对数据进行预处理，使其具有一定条件，能够进行Map端的join
 **/

public class DataHandle extends Configured implements Tool {
    static class MyMapper extends Mapper<Text,Text,Text,Text> {
        private Text k2 = new Text();
        private Text v2 = new Text();

        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            //System.out.println("信息："+key.toString()+" "+value.toString());
            String[] strs = value.toString().split("[|]");
            this.k2.set(strs[0]);
            String book = key.toString();
            String obj = book.equals("a")?"语文":(book.equals("b")?"英语":"数学");
            this.v2.set(obj+":"+strs[1]);
            //this.k2.set(key.toString());
            //this.v2.set(value.toString());
            context.write(this.k2,this.v2);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR,"|");

        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "数据预处理");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        FileInputFormat.addInputPath(job,in);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job,out);
        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new DataHandle(),args));
    }
}
