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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
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
 * @package: hadoop.mapreduce.TotalSort
 * @filename: TotalSort1.java
 * @create: 2019/10/10 10:26
 * @author: 29314
 * @description: .全局排序实现4
 **/

public class TotalSort4 extends Configured implements Tool {
    static class MyMapper extends Mapper<Text, Text,Text, DoubleWritable>{
        private Text k2 = new Text();
        private DoubleWritable v2 = new DoubleWritable();
        DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String[] ss = value.toString().split("[\t]");
            this.k2.set(key.toString());
            this.v2.set(Double.parseDouble(df.format(Double.parseDouble(ss[1]))));
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

        Job job = Job.getInstance(conf, "全局排序4");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job,in);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        job.setNumReduceTasks(2);

        //采用桶排序，在所有数据中随机选取分区数减1个数据作为分区规则
        //然后按照这几个选取的数据分别进入相应的分区，再加上分区内会实现
        //局部排序，所以可以实现全局排序，随机选取会进行多次，
        // 选取分区最均匀的

        //更改分区器
        job.setPartitionerClass(TotalOrderPartitioner.class);

        //产生采样数据
        // 参数分析：采样比（从总数据中用多少数据作为采样样品），采样次数（要从多少次随机采样中取最优），分区数（要用样本分成多少个区）
        InputSampler.RandomSampler<LongWritable,DoubleWritable> sampler= new InputSampler.RandomSampler(0.8,1000,2);

        //生成分区文件【采样文件】,在运行该程序时，
        //客户端会生成采样文件
        InputSampler.writePartitionFile(job,sampler);

        //获取采样文件，获取采样文件的路径
        String file = TotalOrderPartitioner.getPartitionFile(conf);

        //上传采样文件至HDFS集群
        //文件名：_partition.lst
        job.addCacheFile(new URI(file));

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new TotalSort4(),args));
    }
}
