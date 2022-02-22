package hadoop.test;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.secondarysort
 * @filename: YearSid.java
 * @create: 2019/10/10 14:59
 * @author: 29314
 * @description: .年份和气象站编号的复合键
 **/

public class YearSid implements WritableComparable<YearSid> {
    private Text year;
    private Text sid;

    public YearSid() {
        year = new Text();
        sid = new Text();
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("yearsid.equals");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearSid yearSid = (YearSid) o;
        return Objects.equals(year, yearSid.year) &&
                Objects.equals(sid, yearSid.sid);
    }

    @Override
    public int hashCode() {
        System.out.println("yearsid.hashCode");
        return Objects.hash(year, sid);
    }

    @Override
    public int compareTo(YearSid o) {
        System.out.println("yearsid.compareto");
        int cy = this.year.compareTo(o.year);
        int cs = this.sid.compareTo(o.sid);
        return cy==0?cs:cy;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.year.write(dataOutput);
        this.sid.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.year.readFields(dataInput);
        this.sid.readFields(dataInput);
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

    @Override
    public String toString() {
        return this.year.toString() + "\t" + this.sid.toString();
    }
}
