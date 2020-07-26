package org.krytonspace.carrent.controllers.exceptions;

/**
 * Exception class indicating a user input that has been deemed invalid.
 */
public class InvalidDataException extends Exception {
    /**
     * Constructor.
     * @param msg The message to display
     */
    public InvalidDataException(String msg) {
        super(msg);
    }
}
