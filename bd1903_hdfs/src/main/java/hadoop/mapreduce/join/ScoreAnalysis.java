package hadoop.mapreduce.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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
 * @package: hadoop.mapreduce.join
 * @filename: ScoreAnalysis.java
 * @create: 2019/10/10 18:54
 * @author: 29314
 * @description: .成绩分析
 **/

public class ScoreAnalysis extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,Text,Text>{
        private Text k2 = new Text();
        private Text v2 = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("[|]");
            this.k2.set(strs[1]);
            this.v2.set(strs[0]+"|"+strs[2]);
            context.write(this.k2,this.v2);
        }
    }

    static class MyReducer extends Reducer<Text,Text,Text,Text>{
        private Text k3 = new Text();
        private Text v3 = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            double sum = 0d;
            StringBuilder sb = new StringBuilder();
            for(Text v:values){
                String[] strs = v.toString().split("[|]");
                switch (strs[0]){
                    case "a":{
                        sb.append("语文:");
                        break;
                    }
                    case "b":{
                        sb.append("英语:");
                        break;
                    }
                    case "c":{
                        sb.append("数学:");
                        break;
                    }
                }
                sb.append(strs[1]).append(",");
                sum+=Double.parseDouble(strs[1]);
                count++;
            }
            DecimalFormat df = new DecimalFormat( "0.00");

            sb.append("总分:").append(sum).append(",").append("平均分:").append(df.format(sum/count));

            this.k3.set(key.toString());
            this.v3.set(sb.toString());

            context.write(this.k3,this.v3);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        String ins = conf.get("in");
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "成绩分析");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPaths(job,ins);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        //job.setNumReduceTasks(2);

        return job.waitForCompletion(true)?0:1;

    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ScoreAnalysis(),args));
    }
}
