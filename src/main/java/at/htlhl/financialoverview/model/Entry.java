package at.htlhl.financialoverview.model;

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
 * @version 1.3
 * @since 21.07.2023 (version 1.3)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */
@Entity
@Table(name = "entries")
@SecondaryTable(name = "subcategories", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_subcategory_id"))
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_user_id"))
@SecondaryTable(name = "labels", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_label_id"))
public class Entry {
    /** the id of the entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_entry_id")
    private int entryId;

    /** the name of the entry */
    @Column(name = "entry_name")
    private String entryName;

    /** the description of the entry */
    @Column(name = "entry_description")
    private String entryDescription;

    /** the amount of the entry */
    @Column(name = "entry_amount")
    private String entryAmount;

    /** the creation time of the entry */
    @Column(name = "entry_creation_time")
    private String entryCreationTime;

    /** the time of expense for the entry */
    @Column(name = "entry_time_of_expense")
    private String entryTimeOfExpense;

    /** the attachment of the entry */
    @Column(name = "entry_attachment")
    private String entryAttachment;

    /**
     * The subcategory associated with the entry.
     * Represents a many-to-one relationship between Entry and Subcategory.
     */
    @ManyToOne
    @JoinColumn(name = "fk_subcategory_id", referencedColumnName = "pk_subcategory_id", table = "subcategories", insertable=false, updatable=false)
    private Subcategory subcategory;

    @Column(name = "fk_subcategory_id")
    private int subcategoryId;


    /**
     * The user associated with the entry.
     * Represents a many-to-one relationship between Entry and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User user;

    @Column(name = "fk_user_id")
    private int userId;

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

    public Entry() {}

    public Entry(int entryId, String entryName, String entryDescription, String entryAmount, String entryCreationTime, String entryTimeOfExpense, String entryAttachment, int subcategoryId, int userId) {
        this.entryId = entryId;
        this.entryName = entryName;
        this.entryDescription = entryDescription;
        this.entryAmount = entryAmount;
        this.entryCreationTime = entryCreationTime;
        this.entryTimeOfExpense = entryTimeOfExpense;
        this.entryAttachment = entryAttachment;
    }

    public int getEntryId() {
        return entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getEntryDescription() {
        return entryDescription;
    }

    public String getEntryAmount() {
        return entryAmount;
    }

    public String getEntryCreationTime() {
        return entryCreationTime;
    }

    public String getEntryTimeOfExpense() {
        return entryTimeOfExpense;
    }

    public String getEntryAttachment() {
        return entryAttachment;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public int getUserId() {
        return userId;
    }
}