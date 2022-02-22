package hadoop.homework.filebackuporrecovery;

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
 * @package: hadoop.homework
 * @filename: FileBackup.java
 * @create: 2019/09/27 09:39
 * @author: 29314
 * @description: .使用SequenceFile类备份文件实现类
 **/

public class FileBackup {

    public static void main(String[] args) throws IOException {
        new FileBackup().backup(args[0],args[1]);
    }

    public void backup(String path1,String path2) throws IOException {
        SequenceFile.Writer writer = getWriter(path2);
        File file = new File(path1);
        backup(file,writer);
        writer.close();
    }
    public void backup(File file,SequenceFile.Writer writer) throws IOException {
        //判断该文件的文件类型
        Text key = new Text(file.getAbsolutePath()+" ture");
        Text value = new Text("");
        writer.append(key,value);
        writer.sync();
        File[] files = file.listFiles();
        for(File f:files){
            if(f.isFile()){
                Text k = new Text(f.getAbsolutePath()+" false");
                Text v = new Text(getFileMsg(f).toString());
                writer.append(k,v);
            }else if(f.isDirectory()){
                backup(f,writer);
            }
        }
        writer.hflush();
    }

    public StringBuilder getFileMsg(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine())!=null){
            sb.append(line).append(System.getProperty("line.separator"));
        }
        br.close();
        return sb;
    }

    public SequenceFile.Writer getWriter(String path) throws IOException {
        Configuration conf = HDFSUtils.getConf();
        //设置目标文件路径
        SequenceFile.Writer.Option o1 = SequenceFile.Writer.file(new Path(path));
        //设置写入目标文件的key类型
        SequenceFile.Writer.Option o2 = SequenceFile.Writer.keyClass(Text.class);
        //设置写入目标文件的value类型
        SequenceFile.Writer.Option o3 = SequenceFile.Writer.valueClass(Text.class);
        //设置是否要进行压缩,压缩类型
        SequenceFile.Writer.Option o4 = SequenceFile.Writer.compression(SequenceFile.CompressionType.RECORD,new BZip2Codec());
        //获取SequenceFile.Writer对象
        SequenceFile.Writer writer = SequenceFile.createWriter(conf, o1, o2, o3, o4);
        return writer;
    }
}
