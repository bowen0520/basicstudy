package hadoop.mapreduce.combiner;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.combiner
 * @filename: WeatherData.java
 * @create: 2019/10/09 16:54
 * @author: 29314
 * @description: .自定义天气数据对象
 **/

public class AvgData implements Writable {
    private DoubleWritable temp;
    private IntWritable num;

    public AvgData() {
        temp = new DoubleWritable();
        num = new IntWritable();
    }

    public double getTemp() {
        return temp.get();
    }

    public void setTemp(double temp) {
        this.temp.set(temp);
    }

    public int getNum() {
        return num.get();
    }

    public void setNum(int num) {
        this.num.set(num);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.temp.write(dataOutput);
        this.num.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.temp.readFields(dataInput);
        this.num.readFields(dataInput);
    }
}
