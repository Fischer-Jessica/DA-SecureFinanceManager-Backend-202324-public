package at.htlhl.securefinancemanager.exception;

/**
 * The {@code MissingRequiredParameter} exception class is used to indicate that a required parameter
 * is missing in a request or operation. Instances of this exception are typically thrown when validating
 * input parameters, and the absence of a necessary parameter is detected.
 *
 * <p>
 * This exception extends the {@code Exception} class and provides a constructor that accepts a
 * descriptive message explaining the reason for the exception.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>{@code
 * try {
 *     // Code that may throw MissingRequiredParameter exception
 * } catch (MissingRequiredParameter exception) {
 *     // Handle the exception, e.g., log an error message or return an error response
 * }
 * </pre>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.1
 * @since 02.02.2024 (version 1.1)
 */
public class MissingRequiredParameter extends Exception {
    /**
     * Constructs a new {@code MissingRequiredParameter} exception with the specified detail message.
     *
     * @param message A descriptive message explaining the reason for the exception.
     */
    public MissingRequiredParameter(String message) {
        super(message);
    }
}