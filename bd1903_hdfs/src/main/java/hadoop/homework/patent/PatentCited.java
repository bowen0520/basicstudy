package hadoop.homework.patent;

import hadoop.Util.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.homework
 * @filename: PatentCiteds.java
 * @create: 2019/09/26 20:21
 * @author: 29314
 * @description: .专利被谁引用了
 **/

public class PatentCited {
    private Map<String,Integer> cited;
    Configuration conf;
    FileSystem fs;

    public PatentCited() throws IOException {
        cited = new TreeMap<>();
        conf = HDFSUtils.getConf();
        fs = FileSystem.get(conf);
    }

    public static void main(String[] args) throws IOException {
        PatentCited patentCiteds = new PatentCited();
        patentCiteds.handleMsgs(args[0]);
        System.out.println("文件读取完毕");
        patentCiteds.putMsgs(args[1]);
    }

    public void handleMsgs(String ph) throws IOException {
        Path path = new Path(ph);
        FSDataInputStream fsdis = fs.open(path);
        CompressionCodec code = HDFSUtils.getCode(ph);
        CompressionInputStream cis = code.createInputStream(fsdis);
        BufferedReader br = new BufferedReader(new InputStreamReader(cis));
        String msg = null;
        while ((msg = br.readLine())!=null){
            String[] split = msg.split(",");
            if(cited.containsKey(split[1])){
                cited.put(split[1],cited.get(split[1])+1);
            }else {
                cited.put(split[1],1);
            }
        }
        br.close();
        cis.close();
        fsdis.close();
    }
    public void putMsgs(String ph) throws IOException {
        Path path = new Path(ph);
        fs.mkdirs(path.getParent());
        FSDataOutputStream fsdos = fs.create(path);
        CompressionCodec code = HDFSUtils.getCode(ph);
        CompressionOutputStream cos = code.createOutputStream(fsdos);
        PrintWriter pw = new PrintWriter(cos,true);
        cited.forEach((k,v)->{
            pw.println(k+" "+v);
        });
        pw.close();
        cos.close();
        fsdos.close();
    }
}
