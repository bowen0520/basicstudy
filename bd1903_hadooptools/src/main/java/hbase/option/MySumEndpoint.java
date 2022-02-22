package hbase.option;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.CoprocessorException;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.shaded.protobuf.ResponseConverter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @program: hbase.pre
 * @package: com.briup.bigdata.bd1903.hbase.pre
 * @filename: MySumEndpoint.java
 * @create: 2019.10.31 10:18
 * @author: Kevin
 * @description: .
 **/
public class MySumEndpoint extends Sum.SumService implements Coprocessor, CoprocessorService{
    private RegionCoprocessorEnvironment env;

    @Override
    public Service getService(){
        return this;
    }

    @Override
    public void getSum(RpcController controller,Sum.SumRequest request,RpcCallback<Sum.SumResponse> done){
        Scan scan=new Scan();
        scan.addFamily(Bytes.toBytes(request.getFamily()));
        scan.addColumn(Bytes.toBytes(request.getFamily()),Bytes.toBytes(request.getColumn()));
        Sum.SumResponse response=null;
        try(InternalScanner scanner=env.getRegion().getScanner(scan)){

            List<Cell> results=new ArrayList<>();
            boolean hasMore;
            long sum=0L;
            do{
                hasMore=scanner.next(results);
                for(Cell cell: results){
                    sum=sum+Bytes.toLong(CellUtil.cloneValue(cell));
                }
                results.clear();
            }while(hasMore);
            response=Sum.SumResponse.newBuilder().setSum(sum).build();
        }catch(IOException ioe){
            ResponseConverter.setControllerException(controller,ioe);
        }
        done.run(response);
    }

    @Override
    public void start(CoprocessorEnvironment env) throws IOException{
        if(env instanceof RegionCoprocessorEnvironment){
            this.env=(RegionCoprocessorEnvironment)env;
        }else{
            throw new CoprocessorException("Must be loaded on a table region!");
        }
    }

    @Override
    public void stop(CoprocessorEnvironment env){
        // do nothing
    }

    @Override
    public Iterable<Service> getServices(){
        return null;
    }
}
