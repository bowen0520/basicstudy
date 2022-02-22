package hadoop.mapreduce.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.join
 * @filename: ScoreAnalysisByReduceJoin.java
 * @create: 2019/10/11 14:45
 * @author: 29314
 * @description: .使用Map端join连接所有成绩
 **/

public class ScoreAnalysisByReduceJoin extends Configured implements Tool {
    //对chinese.txt的Mapper
    static class MyChineseMapper extends Mapper<Text,Text,KeyFlag,Text>{
        private KeyFlag k2 = new KeyFlag();
        private Text v2 = new Text();
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            this.k2.setName(key.toString());
            this.k2.setFlag("a");
            this.v2.set(value.toString());
            context.write(this.k2,this.v2);
        }
    }

    //对math.txt的Mapper
    static class MyMathMapper extends Mapper<Text,Text,KeyFlag,Text>{
        private KeyFlag k2 = new KeyFlag();
        private Text v2 = new Text();
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            this.k2.setName(key.toString());
            this.k2.setFlag("b");
            this.v2.set(value.toString());
            context.write(this.k2,this.v2);
        }
    }

    //对english.txt的mapper
    static class MyEnglishMapper extends Mapper<Text,Text,KeyFlag,Text>{
        private KeyFlag k2 = new KeyFlag();
        private Text v2 = new Text();
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            this.k2.setName(key.toString());
            this.k2.setFlag("c");
            this.v2.set(value.toString());
            context.write(this.k2,this.v2);
        }
    }

    //自定义分区器
    static class MyPartitioner extends Partitioner<KeyFlag,Text>{
        @Override
        public int getPartition(KeyFlag keyFlag, Text text, int i) {
            return keyFlag.getName().hashCode()%i;
        }
    }

    //自定义分组器
    static class MyComparator extends WritableComparator{
        public MyComparator() {
            super(KeyFlag.class,true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            KeyFlag ka = (KeyFlag) a;
            KeyFlag kb = (KeyFlag) b;
            return ka.getName().compareTo(kb.getName());
        }
    }

    //所有mapper的数据都进入该reduce
    static class MyReducer extends Reducer<KeyFlag,Text,Text,Text>{
        private Text k3 = new Text();
        private Text v3 = new Text();

        @Override
        protected void reduce(KeyFlag key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            k3.set(key.getName());
            StringBuilder sb = new StringBuilder();
            for(Text s:values){
                sb.append(s.toString()).append(" ");
            }
            v3.set(sb.substring(0,sb.length()-1));
            context.write(this.k3,this.v3);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in1 = new Path(conf.get("in1"));
        Path in2 = new Path(conf.get("in2"));
        Path in3 = new Path(conf.get("in3"));
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "reduce端成绩连接");
        job.setJarByClass(this.getClass());

        // 多输入技术,设置多个mapper输入，
        // 但要保证这些mapper输出了K/V的数据类型要一致
        MultipleInputs.addInputPath(
                job,in1,KeyValueTextInputFormat.class,MyChineseMapper.class);
        MultipleInputs.addInputPath(
                job,in2,KeyValueTextInputFormat.class,MyMathMapper.class);
        MultipleInputs.addInputPath(
                job,in3,KeyValueTextInputFormat.class,MyEnglishMapper.class);
        //设置mapper的输出key，value的数据类型
        job.setMapOutputKeyClass(KeyFlag.class);
        job.setMapOutputValueClass(Text.class);

        //设置分区器
        job.setPartitionerClass(MyPartitioner.class);

        //设置分组器
        job.setGroupingComparatorClass(MyComparator.class);

        //设置reduce端的配置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job,out);
        
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ScoreAnalysisByReduceJoin(),args));
    }
}
