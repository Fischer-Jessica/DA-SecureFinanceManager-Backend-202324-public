package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

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
 * @version 1
 * @since 02.07.2023 (version 1)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "labels")
public class Label {
    /** the id of the label */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_label_id")
    private int labelId;

    /** the name of the label */
    @Column(name = "label_name")
    private byte[] labelName;

    /** the description of the label */
    @Column(name = "label_description")
    private byte[] labelDescription;

    /**
     * The colour associated with the label.
     * Represents a many-to-one relationship between Label and Colour.
     */
    @ManyToOne
    @JoinColumn(name = "fk_label_colour", referencedColumnName = "pk_colour_id", table = "colours")
    private Colour labelColour;

    /**
     * The user associated with the label.
     * Represents a many-to-one relationship between Label and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users")
    private User user;
}