package idv.demo;

public interface DataSink<T> {

    void load(T... data);

}
