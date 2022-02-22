package hadoop.jobcontrol;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.jobcontrol
 * @filename: GetSidTemp.java
 * @create: 2019/10/11 19:30
 * @author: 29314
 * @description: .获取气象站编号以及温度
 **/

public class GetSidTemp extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        private Text k2 = new Text();
        private DoubleWritable v2 = new DoubleWritable();

        @Override
        protected void map(LongWritable k1,Text v1,Context context) throws IOException, InterruptedException{
            String line = v1.toString();
            if(check(line)){
                this.k2.set(line.substring(0,15));
                this.v2.set(Double.parseDouble(line.substring(87,92))/10);
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
        Path out=new Path(conf.get("out"));

        Job job=Job.getInstance(conf,"计算92年每个气象站的气温");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new GetSidTemp(),args));
    }
}
