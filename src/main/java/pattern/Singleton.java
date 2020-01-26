package main.java.pattern;

/**
 * an singleton interface to ensure that singletons have an accessor to they're static "instance" attribute.
 * The constructors of an singleton should be private whenever possible in order to prevent a falsely instantiated object.
 */
public interface Singleton {

    /**
     * reference attribute to the object instance
     */
    Singleton instance = null;

    /**
     * a singleton must implement this method, to get a reference to his instance attribute.
     *
     * @return necessary because the method is static
     */
    static Singleton getInstance() {
        return null;
    }
}
