package main.java.pattern;

/**
 * Interface that marks a class as an Observer which can be notified by its observables.
 *
 * @see Observable
 * @param <T> Object which
 */
public interface Observer<T> {
    /**
     * method that gets called when a observable notifies this observer.
     *
     * @param o object
     * @param arg generic args parameter
     */
    void update(Observable<T> o, T arg);
}
