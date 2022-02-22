package hadoop.Util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.GzipCodec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.Util
 * @filename: HDFSUtils.java
 * @create: 2019/09/26 18:02
 * @author: 29314
 * @description: .关于hdfs的工具
 **/

public class HDFSUtils {
    public static Configuration getConf() {
        //获取配置对象
        Configuration conf = new Configuration();
        //设置配置信息
        conf.set("fs.defaultFS","hdfs://192.168.112.120:9000");
        conf.set("dfs.blocksize","32M");
        conf.set("dfs.replication","1");
        return conf;
    }

    public static File createFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            File parent =new File(file.getParent());
            if(!parent.exists()){
                parent.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    public static CompressionCodec getCode(String codeType){
        CompressionCodec codec = null;
        if(codeType.equalsIgnoreCase("gz")){
            codec = new GzipCodec();
        }else if(codeType.equalsIgnoreCase("bz2")){
            codec = new BZip2Codec();
        }else{
            CompressionCodecFactory codecFactory = new CompressionCodecFactory(getConf());
            codec = codecFactory.getCodec(new Path(codeType));
        }
        return codec;
    }

    public static void putFile(String path1,String path2) throws IOException {
        //获取源文件的输入流
        FileInputStream fis = new FileInputStream(new File(path1));
        //获取文件系统
        FileSystem fs = FileSystem.get(getConf());
        //创建目标文件对象
        Path path = new Path(path2);
        fs.mkdirs(path.getParent());
        //获取目标文件输出流
        FSDataOutputStream fsdos = fs.create(path);
        //边读边写
        IOUtils.copyBytes(fis,fsdos,1024,true);
    }

    public static void getFile(String path1,String path2) throws IOException {
        //获取文件系统
        FileSystem fs = FileSystem.get(getConf());
        //获取源文件的输入流
        Path path = new Path(path1);
        FSDataInputStream fsdis = fs.open(path);
        //创建目标文件
        File file = createFile(path2);
        //获取目标文件输出流
        FileOutputStream fos = new FileOutputStream(file);
        //边读边写
        IOUtils.copyBytes(fsdis,fos,1024,true);
    }

    public static void compressionPut(String path1,String path2) throws IOException {
        compressionPut(path1,path2,path2);
    }

    public static void compressionPut(String path1,String path2,String codeType) throws IOException {
        //获取源文件输入流
        FileInputStream fis = new FileInputStream(path1);
        //获取hdfs文件系统对象
        FileSystem fs= FileSystem.get(getConf());
        //创建目标文件对象
        Path path = new Path(path2);
        fs.mkdirs(path.getParent());
        //获取目标文件输出流
        FSDataOutputStream fsdos = fs.create(path);
        //获取对应的编码器
        CompressionCodec codec = getCode(codeType);
        //用编码器将目标文件的输出流进行包装
        CompressionOutputStream cos = codec.createOutputStream(fsdos);
        //边读边写
        IOUtils.copyBytes(fis,cos,1024,true);
    }

    public static void uncompressionGet(String path1,String path2) throws IOException {
        uncompressionGet(path1,path2,path1);
    }

    public static void uncompressionGet(String path1,String path2,String codeType) throws IOException {
        //获取hdfs文件系统对象
        FileSystem fs= FileSystem.get(getConf());
        //获取源文件对象输入流
        Path path = new Path(path1);
        FSDataInputStream fsdis = fs.open(path);
        //获取对应的解码器
        CompressionCodec codec = getCode(codeType);
        //使用解码器对源文件的输入流进行包装
        CompressionInputStream cis = codec.createInputStream(fsdis);
        //创建目标文件对象
        File file = createFile(path2);
        //获取目标文件输出流
        FileOutputStream fos = new FileOutputStream(file);
        //边读边写
        IOUtils.copyBytes(cis,fos,1024,true);
    }
}
