package kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * @program: bd1903_hadooptools
 * @package: kafka
 * @filename: MyProducer.java
 * @create: 2019/10/25 14:24
 * @author: 29314
 * @description: .自定义生产者
 **/

public class MyProducer {
    public static void main(String[] args) throws InterruptedException {
        //使用properties类配置各种属性
        Properties properties = new Properties();
        properties.setProperty("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("bootstrap.servers",
                "192.168.112.121:9092,192.168.112.122:9092");
        //properties.setProperty("topic","test4");

        //通过配置获取生产者对象
        Producer<String,String> producer = new KafkaProducer<>(properties);

        //循环生产
        int count = 0;
        while(count<10000) {
            //创建记录
            ProducerRecord<String, String> record = new ProducerRecord<>("test4", "count"+count);
            //同步生产
            //System.out.println("record:"+record);
            //producer.send(record);
            //异步生产
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        int partition = recordMetadata.partition();
                        long offset = recordMetadata.offset();
                        long timestamp = recordMetadata.timestamp();
                        System.out.println(">>>topic=" + recordMetadata.topic() + ",partition=" + partition + ",timestamp=" + timestamp + ",offset=" + offset + ",value=" + record.value());
                    }
                }
            });
            count++;
            if(count%10==0) {
                System.out.println("发送100条");
                producer.flush();
            }
            Thread.sleep(1000);
        }
        producer.flush();
        producer.close();
    }
}
