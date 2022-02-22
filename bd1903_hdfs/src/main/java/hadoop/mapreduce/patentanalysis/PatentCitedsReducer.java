package hadoop.mapreduce.patentanalysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.reducer
 * @filename: PatentCitesReducer.java
 * @create: 2019/09/26 16:46
 * @author: 29314
 * @description: .分析专利被引用了多少次Reducer端
 **/

public class PatentCitedsReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    private Text k3 = new Text();
    private IntWritable v3 = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        Iterator<IntWritable> iterator = values.iterator();
        while(iterator.hasNext()){
            IntWritable v2 = iterator.next();
            count += v2.get();
        }
        this.k3.set(key.toString());
        this.v3.set(count);
        context.write(this.k3,this.v3);
    }
}
