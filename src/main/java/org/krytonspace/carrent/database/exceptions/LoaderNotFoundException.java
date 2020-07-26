package org.krytonspace.carrent.database.exceptions;

/**
 * Exception raised when a suitable loader could not be found.
 */
public class LoaderNotFoundException extends Exception {
    /**
     * Constructor.
     * @param msg The exception message
     */
    public LoaderNotFoundException(String msg) {
        super(msg);
    }
}
