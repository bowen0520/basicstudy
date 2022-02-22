package hadoop.sequencefile;

import hadoop.Util.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.sequencefile
 * @filename: SequenceFileWrite.java
 * @create: 2019/09/26 22:01
 * @author: 29314
 * @description: .序列化文件的写操作
 **/

public class SequenceFileWrite {
    public static void main(String[] args) throws IOException {
        new SequenceFileWrite().write(args[0],args[1]);
    }

    public void write(String path1,String path2) throws IOException {
        Configuration conf = HDFSUtils.getConf();
        //使用SequenceFile对象的createWriter（）静态方法获取对象
        //createWriter方法需要以下参数
        //1.Configuration对象
        //2.类型为SequenceFile.Writer.Option的可变长参数列表
        //在生成序列化文件时，必须使用Option对象来表示
        // 生成的文件路径，key的数据类型，value的数据类型
        //还可以表示是否进行压缩，以及压缩级别；

        //设置目标文件路径
        SequenceFile.Writer.Option o1 = SequenceFile.Writer.file(new Path(path2));
        //设置写入目标文件的key类型
        SequenceFile.Writer.Option o2 = SequenceFile.Writer.keyClass(Text.class);
        //设置写入目标文件的value类型
        SequenceFile.Writer.Option o3 = SequenceFile.Writer.valueClass(Text.class);
        //设置是否要进行压缩,压缩类型
        SequenceFile.Writer.Option o4 = SequenceFile.Writer.compression(SequenceFile.CompressionType.RECORD,new BZip2Codec());
        //获取SequenceFile.Writer对象
        SequenceFile.Writer writer = SequenceFile.createWriter(conf, o1, o2, o3, o4);

        //获取所有小文件
        File dir = new File(path1);
        File[] files = dir.listFiles();
        //如果没有就直接返回
        if(files==null||files.length==0){
            return;
        }
        int x = 1;
        //将每个文件作为一个kv记录写入
        for(File file:files){
            //获取key
            Text key = new Text(file.getAbsolutePath());
            //获取value
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine())!=null){
                sb.append(line).append(System.getProperty("line.separator"));
            }
            Text value = new Text(sb.toString());
            if(x++%10==0){
                // 能够向序列文件中写入一个同步标记
                writer.sync();
            }
            //写入序列化文件
            writer.append(key,value);
            writer.hflush();
            br.close();
        }
        writer.close();
    }
}
