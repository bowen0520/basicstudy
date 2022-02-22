package day21;

@FunctionalInterface
public interface MyFunction<T,R> {
    public R mix(T a,T b);
}
