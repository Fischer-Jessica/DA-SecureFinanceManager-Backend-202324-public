package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Colour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * The ColourRepository class handles the persistence operations for colour data.
 *
 * @author Fischer
 * @version 1.2
 * @since 12.07.2023 (version 1.2)
 */
@Repository
public class ColourRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_COLOURS = "SELECT pk_colour_id, colour_name, colour_code FROM colours;";

    /**
     * Retrieves a list of all colours.
     *
     * @return A list of Colour objects representing the colours.
     */
    public List<Colour> getColours() {
        return jdbcTemplate.query(SELECT_COLOURS, (rs, rowNum) -> {
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
        String SELECT_COLOUR = "SELECT pk_colour_id, colour_name, colour_code FROM colours WHERE pk_colour_id = " + colourId + ";";
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            Colour colour = new Colour();
            ResultSet getColour = conn.createStatement().executeQuery(SELECT_COLOUR);
            if (getColour.next()) {
                colour = new Colour(getColour.getInt("pk_colour_id"),
                        getColour.getString("colour_name"),
                        getColour.getBytes("colour_code"));
            }
            conn.close();
            return colour;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
