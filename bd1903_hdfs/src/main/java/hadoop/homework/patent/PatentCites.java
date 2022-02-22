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
 * @description: .专利引用了什么专利
 **/

public class PatentCites {
    private Map<String,StringBuilder> cites;
    Configuration conf;
    FileSystem fs;

    public PatentCites() throws IOException {
        cites = new TreeMap<>();
        conf = HDFSUtils.getConf();
        fs = FileSystem.get(conf);
    }

    public static void main(String[] args) throws IOException {
        PatentCites patentCiteds = new PatentCites();
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
            if(cites.containsKey(split[0])){
                cites.get(split[0]).append(split[1]).append(",");
            }else {
                cites.put(split[0],new StringBuilder());
                cites.get(split[0]).append(split[1]).append(",");
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

        cites.forEach((k,v)->{
            pw.println(k+" "+v.substring(0,v.length()-1));
        });
        pw.close();
        cos.close();
        fsdos.close();
    }
}
