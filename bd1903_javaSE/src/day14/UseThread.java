package day14;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class UseThread<T> {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        List<String> list = new ArrayList<>();

    }
}
class MyThread extends Thread{
    @Override
    public void run() {
        super.run();
    }
}
class MyT implements Callable{

    @Override
    public Object call() throws Exception {
        return null;
    }
}

