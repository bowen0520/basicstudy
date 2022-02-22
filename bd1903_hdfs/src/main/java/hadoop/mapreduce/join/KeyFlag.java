package hadoop.mapreduce.join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.join
 * @filename: KeyFlag.java
 * @create: 2019/10/11 14:48
 * @author: 29314
 * @description: .用于标记主键，二次排序时方便同名的数据进入同一组且按照一定顺序
 **/

public class KeyFlag implements WritableComparable<KeyFlag> {
    private Text name;
    private Text flag;

    public KeyFlag() {
        this.name = new Text();
        this.flag = new Text();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyFlag keyFlag = (KeyFlag) o;
        return Objects.equals(name, keyFlag.name) &&
                Objects.equals(flag, keyFlag.flag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, flag);
    }

    @Override
    public int compareTo(KeyFlag o) {
        int a = this.name.compareTo(o.name);
        int b = this.flag.compareTo(o.flag);
        return a==0?b:a;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.name.write(dataOutput);
        this.flag.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.name.readFields(dataInput);
        this.flag.readFields(dataInput);
    }

    public String getName() {
        return name.toString();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFlag() {
        return flag.toString();
    }

    public void setFlag(String flag) {
        this.flag.set(flag);
    }
}

