package at.htlhl.financialoverview.model;

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
 * @version 1.6
 * @since 21.07.2023 (version 1.6)
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

    /** the name of the label */
    @Column(name = "label_name")
    private String labelName;

    /** the description of the label */
    @Column(name = "label_description")
    private String labelDescription;

    /**
     * The colour associated with the label.
     * Represents a many-to-one relationship between Label and Colour.
     */
    @ManyToOne
    @JoinColumn(name = "fk_label_colour_id", referencedColumnName = "pk_colour_id", table = "colours")
    private Colour labelColour;

    @Column(name = "fk_label_colour_id")
    private int labelColourId;

    /**
     * The user associated with the label.
     * Represents a many-to-one relationship between Label and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User labelUser;

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

    public Label() {}

    public Label(int labelId, String labelName, String labelDescription, int labelColourId, int labelUserId) {
        this.labelId = labelId;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelColourId = labelColourId;
        this.labelUserId = labelUserId;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getLabelDescription() {
        return labelDescription;
    }

    public int getLabelColourId() {
        return labelColourId;
    }

    public User getLabelUser() {
        return labelUser;
    }
}