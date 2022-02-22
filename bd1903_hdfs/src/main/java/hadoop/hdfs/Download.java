package hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.hdfs
 * @filename: Download.java
 * @create: 2019/11/25 19:29
 * @author: 29314
 * @description: .下载hdfs文件系统中的文件
 **/

public class Download {
    public FileSystem getConf(String hdfsUrl) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS",hdfsUrl);
        return FileSystem.get(conf);
    }

    public void download(String srcPath,String dstPath,FileSystem fs) throws IOException {
        fs.copyToLocalFile(new Path(srcPath),new Path(dstPath));
    }

    public void listFile(String path,FileSystem fs) throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(new Path(path));
        for(FileStatus files:fileStatuses){
            System.out.println(files.getPath()+" "+files.isDirectory());
        }
    }

    public static void main(String[] args) throws IOException {
        Download download = new Download();
        FileSystem conf = download.getConf("hdfs://172.16.0.4:9000");
        //download.listFile("/data/grouplens/ml-1m.zip",conf);
        download.download("/data/grouplens/ml-1m.zip","E:\\briup\\Spark\\spark",conf);
    }
}
