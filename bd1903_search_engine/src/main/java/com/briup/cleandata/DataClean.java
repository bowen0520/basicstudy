package com.briup.cleandata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.NavigableMap;

/**
 * @program: bd1903_search_engine
 * @package: com.briup.cleandata
 * @filename: DataClean.java
 * @create: 2019/11/11 15:48
 * @author: 29314
 * @description: .获取hbase集群上的数据并清洗
 **/

public class DataClean extends Configured implements Tool {
    static class DCMapper extends TableMapper<ImmutableBytesWritable, Put>{

        ImmutableBytesWritable k1 = new ImmutableBytesWritable();
        @Override
        protected void map(ImmutableBytesWritable key, Result value,
                           Context context) throws IOException, InterruptedException {
            //获取f列族，st列的状态码
            byte[] bs = value.getValue(Bytes.toBytes("f"), Bytes.toBytes("st"));
            int status = Bytes.toInt(bs);
            //获取状态为2的那一行信息
            if(status==2){
                //获取url，只获取有效得url得那一行
                String url = getURL(Bytes.toString(key.get()));
                if(url!=null&&!"".equals(url)) {
                    k1.set(Bytes.toBytes(url));
                    Put put = new Put(k1.get());
                    NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = value.getMap();

                    map.forEach((cf, val1) -> {
                        //保存出链接
                        if (Bytes.equals(cf, Bytes.toBytes("ol"))) {
                            val1.forEach((cn, val2) -> {
                                val2.forEach((ts, val) -> {
                                    put.addColumn(cf, cn, ts, val);
                                });
                            });
                        }
                        //保存p中的标题和内容
                        if (Bytes.equals(cf, Bytes.toBytes("p"))) {
                            val1.forEach((cn, val2) -> {
                                if (Bytes.equals(cn, Bytes.toBytes("t"))) {
                                    val2.forEach((ts, val) -> {
                                        put.addColumn(cf, cn, ts, val);
                                    });
                                }
                                if (Bytes.equals(cn, Bytes.toBytes("c"))) {
                                    val2.forEach((ts, val) -> {
                                        put.addColumn(cf, cn, ts, val);
                                    });
                                }
                            });
                        }
                    });
                    //新增一个r:rank列用于后面的pagerank计算
                    put.addColumn(Bytes.toBytes("r"), Bytes.toBytes("rank"), System.currentTimeMillis(), Bytes.toBytes("10"));
                    context.write(k1, put);
                }
            }
        }
        //将顺序乱得url变为有效得url
        //com.aliyun.edu:https/course/explore/programlanguage
        //http://when-virtual-machine:19888/jobhistory/task
        public String getURL(String old){
            String[] split = old.split(":");
            if(split.length==2){
                String[] split0 = split[0].split("\\.");

                int index = split[1].indexOf("/");
                String head = split[1].substring(0,index).trim();
                String end = split[1].substring(index).trim();

                StringBuilder sb = new StringBuilder();
                sb.append(head).append("://");
                for(int i = split0.length-1;i>=0;i--){
                    sb.append(split0[i]).append(".");
                }
                sb.setLength(sb.length()-1);
                sb.append(end);
                return sb.toString();
            }
            return null;
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf=this.getConf();
        String in = conf.get("in");
        String out = conf.get("out");

        Job job=Job.getInstance(conf,"数据清洗");
        job.setJarByClass(this.getClass());

        Scan scan=new Scan();
        TableMapReduceUtil.initTableMapperJob(in,scan,DCMapper.class,ImmutableBytesWritable.class,Put.class,job);
        TableMapReduceUtil.initTableReducerJob(out,null,job);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new DataClean(), args);
    }
}
