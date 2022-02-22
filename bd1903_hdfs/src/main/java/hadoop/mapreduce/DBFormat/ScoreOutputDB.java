package hadoop.mapreduce.DBFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.DBFormat
 * @filename: ScoreOutputDB.java
 * @create: 2019/10/11 18:40
 * @author: 29314
 * @description: .从数据库中提取数据
 **/

public class ScoreOutputDB extends Configured implements Tool {
    static class MyMapper extends Mapper<LongWritable,Score,Text, Text> {
        private Text k2 = new Text();
        private Text v2 = new Text();
        //DecimalFormat df = new DecimalFormat( "0.00");
        @Override
        protected void map(LongWritable key, Score value, Context context) throws IOException, InterruptedException {
            this.k2.set(value.getName());
            StringBuilder sb = new StringBuilder();
            sb.append("语文:").append(value.getChinese()).append(" ");
            sb.append("数学").append(value.getMath()).append(" ");
            sb.append("英语").append(value.getEnglish());
            this.v2.set(sb.toString());
            context.write(this.k2,this.v2);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Path out = new Path(conf.get("out"));

        Job job = Job.getInstance(conf, "成绩数据出库");
        job.setJarByClass(this.getClass());

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(DBInputFormat.class);
        //配置数据库连接信息
        DBConfiguration.configureDB(
                job.getConfiguration(),
                "com.mysql.cj.jdbc.Driver","jdbc:mysql://192.168.112.121:8017/db_test",
                "root","root"
        );
        //配置数据输入信息
        DBInputFormat.setInput(job,Score.class,"score",
                "math>60","math",
                "name","chinese","math","english");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ScoreOutputDB(),args));
    }
}
