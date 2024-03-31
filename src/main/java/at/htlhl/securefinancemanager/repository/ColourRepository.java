package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseColour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code ColourRepository} class handles the persistence operations for colour data.
 * It serves as a Spring Data JPA repository for the Colour entity.
 *
 * <p>
 * This repository interfaces with the Colour entity class, which is a POJO (Plain Old Java Object) or entity class that maps to the 'colours' table in the database.
 * It uses Spring's JdbcTemplate to perform database operations without boilerplate code.
 * </p>
 *
 * <p>
 * The {@code ColourRepository} serves as an abstraction layer between the ColourController and the underlying data storage, enabling seamless access and manipulation of Colour entities.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 2.1
 * @since 31.03.2024 (version 2.1)
 */
@Repository
public class ColourRepository {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * SQL query to retrieve all colours from the 'colours' table in the database.
     */
    private static final String SELECT_COLOURS = "SELECT pk_colour_id, colour_name, colour_code " +
            "FROM colours;";

    /**
     * SQL query to retrieve a specific colour from the 'colours' table in the database.
     */
    private static final String SELECT_COLOUR = "SELECT pk_colour_id, colour_name, colour_code " +
            "FROM colours " +
            "WHERE pk_colour_id = ?;";

    /**
     * Retrieves a list of all colours.
     *
     * @return A list of Colour objects representing the colours.
     * @throws ValidationException If no colours are found.
     */
    public List<DatabaseColour> getColours() throws ValidationException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_COLOURS);
             ResultSet rs = ps.executeQuery()) {

            List<DatabaseColour> databaseColours = new ArrayList<>();
            while (rs.next()) {
                int colourId = rs.getInt("pk_colour_id");
                String colourName = rs.getString("colour_name");
                byte[] colourCode = rs.getBytes("colour_code");
                databaseColours.add(new DatabaseColour(colourId, colourName, bytesToHex(colourCode)));
            }
            if (databaseColours.isEmpty()) {
                throw new ValidationException("No colours found.");
            }
            return databaseColours;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specific colour.
     *
     * @param colourId The ID of the colour to retrieve.
     * @return The requested Colour object.
     * @throws ValidationException If the specified colour does not exist.
     */
    public DatabaseColour getColour(int colourId) throws ValidationException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_COLOUR)) {

            ps.setInt(1, colourId);
            try (ResultSet rs = ps.executeQuery()) {
                DatabaseColour databaseColour = null;
                if (rs.next()) {
                    databaseColour = new DatabaseColour(rs.getInt("pk_colour_id"),
                            rs.getString("colour_name"),
                            bytesToHex(rs.getBytes("colour_code")));
                }
                if (databaseColour == null) {
                    throw new ValidationException("Colour with ID " + colourId + " does not exist");
                }
                return databaseColour;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Converts a byte array to its hexadecimal representation.
     *
     * @param bytes The byte array to be converted.
     * @return A hexadecimal representation of the input byte array.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString();
    }
}