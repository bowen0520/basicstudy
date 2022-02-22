package hadoop.mapreduce.DBFormat;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.DBFormat
 * @filename: Score.java
 * @create: 2019/10/11 17:16
 * @author: 29314
 * @description: .学生成绩包装类
 **/

public class Score implements DBWritable, WritableComparable<Score> {
    private Text name;
    private DoubleWritable chinese;
    private DoubleWritable math;
    private DoubleWritable english;

    public Score() {
        this.name = new Text();
        this.chinese = new DoubleWritable();
        this.math = new DoubleWritable();
        this.english = new DoubleWritable();
    }

    public String getName() {
        return name.toString();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getChinese() {
        return chinese.get();
    }

    public void setChinese(double chinese) {
        this.chinese.set(chinese);
    }

    public double getMath() {
        return math.get();
    }

    public void setMath(double math) {
        this.math.set(math);
    }

    public double getEnglish() {
        return english.get();
    }

    public void setEnglish(double english) {
        this.english.set(english);
    }

    @Override
    public int compareTo(Score o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return name.equals(score.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.name.write(dataOutput);
        this.chinese.write(dataOutput);
        this.math.write(dataOutput);
        this.english.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.name.readFields(dataInput);
        this.chinese.readFields(dataInput);
        this.math.readFields(dataInput);
        this.english.readFields(dataInput);
    }

    @Override
    public void write(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1,this.name.toString());
        preparedStatement.setDouble(2,this.chinese.get());
        preparedStatement.setDouble(3,this.math.get());
        preparedStatement.setDouble(4,this.english.get());
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.name.set(resultSet.getString(1));
        this.chinese.set(resultSet.getDouble(2));
        this.math.set(resultSet.getDouble(3));
        this.english.set(resultSet.getDouble(4));
    }
}
