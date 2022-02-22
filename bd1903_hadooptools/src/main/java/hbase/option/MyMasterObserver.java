package hbase.option;

import java.io.IOException;
import java.util.Optional;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.RegionInfo;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessor;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.MasterObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.log4j.Logger;

/**
 * @program: bd1903.hbase
 * @package: com.briup.bigdata.bd1903.hbase
 * @filename: MyMasterObserver.java
 * @create: 2019.10.31 14:55
 * @author: Kevin
 * @description: .
 **/
public class MyMasterObserver implements MasterCoprocessor, MasterObserver{
    private Logger logger=Logger.getLogger(MyMasterObserver.class);

    @Override
    public Optional<MasterObserver> getMasterObserver(){
        return Optional.of(new MyMasterObserver());
    }

    @Override
    public void preCreateTable(
        ObserverContext<MasterCoprocessorEnvironment> ctx,
        TableDescriptor desc,
        RegionInfo[] regions
    ) throws IOException{
        this.logger.info("----------开始执行preCreateTable方法------------");

        TableDescriptorBuilder.ModifyableTableDescriptor mtd=
            (TableDescriptorBuilder.ModifyableTableDescriptor)desc;

        ColumnFamilyDescriptor[] cfs=mtd.getColumnFamilies();

        for(ColumnFamilyDescriptor cf: cfs){
            this.logger.info("---------------列族："+new String(cf.getName())+"------------------");

            ColumnFamilyDescriptorBuilder
                .ModifyableColumnFamilyDescriptor mcf=
                    (ColumnFamilyDescriptorBuilder
                        .ModifyableColumnFamilyDescriptor)cf;
            mcf.setMaxVersions(5);
        }
    }
}
