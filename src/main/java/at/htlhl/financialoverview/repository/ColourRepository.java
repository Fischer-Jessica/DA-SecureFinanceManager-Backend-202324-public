package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Colour;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * The ColourRepository class handles the persistence operations for colour data.
 * It serves as a Spring Data JPA repository for the Colour entity.
 *
 * <p>
 * This repository interfaces with the Colour entity class, which is a POJO (Plain Old Java Object) or entity class that maps to the 'colours' table in the database.
 * It uses Spring's JdbcTemplate to perform database operations without boilerplate code.
 * </p>
 *
 * <p>
 * The ColourRepository serves as an abstraction layer between the ColourController and the underlying data storage, enabling seamless access and manipulation of Colour entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for Colour entities.
 * </p>
 *
 * @author Fischer
 * @version 1.3
 * @since 24.07.2023 (version 1.3)
 */
@Repository
public class ColourRepository {
    /** SQL query to retrieve all colours from the 'colours' table in the database. */
    private static final String SELECT_COLOURS = "SELECT pk_colour_id, colour_name, colour_code " +
            "FROM colours;";

    /**  SQL query to retrieve a specific colour from the 'colours' table in the database. */
    private static final String SELECT_COLOUR = "SELECT pk_colour_id, colour_name, colour_code " +
            "FROM colours " +
            "WHERE pk_colour_id = ?;";

    /**
     * Retrieves a list of all colours.
     *
     * @return A list of Colour objects representing the colours.
     */
    public List<Colour> getColours() {
        return UserRepository.jdbcTemplate.query(SELECT_COLOURS, (rs, rowNum) -> {
            int colourId = rs.getInt("pk_colour_id");
            String colourName = rs.getString("colour_name");
            byte[] colourCode = rs.getBytes("colour_code");
            return new Colour(colourId, colourName, colourCode);
        });
    }

    /**
     * Retrieves a specific colour.
     *
     * @param colourId The ID of the colour to retrieve.
     * @return The requested Colour object.
     */
    public Colour getColour(int colourId) {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(ColourRepository.SELECT_COLOUR);
            ps.setInt(1, colourId);
            ResultSet rs = ps.executeQuery();

            Colour colour = null;
            if (rs.next()) {
                colour = new Colour(rs.getInt("pk_colour_id"),
                        rs.getString("colour_name"),
                        rs.getBytes("colour_code"));
            }
            conn.close();
            return colour;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
