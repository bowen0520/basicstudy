package hadoop.sequencefile;

import hadoop.Util.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.sequencefile
 * @filename: SequenceFileRead.java
 * @create: 2019/09/26 22:02
 * @author: 29314
 * @description: .序列化文件的读操作
 **/

public class SequenceFileRead {
    public static void main(String[] args) throws IOException {
        new SequenceFileRead().read(args[0]);
    }

    public void read(String path) throws IOException {
        //获取配置信息
        Configuration conf= HDFSUtils.getConf();
        //指定要读取的序列化文件的路径
        SequenceFile.Reader.Option o1=
                SequenceFile.Reader.file(new Path(path));

        SequenceFile.Reader reader=new SequenceFile.Reader(conf,o1);

        Text key=new Text();
        Text value=new Text();

        // 1.判断该序列文件中有没有下一个能够读取的Record；
        // 2.如果有下一个能够读取的record，则读取该Record，
        // 并将其Key和Value分别赋值给参数中的key和value；
        while(reader.next(key,value)){
            System.out.print(reader.syncSeen()?"*"+key+"    "+value:" "+key+"   "+value);
            File file = HDFSUtils.createFile(key.toString());
            FileWriter fw = new FileWriter(file);
            fw.write(value.toString());
            fw.flush();
            fw.close();
        }
        reader.close();
    }
}
