package at.htlhl.securefinancemanager.model.api;

import jakarta.validation.constraints.NotBlank;

/**
 * The {@code ApiEntry} class represents an entry entity in the 'entries' table of the 'secure_finance_manager' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class contains several fields representing entry properties, such as entry name, description, amount, creation time, and attachment.
 * It also includes many-to-one relationships with the {@link ApiLabel}, {@link ApiSubcategory}, and {@link ApiUser} classes, where an entry is associated with a label, subcategory, and user.
 * </p>
 *
 * <p>
 * The class provides a parameterized constructor to create a new Entry object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of entry attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiEntry entry = new ApiEntry("Grocery Shopping", "Monthly grocery expenses", "-$200", "2023-11-10T15:30:00", "receipt.jpg");
 * }</pre>
 * </p>
 *
 * <p>
 * This class can be used as a data transfer object (DTO) to facilitate communication between the frontend and backend.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 2.1
 * @since 02.02.2024 (version 2.1)
 */
public class ApiEntry {
    /**
     * The name of the entry.
     */
    private String entryName;

    /**
     * The description of the entry.
     */
    private String entryDescription;

    /**
     * The amount of the entry.
     */
    @NotBlank
    private String entryAmount;

    /**
     * The time of the transaction of the entry.
     */
    @NotBlank
    private String entryTimeOfTransaction;

    /**
     * The attachment of the entry.
     */
    private String entryAttachment;

    /**
     * Constructs a new Entry object with the specified properties.
     *
     * @param entryName              The name of the entry.
     * @param entryDescription       The description of the entry.
     * @param entryAmount            The amount of the entry.
     * @param entryTimeOfTransaction The time of the transaction in the entry.
     * @param entryAttachment        The attachment of the entry.
     */
    public ApiEntry(String entryName, String entryDescription, String entryAmount, String entryTimeOfTransaction, String entryAttachment) {
        this.entryName = entryName;
        this.entryDescription = entryDescription;
        this.entryAmount = entryAmount;
        this.entryTimeOfTransaction = entryTimeOfTransaction;
        this.entryAttachment = entryAttachment;
    }

    /**
     * Returns the name of the entry.
     *
     * @return The name of the entry.
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * Returns the description of the entry.
     *
     * @return The description of the entry.
     */
    public String getEntryDescription() {
        return entryDescription;
    }

    /**
     * Returns the amount of the entry.
     *
     * @return The amount of the entry.
     */
    public String getEntryAmount() {
        return entryAmount;
    }

    /**
     * Returns the time of the transaction of the entry.
     *
     * @return The time of transaction of the entry.
     */
    public String getEntryTimeOfTransaction() {
        return entryTimeOfTransaction;
    }

    /**
     * Returns the attachment of the entry.
     *
     * @return The attachment of the entry.
     */
    public String getEntryAttachment() {
        return entryAttachment;
    }
}