package flume.source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;

/**
 * @program: flume.pre
 * @package: com.briup.bigdata.bd1903.flume.pre.test
 * @filename: MySource.java
 * @create: 2019.10.21 21:58
 * @author: Kevin
 * @description: .自定义Source
 **/
public class MySource extends AbstractSource implements Configurable, EventDrivenSource{
    @Override
    public void configure(Context context){

    }

    @Override
    public synchronized void start(){
        try{
            super.start();
            ChannelProcessor cp=this.getChannelProcessor();
            BufferedReader reader=
                new BufferedReader(
                    new InputStreamReader(System.in));
            String line;
            while((line=reader.readLine())!=null){
                Event event=EventBuilder.withBody(line.getBytes());
                cp.processEvent(event);
                // cp.processEventBatch();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void stop(){
        super.stop();
    }
}
