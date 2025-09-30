package kiki.exception;

/**
 * Represents an exception specific to the Kiki task manager.
 * <p>
 * Used to signal invalid commands, missing arguments, or other
 * user input errors. When caught, the application prints the
 * exception message to guide the user.
 * </p>
 */
public class KikiException extends Exception {
    // constructor, so catch can print the message directly
    public KikiException(String message) {
        super(message);
    }
}
