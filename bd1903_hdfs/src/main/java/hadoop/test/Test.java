package hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.test
 * @filename: Test.java
 * @create: 2019/10/14 11:12
 * @author: 29314
 * @description: .测试分区分组排序的默认使用排序是什么
 **/

public class Test extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable, Text, YearSid, DoubleWritable> {
        private YearSid k2 = new YearSid();
        private DoubleWritable v2 = new DoubleWritable();
        DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println(value.toString()+"_____________________");
            String[] strings = value.toString().split("[\t]");
            k2.setYear(strings[0]);
            k2.setSid(strings[1]);
            v2.set(Double.parseDouble(df.format(Double.parseDouble(strings[2])/10)));
            context.write(this.k2,this.v2);
        }
    }

    static class MyPartitioner extends Partitioner<YearSid,DoubleWritable>{
        @Override
        public int getPartition(YearSid yearSid, DoubleWritable doubleWritable, int i) {
            System.out.println("use Partition");
            return yearSid.getYear().hashCode()%i;
        }
    }

    static class MyGroupingComparator extends WritableComparator{
        public MyGroupingComparator() {
            super(YearSid.class,true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            System.out.println("use GroupingComparator");
            YearSid ays = (YearSid) a;
            YearSid bys = (YearSid) b;
            int cy = ays.getYear().compareTo(bys.getYear());
            int cs = ays.getSid().compareTo(bys.getSid());
            return cy==0?cs:cy;
        }
    }

    static class MySortComparator extends WritableComparator{
        public MySortComparator() {
            super(YearSid.class,true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            System.out.println("use SortComparator");
            YearSid ays = (YearSid) a;
            YearSid bys = (YearSid) b;
            int cy = ays.getYear().compareTo(bys.getYear());
            int cs = ays.getSid().compareTo(bys.getSid());
            return cy==0?cs:cy;
        }
    }

    static class MyReducer extends Reducer<YearSid,DoubleWritable, YearSid,DoubleWritable> {
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

        // 设置分区器
        //job.setPartitionerClass(MyPartitioner.class);

        // 设置分组比较器
        job.setGroupingComparatorClass(MyGroupingComparator.class);

        //设置排序器
        //job.setSortComparatorClass(MySortComparator.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(YearSid.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        job.setNumReduceTasks(2);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Test(),args));
    }
}
