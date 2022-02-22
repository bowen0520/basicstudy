package hadoop.jobcontrol;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.jobcontrol
 * @filename: GetSidMaxTemp.java
 * @create: 2019/10/11 19:37
 * @author: 29314
 * @description: .获取每个气象站最高温度
 **/

public class GetSidMaxTemp extends Configured implements Tool {

    static class MyReducer extends Reducer<Text,Text,Text,Text> {
        private Text k3=new Text();
        private Text v3=new Text();
        DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void reduce(Text k2,Iterable<Text> v2s,Context context) throws IOException, InterruptedException{
            double max=-Double.MAX_VALUE;

            for(Text v2: v2s){
                double temp = Double.parseDouble(v2.toString());
                if(temp>max){
                    max = temp;
                }
            }
            this.k3.set(k2.toString());
            this.v3.set("最大温度:"+df.format(max));

            context.write(this.k3,this.v3);
        }
    }

    @Override
    public int run(String[] strings) throws Exception{
        Configuration conf=this.getConf();
        Path in=new Path(conf.get("in"));
        Path out=new Path(conf.get("out"));

        Job job=Job.getInstance(conf,"计算92年每个气象站的最高气温");
        job.setJarByClass(this.getClass());

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        KeyValueTextInputFormat.addInputPath(job,in);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new GetSidTemp(),args));
    }
}
