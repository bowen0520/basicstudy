package hadoop.homework.filebackuporrecovery;

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
 * @package: hadoop.homework
 * @filename: FileRecovery.java
 * @create: 2019/09/27 09:41
 * @author: 29314
 * @description: .使用SequenceFile类来恢复文件实现类
 **/

public class FileRecovery {
    public static void main(String[] args) throws IOException {
        new FileRecovery().recovery(args[0]);
    }

    public void recovery(String path) throws IOException {
        SequenceFile.Reader reader = getReader(path);
        Text key=new Text();
        Text value=new Text();
        while(reader.next(key,value)){
            System.out.print(reader.syncSeen()?"*"+key+"    "+value:" "+key+"   "+value);
            String[] strs = key.toString().split(" ");
            File file = null;
            if("true".equals(strs[1])){
                file = new File(strs[0]);
                file.mkdirs();
            }else if("false".equals(strs[1])){
                file = HDFSUtils.createFile(strs[0]);
                FileWriter fw = new FileWriter(file);
                fw.write(value.toString());
                fw.flush();
                fw.close();
            }
        }
        reader.close();
    }

    public SequenceFile.Reader getReader(String path) throws IOException {
        //获取配置信息
        Configuration conf= HDFSUtils.getConf();
        //指定要读取的序列化文件的路径
        SequenceFile.Reader.Option o1=
                SequenceFile.Reader.file(new Path(path));
        SequenceFile.Reader reader=new SequenceFile.Reader(conf,o1);
        return reader;
    }
}
