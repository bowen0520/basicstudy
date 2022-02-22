package hadoop.Util;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.Util
 * @filename: CreateFiles.java
 * @create: 2019/09/26 22:07
 * @author: 29314
 * @description: .创建一堆小文件
 **/

public class CreateFiles {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        if(!file.exists()) {
            file.mkdirs();
        }
        int nums = Integer.parseInt(args[1]);
        for(int i = 1;i<=nums;i++){
            File f = new File(file,i+".txt");
            f.createNewFile();
            PrintWriter  pw = new PrintWriter(new FileWriter(f));
            pw.println(i);
            pw.close();
        }
    }
}
