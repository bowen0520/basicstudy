package flume.sink;

import com.google.common.collect.ImmutableMap;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

/**
 * @program: bd1903_flume
 * @package: flume.sink
 * @filename: MyHdfsSink.java
 * @create: 2019/10/21 14:44
 * @author: 29314
 * @description: .自定义hdfssink
 **/

public class MyHdfsSink extends AbstractSink implements Configurable {


    @Override
    public Status process() throws EventDeliveryException {
        //获取channel对象
        Channel ch = this.getChannel();

        //获取transaction对象
        Transaction transaction = ch.getTransaction();

        //开启事务i
        transaction.begin();

        //获取event
        Event event = ch.take();

        System.out.println("event_object:"+event);

        transaction.commit();
        while(true){

        }
        //return Status.READY;
    }

    @Override
    public void configure(Context context) {
        ImmutableMap<String, String> parameters = context.getParameters();
        parameters.forEach((k,v)->{
            System.out.println(k+" "+v);
        });
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public synchronized void stop() {
        super.stop();
    }
}
