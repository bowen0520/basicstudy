package hbase.option;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 * @program: bd1903.hbase
 * @package: com.briup.bigdata.bd1903.hbase
 * @filename: MyRegionObserver.java
 * @create: 2019.10.30 17:16
 * @author: Kevin
 * @description: .RegionObserver演示
 **/
public class MyRegionObserver implements RegionCoprocessor, RegionObserver{
    private Logger logger=Logger.getLogger(MyRegionObserver.class);

    @Override
    public Optional<RegionObserver> getRegionObserver(){
        return Optional.of(new MyRegionObserver());
    }

    @Override
    public void preGetOp(
        ObserverContext<RegionCoprocessorEnvironment> c,
        Get get,
        List<Cell> result
    ) throws IOException{
        this.logger.info("----开始执行get之前的操作----");
        Cell cell = new KeyValue(
                Bytes.toBytes("1001"),
                Bytes.toBytes("f1"),
                Bytes.toBytes("hight"),
                System.currentTimeMillis(),
                Bytes.toBytes("177.7")
        );
        result.add(cell);
    }

    @Override
    public void postGetOp(
        ObserverContext<RegionCoprocessorEnvironment> c,
        Get get,
        List<Cell> result
    ) throws IOException{
        this.logger.info("----get操作执行结束----");
    }
}
