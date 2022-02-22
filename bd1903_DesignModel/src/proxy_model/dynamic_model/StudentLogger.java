package proxy_model.dynamic_model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentLogger{
    public static void log(String msg){
        try{
            FileOutputStream fos=new FileOutputStream("C:\\test.log",true);
            PrintWriter pw=new PrintWriter(fos,true);
            SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
            String date=sdf.format(new Date());
            pw.println(date+": "+msg);
            pw.println("---------------------------------------");
            System.out.println(date+": "+msg);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
