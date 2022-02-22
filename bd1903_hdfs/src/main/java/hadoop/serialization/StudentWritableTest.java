package hadoop.serialization;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.writable
 * @filename: StudentWritableTest.java
 * @create: 2019/09/25 16:57
 * @author: 29314
 * @description: .序列化学生类的测试
 **/

public class StudentWritableTest {
    public static void main(String[] args) throws IOException {
        StudentWritable sw = new StudentWritable();
        sw.setId(new IntWritable(1234));
        sw.setName(new Text("zhangsan"));
        sw.setSourse(99.99);
        List<Text> list = new ArrayList<>();
        list.add(new Text("yuwen"));
        list.add(new Text("shuxue"));
        list.add(new Text("yingyu"));
        sw.setCourses(list);

        System.out.println(sw);

        DataOutputStream dos = new DataOutputStream(new FileOutputStream("src/main/file/a.txt"));
        sw.write(dos);

        DataInputStream dis = new DataInputStream(new FileInputStream("src/main/file/a.txt"));
        StudentWritable sw2 = new StudentWritable();
        sw2.readFields(dis);

        System.out.println(sw2);
    }
}
