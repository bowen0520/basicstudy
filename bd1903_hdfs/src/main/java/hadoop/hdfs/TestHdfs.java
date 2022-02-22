package hadoop.hdfs;

import hadoop.Util.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.hdfs
 * @filename: TestHdfs.java
 * @create: 2019/09/24 23:11
 * @author: 29314
 * @description: .hdfs文件系统操作
 **/

public class TestHdfs {
    public static void main(String[] args) throws IOException {
        if(args.length==3){
            String operation = args[0];
            String path1 = args[1];
            String path2 = args[2];
            TestHdfs hdfs = new TestHdfs();
            if(operation.equalsIgnoreCase("put")){
                hdfs.putFile(path1,path2);
            }else if(operation.equalsIgnoreCase("get")){
                hdfs.getFile(path1,path2);
            }else {
                System.out.println("operation error");
            }
        }else {
            System.out.println("参数不完整");
        }
    }

    public FileSystem getFS() throws IOException {
        //获取配置对象
        Configuration conf = new Configuration();

        //设置配置信息
        conf.set("fs.defaultFS","hdfs://192.168.112.120:9000");
        conf.set("dfs.blocksize","32M");
        conf.set("dfs.replication","1");

        //获取hdfs文件系统对象
        FileSystem fs= FileSystem.get(conf);
        return fs;
    }

    public void putFile(String path1,String path2) throws IOException {
        FileSystem fs = getFS();
        //创建文件
        Path path = new Path(path2);
        fs.mkdirs(path.getParent());
        FSDataOutputStream fsdos = fs.create(path);

        FileInputStream fis = new FileInputStream(new File(path1));
        //边读边写
        IOUtils.copyBytes(fis,fsdos,1024,true);
        // winutils.exe
        // 1.在windows中安装hadoop并配置环境变量
        // 2.将文件winutils.exe拷贝至hadoop安装目录的bin目录下
    }

    public void getFile(String path1,String path2) throws IOException {
        FileSystem fs = getFS();
        Path path = new Path(path1);
        FSDataInputStream fsdis = fs.open(path);

        File file = HDFSUtils.createFile(path2);
        FileOutputStream fos = new FileOutputStream(file);

        //边读边写
        IOUtils.copyBytes(fsdis,fos,1024,true);
    }
}
