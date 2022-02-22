package flume.sink;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @program: flume.pre
 * @package: com.briup.bigdata.bd1903.flume.pre.test
 * @filename: MySink.java
 * @create: 2019.10.21 23:01
 * @author: Kevin
 * @description: .自定义Sink
 **/
public class MySink extends AbstractSink implements Configurable{
    private String path;
    private int count;
    private List<Event> list;
    private FileSystem fs;

    @Override
    public Status process() throws EventDeliveryException{
        System.out.println("......执行了一次process方法......");
        Channel ch=this.getChannel();
        Transaction tx=ch.getTransaction();
        tx.begin();
        try{
            Event event=ch.take();
            System.out.println("...获取到的Event对象是："+event);
            if(event==null){
                // 处理
                tx.commit();  // 意味着事务完成，
                return Status.READY;
            }else{
                this.list.add(event);
            }
            System.out.println("...集合的大小是...："+this.list.size());

            if(this.list.size()==this.count){
                System.out.println("...开始执行if判断...");
                PrintWriter pw=
                    new PrintWriter(
                        new OutputStreamWriter(
                            this.fs.create(
                                new Path(
                                    this.path+"/"+System.currentTimeMillis()))),true);
                this.list.forEach(e->{  // 10
                    String data=new String(e.getBody(),Charset.forName("UTF-8"));
                    pw.println(data);
                    pw.flush();
                });
                pw.close();
                this.list.clear();
            }
            tx.commit();
            return Status.READY;
        }catch(IOException e){
            e.printStackTrace();
            tx.rollback();
            return Status.BACKOFF;
        }finally{
            tx.close();
        }
    }

    @Override
    public void configure(Context context){
        this.path=context.getString("path");
        this.count=context.getInteger("count");
    }

    @Override
    public synchronized void start(){
        System.out.println("Sink运行，Start方法执行。。。");
        super.start();
        try{
            Configuration conf=new Configuration();
            this.fs=FileSystem.get(conf);
            Path path=new Path(this.path);
            if(!this.fs.exists(path)){
                this.fs.mkdirs(path);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        this.list=new ArrayList<>();
    }

    @Override
    public synchronized void stop(){
        super.stop();
    }
}
