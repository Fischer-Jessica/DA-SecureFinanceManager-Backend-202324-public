package at.htlhl.securefinancemanager.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * The {@code Subcategory} class represents a subcategory entity in the 'subcategories' table of the 'financial_overview' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains several fields representing subcategory properties, such as subcategory name and subcategory description.
 * It also includes many-to-one relationships with the {@link Category}, {@link Colour}, and {@link User} classes, where a subcategory belongs to a category, has a subcategory colour, and is associated with a user.
 * It also has a one-to-many relationship with the {@link Entry} class, where a subcategory can have multiple entries associated with it.
 * </p>
 *
 * @author Fischer
 * @version 1.6
 * @since 15.10.2023 (version 1.6)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "subcategories")
@SecondaryTable(name = "categories", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_category_id"))
@SecondaryTable(name = "colours", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_colour_id"))
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_user_id"))

public class Subcategory {
    /**
     * The unique identifier for this Subcategory.
     * It is generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_subcategory_id")
    private int subcategoryId;

    /**
     * The category to which the subcategory belongs.
     * Represents a many-to-one relationship between Subcategory and Category.
     */
    @ManyToOne
    @JoinColumn(name = "fk_category_id", insertable=false, updatable=false)
    private Category subcategoryCategory;

    /** The ID of the associated category. */
    @Column(name = "fk_category_id")
    private int subcategoryCategoryId;

    /** The name of the subcategory. */
    @Column(name = "subcategory_name")
    private String subcategoryName;

    /** The description of the subcategory. */
    @Column(name = "subcategory_description")
    private String subcategoryDescription;

    /**
     * The colour associated with the subcategory.
     * Represents a many-to-one relationship between Subcategory and Colour.
     */
    @ManyToOne
    @JoinColumn(name = "fk_subcategory_colour_id", referencedColumnName = "pk_colour_id", table = "colours")
    private Colour subcategoryColour;

    /** The ID of the associated colour. */
    @Column(name = "fk_subcategory_colour_id")
    private int subcategoryColourId;

    /**
     * The user associated with the subcategory.
     * Represents a many-to-one relationship between Subcategory and User.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User subcategoryUser;

    /** The ID of the associated user. */
    @Column(name = "fk_user_id")
    private int subcategoryUserId;

    /**
     * The entries associated with the subcategory.
     * Represents a one-to-many relationship between Subcategory and Entry.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_subcategory_id")
    private List<Entry> entries;

    /** Default constructor. */
    public Subcategory() {}

    /**
     * Constructs a new Subcategory object with the specified properties.
     *
     * @param subcategoryId                 The unique identifier for this Subcategory.
     * @param subcategoryCategoryId         The ID of the associated category.
     * @param subcategoryName               The name of the subcategory.
     * @param subcategoryDescription        The description of the subcategory.
     * @param subcategoryColourId           The ID of the associated colour.
     * @param subcategoryUserId             The ID of the associated user.
     */
    public Subcategory(int subcategoryId, int subcategoryCategoryId, String subcategoryName, String subcategoryDescription, int subcategoryColourId, int subcategoryUserId) {
        this.subcategoryId = subcategoryId;
        this.subcategoryCategoryId = subcategoryCategoryId;
        this.subcategoryName = subcategoryName;
        this.subcategoryDescription = subcategoryDescription;
        this.subcategoryColourId = subcategoryColourId;
        this.subcategoryUserId = subcategoryUserId;
    }

    /**
     * Returns the unique identifier for this Subcategory.
     *
     * @return The unique identifier for this Subcategory.
     */
    public int getSubcategoryId() {
        return subcategoryId;
    }

    /**
     * Returns the ID of the associated category.
     *
     * @return The ID of the associated category.
     */
    public int getSubcategoryCategoryId() {
        return subcategoryCategoryId;
    }

    /**
     * Returns the name of the subcategory.
     *
     * @return The name of the subcategory.
     */
    public String getSubcategoryName() {
        return subcategoryName;
    }

    /**
     * Returns the description of the subcategory.
     *
     * @return The description of the subcategory.
     */
    public String getSubcategoryDescription() {
        return subcategoryDescription;
    }

    /**
     * Returns the ID of the associated colour.
     *
     * @return The ID of the associated colour.
     */
    public int getSubcategoryColourId() {
        return subcategoryColourId;
    }

    /**
     * Returns the ID of the user associated with the subcategory.
     *
     * @return The ID of the user associated with the subcategory.
     */
    public int getSubcategoryUserId() {
        return subcategoryUserId;
    }

    /**
     * Sets the ID of the subcategory.
     *
     * @param subcategoryId The ID of the subcategory to set.
     */
    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    /**
     * Sets the ID of the category to which this subcategory belongs.
     *
     * @param subcategoryCategoryId The ID of the category for this subcategory.
     */
    public void setSubcategoryCategoryId(int subcategoryCategoryId) {
        this.subcategoryCategoryId = subcategoryCategoryId;
    }

    /**
     * Sets the ID of the user to whom this subcategory belongs.
     *
     * @param subcategoryUserId The ID of the user for this subcategory.
     */
    public void setSubcategoryUserId(int subcategoryUserId) {
        this.subcategoryUserId = subcategoryUserId;
    }
}