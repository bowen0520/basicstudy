package hadoop.mapreduce.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.mapreduce.secondarysort
 * @filename: MyComparator.java
 * @create: 2019/10/10 15:43
 * @author: 29314
 * @description: .自定义分组比较器
 **/

public class MyComparator extends WritableComparator {
    public MyComparator() {
        super(YearSid.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        YearSid ays = (YearSid) a;
        YearSid bys = (YearSid) b;
        int cy = ays.getYear().compareTo(bys.getYear());
        int cs = ays.getSid().compareTo(bys.getSid());
        return cy==0?cs:cy;
    }
}
