package main.java.pattern;

/**
 * Interface that ensures that a class will be a factory
 */
public interface Factory<T> {

    /**
     * create new instance of an generic class type.
     *
     * @param type type decide string
     * @return a generic object
     */
    T createNewInstance(String type);
}
