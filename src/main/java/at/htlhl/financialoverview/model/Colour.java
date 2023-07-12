package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

/**
 * The {@code Colour} class represents a color entity in the 'colours' table of the 'financial_overview' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains two fields:
 * <ul>
 * <li>{@code colourId}: Represents the primary key of the 'colours' table, mapped to the 'pk_colour_id' column.</li>
 * <li>{@code colourName}: Represents the name of the color, mapped to the 'colour_name' column.</li>
 * </ul>
 * </p>
 *
 * @author Fischer
 * @version 1.4
 * @since 12.07.2023 (version 1.4)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "colours")
public class Colour {
    /** the id of the colour */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_colour_id")
    private Integer colourId;

    /** the name of the colour */
    @Column(name = "colour_name")
    private String colourName;

    /** the hex-value of the colour */
    @Column(name = "colour_code")
    private byte[] colourCode;

    public Colour() {}

    /**
     * Constructs a new Colour object with the specified ID, name, and code.
     *
     * @param colourId    the ID of the colour
     * @param colourName  the name of the colour
     * @param colourCode  the code of the colour
     */
    public Colour(int colourId, String colourName, byte[] colourCode) {
        this.colourId = colourId;
        this.colourName = colourName;
        this.colourCode = colourCode;
    }

    /**
     * Returns the ID of the colour.
     *
     * @return the ID of the colour
     */
    public Integer getColourId() {
        return colourId;
    }

    /**
     * Returns the name of the colour.
     *
     * @return the name of the colour
     */
    public String getColourName() {
        return colourName;
    }

    /**
     * Returns the code of the colour.
     *
     * @return the code of the colour
     */
    public byte[] getColourCode() {
        return colourCode;
    }
}