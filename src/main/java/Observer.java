package main.java;

public interface Observer<T> {
    public void update( Observable<T> o, T arg );
}
