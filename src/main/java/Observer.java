package main.java;

public interface Observer<T> {
    void update(Observable<T> o, T arg);
}
