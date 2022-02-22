package hadoop.compression;

import hadoop.Util.HDFSUtils;
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
 * @package: hadoop.compression
 * @filename: TestCompression.java
 * @create: 2019/09/25 14:29
 * @author: 29314
 * @description: .测试文件的压缩与解压缩
 **/

public class TestCompression {
    public static void main(String[] args) throws IOException {
        if(args.length==3){
            String operation = args[0];
            String path1 = args[1];
            String path2 = args[2];
            if(operation.equalsIgnoreCase("uncompression")){
                HDFSUtils.uncompressionGet(path1,path2);
            }else if(operation.equalsIgnoreCase("compression")){
                HDFSUtils.compressionPut(path1,path2);
            }else {
                System.out.println("operation error");
            }
        }else {
            System.out.println("参数不完整");
        }
    }

    public void compression(String path1,String path2,Configuration conf) throws IOException {
        //获取hdfs文件系统对象
        FileSystem fs= FileSystem.get(conf);
        //获取文件系统对象中文件的流
        Path path = new Path(path2);
        fs.mkdirs(path.getParent());
        FSDataOutputStream fsdos = fs.create(path);

        //上传文件需要压缩，需要编码器
        //1. CompressionCodecFactory通过工厂模式会根据上传
        //的文件扩展名自动获取相应的编码器

        //要将数据进行压缩，需要使用编码器对流压缩
        //工厂获取编码器
        CompressionCodecFactory codecFactory = new CompressionCodecFactory(conf);
        CompressionCodec codec = codecFactory.getCodec(path);
        CompressionOutputStream cos = codec.createOutputStream(fsdos);
        //直接获取编码器
        //1.
        //GzipCodec gzipCodec = new GzipCodec();
        //CompressionOutputStream cos = gzipCodec.createOutputStream(fsdos);
        //2.
        //BZip2Codec bZip2Codec = new BZip2Codec();
        //CompressionOutputStream cos = bZip2Codec.createOutputStream(fsdos);

        FileInputStream fis = new FileInputStream(path1);
        IOUtils.copyBytes(fis,cos,1024,true);
    }

    public void uncompression(String path1,String path2,Configuration conf) throws IOException {
        //获取hdfs文件系统对象
        FileSystem fs= FileSystem.get(conf);
        //获取文件系统对象中文件的流
        Path path = new Path(path1);
        FSDataInputStream fsdis = fs.open(path);

        //上传文件需要压缩，需要编码器
        //1. CompressionCodecFactory通过工厂模式会根据上传
        //的文件扩展名自动获取相应的编码器

        //要将数据进行压缩，需要使用编码器对流压缩
        //工厂获取编码器
        CompressionCodecFactory codecFactory = new CompressionCodecFactory(conf);
        CompressionCodec codec = codecFactory.getCodec(path);
        CompressionInputStream cis = codec.createInputStream(fsdis);
        //直接获取编码器
        //1.
        //GzipCodec gzipCodec = new GzipCodec();
        //CompressionInputStream cis = gzipCodec.createInputStream(fsdis);
        //2.
        //BZip2Codec bZip2Codec = new BZip2Codec();
        //CompressionInputStream cis = bZip2Codec.createInputStream(fsdis);
        File file = HDFSUtils.createFile(path2);
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copyBytes(cis,fos,1024,true);
    }
}
