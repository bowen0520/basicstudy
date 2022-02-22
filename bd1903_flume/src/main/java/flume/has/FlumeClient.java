package flume.has;

import java.util.Properties;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

/**
 * @program: bd1903.flume
 * @package: com.briup.bigdata.bd1903.flume
 * @filename: FlumeClient.java
 * @create: 2019.10.22 16:00
 * @author: Kevin
 * @description: .自动容错和负载均衡测试
 **/
public class FlumeClient{
    public static void main(String[] args) throws EventDeliveryException{
        Properties prop=new Properties();

        // 设置客户端类型:default、default_loadbalance、default_failover
        prop.setProperty("client.type","default_failover");

        // prop.put("host-selector", "random");

        // 设置客户端要连接的主机，都启动相关的agent
        prop.setProperty("hosts","h1 h2 h3"); // 给主机起别名、逻辑名称

        // 将逻辑主机名和物理地址对应
        prop.setProperty("hosts.h1","bt1:1234");
        prop.setProperty("hosts.h2","bt1:2345");
        prop.setProperty("hosts.h3","bt1:3456");

        RpcClient client=RpcClientFactory.getInstance(prop);

        int x=0;

        while(true){
            Event event=EventBuilder.withBody(("briup"+x++).getBytes());
            client.append(event);

            try{
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
