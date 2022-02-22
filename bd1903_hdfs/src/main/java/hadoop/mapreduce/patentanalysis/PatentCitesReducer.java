package hadoop.mapreduce.patentanalysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @program: bd1903_hdfs
 * @package: hadoop.reducer
 * @filename: PatentCitesReducer.java
 * @create: 2019/09/26 16:46
 * @author: 29314
 * @description: .分析专利引用了哪些专利的Reducer端
 **/

public class PatentCitesReducer extends Reducer<Text,Text,Text,Text> {
    private Text k3 = new Text();
    private Text v3 = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        values.forEach(o->{
            sb.append(o.toString()).append(",");
        });
        this.k3.set(key.toString());
        this.v3.set(sb.substring(0,sb.length()-1));
        context.write(this.k3,this.v3);
    }
}
