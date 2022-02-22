package hadoop.mapreduce.combiner;

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
 * @package: hadoop.mapreduce.combiner
 * @filename: WeatherDataParserWithCombiner.java
 * @create: 2019/10/09 17:26
 * @author: 29314
 * @description: .原始天气采集分析(有combiner)
 **/

public class WeatherDataParserWithCombiner extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text,Text,AvgData> {
        private Text k2=new Text();
        private AvgData v2=new AvgData();

        @Override
        protected void map(LongWritable k1,Text v1,Context context) throws IOException, InterruptedException{
            String line = v1.toString();
            if(check(line)){
                this.k2.set(line.substring(15,19)+" "+line.substring(0,15));
                this.v2.setTemp(Double.parseDouble(line.substring(87,92))/10);
                this.v2.setNum(1);
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

    // 自定义Combiner
    static  class MyCombiner extends Reducer<Text,AvgData,Text,AvgData>{
        private Text kc2=new Text();
        private AvgData vc2=new AvgData();

        @Override
        protected void reduce(Text key, Iterable<AvgData> values, Context context) throws IOException, InterruptedException {
            double sum=0;
            int count=0;

            for(AvgData v2: values){
                sum+=v2.getTemp();
                count+=v2.getNum();
            }

            this.kc2.set(key.toString());
            this.vc2.setTemp(sum);
            this.vc2.setNum(count);

            context.write(this.kc2,this.vc2);
        }
    }

    static class MyReducer extends Reducer<Text,AvgData,Text, DoubleWritable> {
        private Text k3=new Text();
        private DoubleWritable v3=new DoubleWritable();

        @Override
        protected void reduce(Text k2,Iterable<AvgData> v2s,Context context) throws IOException, InterruptedException{
            double sum=0;
            int count=0;

            for(AvgData v2: v2s){
                sum+=v2.getTemp();
                count+=v2.getNum();
            }
            DecimalFormat df = new DecimalFormat( "0.00");
            double avg=sum/count;

            this.k3.set(k2.toString());
            this.v3.set(Double.parseDouble(df.format(avg)));

            context.write(this.k3,this.v3);
        }
    }

    @Override
    public int run(String[] strings) throws Exception{
        Configuration conf=this.getConf();
        Path in=new Path(conf.get("in"));
        Path out=new Path(conf.get("out"));

        Job job=Job.getInstance(conf,"每个气象站的平均气温");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(AvgData.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);

        // 设置Combiner类，
        job.setCombinerClass(MyCombiner.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        // 设置Reduce的个数
        job.setNumReduceTasks(5);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new WeatherDataParserWithCombiner(),args));
    }
}
