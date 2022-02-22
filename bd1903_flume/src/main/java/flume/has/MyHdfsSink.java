package flume.has;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @program: bd1903.flume
 * @package: com.briup.bigdata.bd1903.flume
 * @filename: MyHdfsSink.java
 * @create: 2019.10.21 14:41
 * @author: Kevin
 * @description: .自定义Sink
 **/
public class MyHdfsSink extends AbstractSink implements Configurable{
    private PrintWriter pw;
    private String path;
    private FileSystem fs;
    private List<Event> events;

    // start方法在Sink运行之前只执行一次，主要是对Sink中
    // 的一些变量或者代码进行初始化
    @Override
    public synchronized void start(){
        super.start();
        try{
            Configuration conf=new Configuration();
            this.fs=FileSystem.get(conf);

            Path path=new Path(this.path);
            boolean exists=fs.exists(path);

            if(!exists){
                fs.mkdirs(path);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        this.events=new ArrayList<>();
    }

    // 在Sink执行结束之后，stop方法只执行一次
    @Override
    public synchronized void stop(){
        super.stop();
    }

    // process方法在SinK运行的时候进行调用，
    // 每调用一次该方法，就会获取到一个Event对象，有可能为null
    @Override
    public Status process() throws EventDeliveryException{
        // 获取Channel对象
        Channel ch=this.getChannel();

        // 获取Transaction对象
        Transaction tx=ch.getTransaction();

        // 开启事务
        tx.begin();

        Event event;

        while(true){
            event=ch.take();
            if(event!=null) break;
        }


        this.events.add(event);
        System.out.println(events.size()+"=============");

        if(events.size()==100){
            try{
                FSDataOutputStream fsdos=fs.create(new Path(this.path+"/"+System.currentTimeMillis()));
                this.pw=new PrintWriter(new OutputStreamWriter(fsdos),true);
                events.forEach(event1 -> {
                    byte[] body=event1.getBody();
                    String data=new String(body);
                    this.pw.println(data);
                    this.pw .flush();
                });
                this.pw.close();
                this.events.clear();
                tx.commit();
                return Status.READY;
            }catch(IOException e){
                e.printStackTrace();
                tx.rollback();
                return Status.BACKOFF;
            }finally{
                tx.close();
            }
        }else{
            return Status.BACKOFF;
        }
    }

    // 在Agent.Sink执行的之前获取配置信息
    @Override
    public void configure(Context context){
        ImmutableMap<String,String> params=context.getParameters();
        this.path=context.getString("hdfs.path");
    }
}
