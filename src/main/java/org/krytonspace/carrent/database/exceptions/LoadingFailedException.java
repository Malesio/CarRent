package org.krytonspace.carrent.database.exceptions;

/**
 * Exception raised when a loader failed to read or parse data.
 */
public class LoadingFailedException extends Exception {
    /**
     * Constructor.
     * @param msg The exception message
     */
    public LoadingFailedException(String msg) {
        super(msg);
    }
}
