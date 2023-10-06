package at.htlhl.securefinancemanager.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * The {@code Entry} class represents an entry entity in the 'entries' table of the 'financial_overview' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains several fields representing entry properties, such as entry name, description, amount, creation time, and attachment.
 * It also includes many-to-one relationships with the {@link Label}, {@link Subcategory}, and {@link User} classes, where an entry is associated with a label, subcategory, and user.
 * </p>
 *
 * @author Fischer
 * @version 1.6
 * @since 29.09.2023 (version 1.6)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "entries")
@SecondaryTable(name = "subcategories", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_subcategory_id"))
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_user_id"))
@SecondaryTable(name = "labels", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_label_id"))
public class Entry {
    /**
     * The unique identifier for this Entry.
     * It is generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_entry_id")
    private int entryId;

    /** The name of the entry. */
    @Column(name = "entry_name")
    private String entryName;

    /** The description of the entry. */
    @Column(name = "entry_description")
    private String entryDescription;

    /** The amount of the entry. */
    @Column(name = "entry_amount")
    private String entryAmount;

    /** The creation time of the entry. */
    @Column(name = "entry_creation_time")
    private String entryCreationTime;

    /** The time of the transaction in the entry. */
    @Column(name = "entry_time_of_transaction")
    private String entryTimeOfTransaction;

    /** The attachment of the entry. */
    @Column(name = "entry_attachment")
    private String entryAttachment;

    /**
     * The subcategory associated with the entry.
     * Represents a many-to-one relationship between Entry and Subcategory.
     */
    @ManyToOne
    @JoinColumn(name = "fk_subcategory_id", referencedColumnName = "pk_subcategory_id", table = "subcategories", insertable=false, updatable=false)
    private Subcategory subcategory;

    /** The ID of the subcategory associated with the Entry. */
    @Column(name = "fk_subcategory_id")
    private int entrySubcategoryId;


    /**
     * The user associated with the entry.
     * Represents a many-to-one relationship between Entry and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User user;

    /** The ID of the user associated with the Entry. */
    @Column(name = "fk_user_id")
    private int entryUserId;

    /**
     * Many-to-many relationship with Label.
     * Represents the list of labels associated with this entry.
     */
    @ManyToMany
    @JoinTable(
            name = "entry_labels",
            joinColumns = @JoinColumn(name = "fk_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_label_id")
    )
    private List<Label> labels;

    /**
     * Default constructor.
     */
    public Entry() {}

    /**
     * Constructs a new Entry object with the specified properties.
     *
     * @param entryId                   The ID of the entry.
     * @param entryName                 The name of the entry.
     * @param entryDescription          The description of the entry.
     * @param entryAmount               The amount of the entry.
     * @param entryCreationTime         The creation time of the entry.
     * @param entryTimeOfTransaction    The time of the transaction in the entry.
     * @param entryAttachment           The attachment of the entry.
     * @param entrySubcategoryId        The ID of the associated subcategory.
     * @param entryUserId               The ID of the associated user.
     */
    public Entry(int entryId, String entryName, String entryDescription, String entryAmount, String entryCreationTime, String entryTimeOfTransaction, String entryAttachment, int entrySubcategoryId, int entryUserId) {
        this.entryId = entryId;
        this.entryName = entryName;
        this.entryDescription = entryDescription;
        this.entryAmount = entryAmount;
        this.entryCreationTime = entryCreationTime;
        this.entryTimeOfTransaction = entryTimeOfTransaction;
        this.entryAttachment = entryAttachment;
        this.entrySubcategoryId = entrySubcategoryId;
        this.entryUserId = entryUserId;
    }

    /**
     * Returns the ID of the entry.
     *
     * @return The ID of the entry.
     */
    public int getEntryId() {
        return entryId;
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
     * Returns the creation time of the entry.
     *
     * @return The creation time of the entry.
     */
    public String getEntryCreationTime() {
        return entryCreationTime;
    }

    /**
     * Returns the time of the transaction for the entry.
     *
     * @return The time of transaction for the entry.
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

    /**
     * Returns the ID of the associated subcategory.
     *
     * @return The ID of the associated subcategory.
     */
    public int getEntrySubcategoryId() {
        return entrySubcategoryId;
    }

    /**
     * Returns the ID of the associated user.
     *
     * @return The ID of the associated user.
     */
    public int getEntryUserId() {
        return entryUserId;
    }
}