package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

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
 * @version 1
 * @since 02.07.2023 (version 1)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */
@Entity
@Table(name = "entries")
public class Entry {
    /** the id of the entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_entry_id")
    private int entryId;

    /** the name of the entry */
    @Column(name = "entry_name")
    private byte[] entryName;

    /** the description of the entry */
    @Column(name = "entry_description")
    private byte[] entryDescription;

    /** the amount of the entry */
    @Column(name = "entry_amount")
    private byte[] entryAmount;

    /** the creation time of the entry */
    @Column(name = "entry_creation_time")
    private byte[] entryCreationTime;

    /** the time of expense for the entry */
    @Column(name = "entry_time_of_expense")
    private byte[] entryTimeOfExpense;

    /** the attachment of the entry */
    @Column(name = "entry_attachment")
    private byte[] entryAttachment;

    /**
     * The label associated with the entry.
     * Represents a many-to-one relationship between Entry and Label.
     */
    @ManyToOne
    @JoinColumn(name = "fk_entry_label_id", referencedColumnName = "pk_label_id", table = "labels")
    private Label entryLabel;

    /**
     * The subcategory associated with the entry.
     * Represents a many-to-one relationship between Entry and Subcategory.
     */
    @ManyToOne
    @JoinColumn(name = "fk_subcategory_id", referencedColumnName = "pk_subcategory_id", table = "subcategories")
    private Subcategory subcategory;

    /**
     * The user associated with the entry.
     * Represents a many-to-one relationship between Entry and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users")
    private User user;
}