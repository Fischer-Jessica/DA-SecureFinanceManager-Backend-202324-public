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
 * @version 1
 * @since 02.07.2023 (version 1)
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
}