package at.htlhl.securefinancemanager.exception;

/**
 * The {@code ValidationException} exception class is used to indicate that a validation error
 * has occurred during processing. Instances of this exception are typically thrown when checking
 * the validity of input data, and an inconsistency or violation of business rules is detected.
 *
 * <p>
 * This exception extends the {@code Exception} class and provides a constructor that accepts a
 * descriptive message explaining the reason for the validation failure.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>{@code
 * try {
 *     // Code that may throw ValidationException
 * } catch (ValidationException exception) {
 *     // Handle the exception, e.g., log an error message or return an error response
 * }
 * </pre>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.1
 * @since 02.02.2024 (version 1.1)
 */
public class ValidationException extends Exception {
    /**
     * Constructs a new {@code ValidationException} exception with the specified detail message.
     *
     * @param message A descriptive message explaining the reason for the validation failure.
     */
    public ValidationException(String message) {
        super(message);
    }
}