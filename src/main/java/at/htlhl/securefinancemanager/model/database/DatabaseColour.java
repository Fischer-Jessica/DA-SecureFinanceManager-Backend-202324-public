package at.htlhl.securefinancemanager.model.database;

/**
 * The {@code DatabaseColour} class represents a colour entity in the 'colours' table of the 'secure_finance_manager' PostgreSQL database.
 *
 * <p>
 * This class includes attributes such as the unique colour identifier (ID), name, and hex-value code.
 * </p>
 *
 * <p>
 * The class provides a parameterized constructor to create a new DatabaseColour object with the specified ID, name, and code.
 * Additionally, getter methods are provided to retrieve the values of colour attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * DatabaseColour colour = new DatabaseColour(1, "Red", new byte[]{(byte)255, 0, 0});
 * }</pre>
 * </p>
 *
 * <p>
 * Note: The hex-value code is represented by a byte array.
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work with database interactions related to colours.
 * </p>
 *
 * <p>
 * It is important to mention that the colour ID is generated automatically by the database.
 * </p>
 *
 * <p>
 * Author: Fischer
 * Version: 1.6
 * Since: 10.11.2023 (version 1.6)
 * </p>
 */
public class DatabaseColour {
    /**
     * The unique identifier for this Colour.
     * It is generated automatically by the database.
     */
    private int colourId;

    /** The name of the colour. */
    private String colourName;

    /** The hex-value of the colour. */
    private byte[] colourCode;

    /**
     * Constructs a new DatabaseColour object with the specified ID, name, and code.
     *
     * @param colourId      The ID of the colour.
     * @param colourName    The name of the colour.
     * @param colourCode    The code of the colour.
     */
    public DatabaseColour(int colourId, String colourName, byte[] colourCode) {
        this.colourId = colourId;
        this.colourName = colourName;
        this.colourCode = colourCode;
    }

    /**
     * Returns the ID of the colour.
     *
     * @return The ID of the colour.
     */
    public int getColourId() {
        return colourId;
    }

    /**
     * Returns the name of the colour.
     *
     * @return The name of the colour.
     */
    public String getColourName() {
        return colourName;
    }

    /**
     * Returns the code of the colour.
     *
     * @return The code of the colour.
     */
    public byte[] getColourCode() {
        return colourCode;
    }
}