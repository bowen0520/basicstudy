package flume.has;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.util.Properties;

/**
 * @program: bd1903_flume
 * @package: flume.has
 * @filename: FlumeTest.java
 * @create: 2019/10/22 16:01
 * @author: 29314
 * @description: .自动容错，负载均衡
 **/

public class FlumeTest {
    public static void main(String[] args) throws EventDeliveryException {
        Properties prop = new Properties();

        //设置客户端类型：defa5'  ult，loadbalance,default_failover
        prop.setProperty("client.type","default");

        //设置客户端连接的主机：开启了agent的主机
        //给主机起别名，逻辑名称
        prop.setProperty("hosts","h1 h2 h3");


        //将逻辑地址与物理地址对应
        prop.setProperty("hosts.h1","192.168.112.122:1234");
        prop.setProperty("hosts.h2","192.168.112.122:1235");
        prop.setProperty("hosts.h3","192.168.112.122:1236");

        RpcClient client = RpcClientFactory.getInstance(prop);

        int x = 0;
        while(true) {
            Event event = EventBuilder.withBody(("briup"+x++).getBytes());
            client.append(event);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
