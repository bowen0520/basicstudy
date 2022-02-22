package hadoop.serialization;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.writable
 * @filename: StudentWritable.java
 * @create: 2019/09/25 16:14
 * @author: 29314
 * @description: .学生序列化类/除了重写compareable方法还要重写equals和hashCode
 *                  再重写时只需要根据compareable方法中用到的字段【成员变量】重写即可
 **/

public class StudentWritable implements WritableComparable<StudentWritable> {
    private IntWritable id;
    private Text name;
    private double sourse;
    private List<Text> courses;

    public StudentWritable() {
        this.id = new IntWritable();
        this.name = new Text();
        this.courses = new ArrayList<>();
    }

    @Override
    public int compareTo(StudentWritable o) {
        int i = this.id.compareTo(o.id);
        /*if(i==0){
            return this.name.compareTo(o.name);
        }*/
        return i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StudentWritable that = (StudentWritable) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.id.write(dataOutput);
        this.name.write(dataOutput);
        new DoubleWritable(this.sourse).write(dataOutput);
        new IntWritable(this.courses.size()).write(dataOutput);
        for(Text course:courses){
            course.write(dataOutput);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        //先序列化的属性先反序列化
        this.id.readFields(dataInput);
        this.name.readFields(dataInput);
        DoubleWritable doubleWritable = new DoubleWritable();
        doubleWritable.readFields(dataInput);
        this.sourse = doubleWritable.get();
        IntWritable size = new IntWritable();
        size.readFields(dataInput);
        this.courses.clear();
        for(int i = 0;i<size.get();i++){
            Text text = new Text();
            text.readFields(dataInput);
            this.courses.add(text);
        }
    }

    public IntWritable getId() {
        return id;
    }

    public void setId(IntWritable id) {
        this.id = id;
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public double getSourse() {
        return sourse;
    }

    public void setSourse(double sourse) {
        this.sourse = sourse;
    }

    public List<Text> getCourses() {
        return courses;
    }

    public void setCourses(List<Text> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "StudentWritable{" +
                "id=" + id +
                ", name=" + name +
                ", sourse=" + sourse +
                ", courses=" + courses +
                '}';
    }
}
