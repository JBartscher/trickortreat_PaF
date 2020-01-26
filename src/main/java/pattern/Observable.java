package main.java.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * object which can notify observers.
 *
 * @see Observer#update(Observable, Object)
 * @param <T> generic object
 */
public class Observable<T> {

    /**
     * list of all observers, observing this object.
     */
    protected List<Observer<T>> observers = new ArrayList<Observer<T>>();

    /**
     * add a new observer to this object.
     *
     * @param observer observer
     */
    public void addObserver( Observer<T> observer )
    {
        if ( ! observers.contains( observer ) )
            observers.add( observer );
    }

    /**
     * remove observer from this object.
     *
     * @param observer observer
     */
    public void removeObserver(Observer<?> observer )
    {
        observers.remove( observer );
    }

    /**
     * notify observers of this object and call they're update() method.
     *
     * @see Observer#update(Observable, Object)
     * @param arg additional args parameter
     */
    public void notifyObservers( T arg )
    {
        for ( Observer<T> observer : observers )
            observer.update( this, arg );
    }
}
