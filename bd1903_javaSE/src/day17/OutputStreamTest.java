package day17;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class OutputStreamTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        testByteArrayOS();
    }

    static void testByteArrayOS() throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String s = "HelloWord,老司机";
        byte[] b = s.getBytes("UTF-16");
        byteArrayOutputStream.write(b,0,b.length);
        System.out.println(Arrays.toString(byteArrayOutputStream.toByteArray()));
        System.out.println(byteArrayOutputStream.toString("UTF-16"));
    }
}
