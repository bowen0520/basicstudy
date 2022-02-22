package com.briup.DataIntegration;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

/**
 * @program: bd1903_search_engine
 * @package: com.briup.DataIntegration
 * @filename: DataGet.java
 * @create: 2019/11/15 12:54
 * @author: 29314
 * @description: .获取url，rank，关键字这些有用信息
 **/

public class DataGet extends Configured implements Tool {

    static class DGMapper extends TableMapper<Text, Text> {
        Text k2 = new Text();
        Text v2 = new Text();
        @Override
        protected void map(ImmutableBytesWritable key, Result value,
                           Context context) throws IOException, InterruptedException {
            //获取当前url
            String mainurl = Bytes.toString(key.get());
            //获取rank值
            byte[] ranks = value.getValue(Bytes.toBytes("r"), Bytes.toBytes("rank"));
            String rank = Bytes.toString(ranks);

            k2.set(mainurl);
            v2.set(rank+"r");
            context.write(k2,v2);
            //获取关键字
            byte[] title = value.getValue(Bytes.toBytes("p"), Bytes.toBytes("t"));
            if(title!=null){
                String keyword = Bytes.toString(title);
                v2.set(keyword+"k");
                context.write(k2,v2);
            }
            //获取出链接
            /*NavigableMap<byte[], NavigableMap<byte[], byte[]>> noVersionMap =
                    value.getNoVersionMap();
            NavigableMap<byte[], byte[]> ol = noVersionMap.get(Bytes.toBytes("ol"));*/

            NavigableMap<byte[], byte[]> ol = value.getFamilyMap(Bytes.toBytes("ol"));
            if (ol!=null&&ol.size() != 0) {
                for (Entry<byte[],byte[]> cn : ol.entrySet()) {
                    k2.set(Bytes.toString(cn.getKey()));
                    v2.set(Bytes.toString(cn.getValue())+"k");
                    context.write(k2, v2);
                }
            }
        }
    }

    public static class DGReducer extends TableReducer<Text,Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<Text> values,
                              Context context) throws IOException, InterruptedException {
            byte[] url = Bytes.toBytes(key.toString());
            double rank = 0;
            List<Put> puts = new ArrayList<>();
            for (Text value : values) {
                String v = value.toString();
                String vv = v.substring(0,v.length()-1);
                if(v.endsWith("r")){
                    double num = Double.parseDouble(vv);
                    rank = num;
                }else if(v.endsWith("k")){
                    if(!"".equals(vv.trim())) {
                        Put put = new Put(Bytes.toBytes(vv));
                        puts.add(put);
                    }
                }
            }
            for(Put p:puts){
                p.addColumn(Bytes.toBytes("page"),url,
                        System.currentTimeMillis(),Bytes.toBytes(rank+"##test"));
                context.write(NullWritable.get(),p);
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Job job = Job.getInstance(conf, "DataGet");
        job.setJarByClass(this.getClass());
        Scan scan = new Scan();
        TableMapReduceUtil.initTableMapperJob("when:tbl_dc", scan, DGMapper.class,
                Text.class, Text.class, job);
        TableMapReduceUtil.initTableReducerJob("when:tbl_dg", DGReducer.class, job);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new DataGet(), args);
    }
}
