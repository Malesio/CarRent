package org.krytonspace.carrent.database.exceptions;

/**
 * Exception raised when a loader failed to write/dump data.
 */
public class WritingFailedException extends Exception {
    /**
     * Constructor.
     * @param msg The exception message
     */
    public WritingFailedException(String msg) {
        super(msg);
    }
}
