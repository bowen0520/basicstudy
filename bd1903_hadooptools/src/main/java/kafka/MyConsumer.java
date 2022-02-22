package kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @program: bd1903_hadooptools
 * @package: kafka
 * @filename: MyConsumer.java
 * @create: 2019/10/25 14:55
 * @author: 29314
 * @description: .自定义消费者
 **/

public class MyConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("bootstrap.servers",
                "192.168.112.121:9092,192.168.112.122:9092");
        properties.setProperty("group.id","group1");

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        //订阅topic
        consumer.subscribe(Collections.singleton("test4"));

        //读取record将record写出到HDFS集群中
        //先做缓存操作，然后一次性写出去
        List<ConsumerRecord<String,String>> list = new ArrayList<>();
        int count = 0;
        //获取消息
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            Iterator<ConsumerRecord<String,String>> iterator=records.iterator();
            while(iterator.hasNext()){
                ConsumerRecord<String,String> record=iterator.next();
                //System.out.println(">>>"+record);
                list.add(record);
                if(count++>=10){
                    try {
                        Configuration conf = new Configuration();
                        conf.set("fs.defaultFS","192.168.112.120:9000");
                        FileSystem fs = FileSystem.get(conf);
                        Path parent = new Path("/file/kafka/kfk1");
                        if(!fs.exists(parent)){
                            fs.mkdirs(parent);
                        }
                        FSDataOutputStream fsdos = fs.create(new Path(parent, System.currentTimeMillis() + ".txt"));
                        PrintWriter pw = new PrintWriter(fsdos,true);
                        list.forEach(o->pw.println(o.value()));
                        pw.flush();
                        pw.close();
                        list.clear();
                        count=0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
