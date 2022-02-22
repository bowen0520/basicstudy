package com.briup.pagerank;

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
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * @program: bd1903_search_engine
 * @package: com.briup.pagerank
 * @filename: PageRank.java
 * @create: 2019/11/12 16:29
 * @author: 29314
 * @description: .使用mapreducer实现pagerank得到网页的权重值
 **/

public class PageRank extends Configured implements Tool {
    static class PRMapper extends TableMapper<ImmutableBytesWritable, DoubleWritable> {
        ImmutableBytesWritable k2 = new ImmutableBytesWritable();
        DoubleWritable v2 = new DoubleWritable();
        @Override
        protected void map(ImmutableBytesWritable key, Result value,
                           Context context) throws IOException, InterruptedException {
            //获取rank值
            byte[] ranks = value.getValue(Bytes.toBytes("r"), Bytes.toBytes("rank"));
            double rank = Double.parseDouble(Bytes.toString(ranks));
            //获取出链接
            NavigableMap<byte[], NavigableMap<byte[], byte[]>> noVersionMap =
                    value.getNoVersionMap();
            NavigableMap<byte[], byte[]> ol = noVersionMap.get(Bytes.toBytes("ol"));
            if (ol!=null&&ol.size() != 0) {
                double avgRank = rank / ol.size();
                v2.set(avgRank);
                //写出出链接对应得rank
                for (byte[] cn : ol.keySet()) {
                    k2.set(cn);
                    context.write(k2, v2);
                }
            }
            k2.set(key.get());
            v2.set(0.0d);
            context.write(k2, v2);
        }
    }

    public static class PRReducer extends TableReducer<ImmutableBytesWritable,DoubleWritable,
            NullWritable> {
        // 障碍因子
        double factor = 0.85d;
        @Override
        protected void reduce(ImmutableBytesWritable key, Iterable<DoubleWritable> values,
                              Context context) throws IOException, InterruptedException {
            double sum = 0.0;
            //将出链接作为rowkey，写入表中
            Put put = new Put(key.get());
            //求出rowkey对应得rank值
            for (DoubleWritable value : values) {
                sum += value.get();
            }
            //将sum进行计算得到有效得rank
            double rank = sum*factor+(1-factor);
            //对double类型得sum进行格式化
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(4);
            format.setRoundingMode(RoundingMode.UP);

            put.addColumn(Bytes.toBytes("r"),Bytes.toBytes("rank"),
                    System.currentTimeMillis(),Bytes.toBytes(format.format(rank)));
            context.write(NullWritable.get(),put);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        for (int i = 1; i < 20; i++) {
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf, "PageRank");
            job.setJarByClass(this.getClass());

            Scan scan = new Scan();
            TableMapReduceUtil.initTableMapperJob("when:tbl_dc", scan, PRMapper.class,
                    ImmutableBytesWritable.class, DoubleWritable.class, job);
            TableMapReduceUtil.initTableReducerJob("when:tbl_dc", PRReducer.class, job);

            job.waitForCompletion(true);
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new PageRank(), args);
    }
}
