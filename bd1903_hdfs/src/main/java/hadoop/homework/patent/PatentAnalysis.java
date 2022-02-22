package hadoop.homework.patent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.homework
 * @filename: Homework.java
 * @create: 2019/09/25 18:48
 * @author: 29314
 * @description: .专利分析实现
 **/

public class PatentAnalysis {
    Map<String, Set<String>> cite = new HashMap<>();
    //Map<String, Set<String>> cited = new HashMap<>();
    Map<String, Integer> cited = new HashMap<>();

    public static void main(String[] args) throws IOException {
        PatentAnalysis patentAnalysis = new PatentAnalysis();
        patentAnalysis.getMsgs(args[0]);
        patentAnalysis.handleMsgs();
        //patentAnalysis.handleMsgs("/home/when/patent/cite.txt",patentAnalysis.cite,"：引用了","项专利，分别是：");
        //patentAnalysis.handleMsgs("/home/when/patent/cited.txt",patentAnalysis.cited,"：被引用了","次，引用了该专利的为：");
        patentAnalysis.putMsgs("/home/when/patent/cite.txt","/patent/cite.txt.bz2");
        patentAnalysis.putMsgs("/home/when/patent/cited.txt","/patent/cited.txt.bz2");
    }

    public Configuration getConf() {
        //获取配置对象
        Configuration conf = new Configuration();

        //设置配置信息
        conf.set("fs.defaultFS","hdfs://192.168.112.120:9000");
        conf.set("dfs.blocksize","32");
        conf.set("dfs.replication","1");

        return conf;
    }

    public void getMsgs(String url) throws IOException {
        FileSystem fs = FileSystem.get(getConf());
        Path path = new Path(url);
        FSDataInputStream fsdis = fs.open(path);

        CompressionCodecFactory codecFactory = new CompressionCodecFactory(getConf());
        CompressionCodec codec = codecFactory.getCodec(path);
        CompressionInputStream cis = codec.createInputStream(fsdis);

        BufferedReader br = new BufferedReader(new InputStreamReader(cis));
        String msg = null;
        while((msg=br.readLine())!=null){
            String[] ss = msg.split(",");
            if(cite.containsKey(ss[0])){
                cite.get(ss[0]).add(ss[1]);
            }else{
                Set<String> set = new HashSet<>();
                set.add(ss[1]);
                cite.put(ss[0],set);
            }

            if(cited.containsKey(ss[1])){
                cited.put(ss[1],cited.get(ss[1])+1);
            }else{
                cited.put(ss[1],1);
            }

            /*if(cited.containsKey(ss[1])){
                cited.get(ss[1]).add(ss[0]);
            }else{
                Set<String> set = new HashSet<>();
                set.add(ss[0]);
                cited.put(ss[1],set);
            }*/
        }
    }

    public File getFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            File parent = new File(file.getParent());
            if(!parent.exists()){
                parent.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    public void handleMsgs() throws IOException {
        File file1 = getFile("/home/when/patent/cite.txt");
        PrintWriter pw1 = new PrintWriter(new FileOutputStream(file1),true);
        cite.forEach((o1,o2)->{
            pw1.print(o1+"：引用了"+o2.size()+"项专利，分别是：");
            o2.forEach(o->{
                pw1.print(o+",");
            });
            pw1.println();
        });
        pw1.flush();
        pw1.close();

        File file2 = getFile("/home/when/patent/cited.txt");
        PrintWriter pw2 = new PrintWriter(new FileOutputStream(file2),true);
        cited.forEach((o1,o2)->{
            pw2.println(o1+"::"+o2);
        });
        pw2.flush();
        pw2.close();
    }

    public void handleMsgs(String path,Map<String,Set<String>> map, String word1,String word2) throws IOException {
        File file = getFile(path);
        PrintWriter pw = new PrintWriter(new FileOutputStream(file),true);
        map.forEach((o1,o2)->{
            pw.print(o1+word1+o2.size()+word2);
            o2.forEach(o->{
                pw.print(o+",");
            });
            pw.println();
        });
        pw.flush();
        pw.close();
    }

    public void putMsgs(String path1,String path2) throws IOException {
        FileSystem fs= FileSystem.get(getConf());
        Path path = new Path(path2);
        fs.mkdirs(path.getParent());
        FSDataOutputStream fsdos = fs.create(path);
        CompressionCodecFactory codecFactory = new CompressionCodecFactory(getConf());
        CompressionCodec codec = codecFactory.getCodec(path);
        CompressionOutputStream cos = codec.createOutputStream(fsdos);
        FileInputStream fis = new FileInputStream(path1);
        IOUtils.copyBytes(fis,cos,1024,true);
    }
}
