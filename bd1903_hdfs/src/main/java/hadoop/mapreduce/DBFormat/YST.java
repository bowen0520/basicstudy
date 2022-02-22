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
 * @filename: YST.java
 * @create: 2019/10/11 15:54
 * @author: 29314
 * @description: .对应数据库中的自定义数据类型
 **/

public class YST implements DBWritable, WritableComparable<YST> {
    private Text year;
    private Text sid;
    private DoubleWritable temp;

    public YST() {
        this.year = new Text();
        this.sid = new Text();
        this.temp = new DoubleWritable();
    }

    @Override
    public void write(PreparedStatement preparedStatement) throws SQLException {
        // insert into db_test.tbl_yst(year,sid,temp) values(?,?,?);
        preparedStatement.setString(1,this.year.toString());
        preparedStatement.setString(2,this.sid.toString());
        preparedStatement.setDouble(3,this.temp.get());
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.year.set(resultSet.getString(1));
        this.sid.set(resultSet.getString(2));
        this.temp.set(resultSet.getDouble(3));
    }

    @Override
    public int compareTo(YST o) {
        int cy = this.year.compareTo(o.year);
        int cs = this.sid.compareTo(o.sid);
        int ct = this.temp.compareTo(o.temp);
        return cy==0?(cs==0?ct:cs):cy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YST yst = (YST) o;
        return Objects.equals(year, yst.year) &&
                Objects.equals(sid, yst.sid) &&
                Objects.equals(temp, yst.temp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, sid, temp);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.year.write(dataOutput);
        this.sid.write(dataOutput);
        this.temp.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.year.readFields(dataInput);
        this.sid.readFields(dataInput);
        this.temp.readFields(dataInput);
    }

    public String getYear() {
        return year.toString();
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getSid() {
        return sid.toString();
    }

    public void setSid(String sid) {
        this.sid.set(sid);
    }

    public double getTemp() {
        return temp.get();
    }

    public void setTemp(double temp) {
        this.temp.set(temp);
    }
}
