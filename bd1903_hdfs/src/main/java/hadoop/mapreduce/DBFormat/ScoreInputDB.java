package hadoop.mapreduce.DBFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Arrays;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.DBFormat
 * @filename: ScoreInputDB.java
 * @create: 2019/10/11 17:29
 * @author: 29314
 * @description: .成绩数据入库
 **/

public class ScoreInputDB extends Configured implements Tool {
    static class MyMapper extends Mapper<Text,Text,Score,NullWritable> {
        private Score k2 = new Score();
        private NullWritable v2 = NullWritable.get();
        //DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            this.k2.setName(key.toString());
            String[] ss = value.toString().split(" ");
            System.out.println(Arrays.toString(ss));
            double chinese = Double.parseDouble(ss[0].split(":")[1]);
            double math = Double.parseDouble(ss[1].split(":")[1]);
            double english = Double.parseDouble(ss[2].split(":")[1]);
            this.k2.setChinese(chinese);
            this.k2.setMath(math);
            this.k2.setEnglish(english);
            System.out.println(key.toString()+"成绩："+chinese+" "+math+" "+english);
            context.write(this.k2,this.v2);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path in = new Path(conf.get("in"));

        Job job = Job.getInstance(conf, "成绩数据入库");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Score.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        KeyValueTextInputFormat.addInputPath(job,in);

        job.setOutputKeyClass(Score.class);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputFormatClass(DBOutputFormat.class);
        //配置数据库连接信息
        DBConfiguration.configureDB(
                job.getConfiguration(),
                "com.mysql.cj.jdbc.Driver","jdbc:mysql://192.168.112.121:8017/db_test",
                "root","root"
        );
        //配置数据输出信息
        DBOutputFormat.setOutput(job,"score","name","chinese","math","english");

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ScoreInputDB(),args));
    }
}
