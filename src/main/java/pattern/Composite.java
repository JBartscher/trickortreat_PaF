package main.java.pattern;

import main.java.map.Sector;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface which ensures that a class implements the composite pattern.
 */
public interface Composite<T> {

    /**
     * remove child from composite.
     *
     * @param child childObject
     */
    void removeChild(T child);

    /**
     * add child to composite.
     *
     * @param child childObject
     */
    void addChild(T child);

    /**
     * set parent composite of this composite.
     *
     * @param parent parentObject
     */
    void setParent(T parent);

    /**
     * get all children of this composite.
     *
     * @return a List of childObjects
     */
    List<T> getChildren();


}
