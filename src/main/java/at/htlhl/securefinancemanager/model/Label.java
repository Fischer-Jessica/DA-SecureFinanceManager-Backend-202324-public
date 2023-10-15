package at.htlhl.securefinancemanager.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * The {@code Label} class represents a label entity in the 'labels' table of the 'financial_overview' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains several fields representing label properties, such as label name and label description.
 * It also includes many-to-one relationships with the {@link Colour} and {@link User} classes, where a label has a label colour and is associated with a user.
 * </p>
 *
 * @author Fischer
 * @version 1.8
 * @since 15.10.2023 (version 1.8)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "labels")
@SecondaryTable(name = "colours", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_colour_id"))
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_user_id"))
public class Label {
    /**
     * The unique identifier for this Label.
     * It is generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_label_id")
    private int labelId;

    /** The name of the label. */
    @Column(name = "label_name")
    private String labelName;

    /** The description of the label. */
    @Column(name = "label_description")
    private String labelDescription;

    /**
     * The colour associated with the label.
     * Represents a many-to-one relationship between Label and Colour.
     */
    @ManyToOne
    @JoinColumn(name = "fk_label_colour_id", referencedColumnName = "pk_colour_id", table = "colours")
    private Colour labelColour;

    /** The ID of the associated colour. */
    @Column(name = "fk_label_colour_id")
    private int labelColourId;

    /**
     * The user associated with the label.
     * Represents a many-to-one relationship between Label and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User labelUser;

    /** The ID of the associated user. */
    @Column(name = "fk_user_id")
    private int labelUserId;

    /**
     * Many-to-many relationship with Entry.
     * Represents the list of entries associated with this label.
     */
    @ManyToMany
    @JoinTable(
            name = "entry_labels",
            joinColumns = @JoinColumn(name = "fk_label_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_entry_id")
    )
    private List<Entry> entries;

    /** Default constructor. */
    public Label() {}

    /**
     * Constructs a new Label object with the specified properties.
     *
     * @param labelId               The unique identifier for this Label.
     * @param labelName             The name of the label.
     * @param labelDescription      The description of the label.
     * @param labelColourId         The ID of the associated colour.
     * @param labelUserId           The ID of the associated user.
     */
    public Label(int labelId, String labelName, String labelDescription, int labelColourId, int labelUserId) {
        this.labelId = labelId;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelColourId = labelColourId;
        this.labelUserId = labelUserId;
    }

    /**
     * Returns the unique identifier for this Label.
     *
     * @return The unique identifier for this Label.
     */
    public int getLabelId() {
        return labelId;
    }

    /**
     * Returns the name of the label.
     *
     * @return The name of the label.
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * Returns the description of the label.
     *
     * @return The description of the label.
     */
    public String getLabelDescription() {
        return labelDescription;
    }

    /**
     * Returns the ID of the associated colour.
     *
     * @return The ID of the associated colour.
     */
    public int getLabelColourId() {
        return labelColourId;
    }

    /**
     * Returns the ID of the user associated with the label.
     *
     * @return The ID of the user associated with the label.
     */
    public int getLabelUserId() {
        return labelUserId;
    }

    /**
     * Sets the ID of the label.
     *
     * @param labelId The ID to be associated with the label.
     *               This ID uniquely identifies the label within the system.
     */
    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    /**
     * Sets the user ID associated with this label.
     *
     * @param labelUserId The user ID to be associated with the label.
     *                   This ID represents the user to whom the label belongs.
     */
    public void setLabelUserId(int labelUserId) {
        this.labelUserId = labelUserId;
    }
}