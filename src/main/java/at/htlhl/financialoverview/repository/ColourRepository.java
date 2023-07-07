package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Colour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ColourRepository class handles the persistence operations for colour data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class ColourRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of all colours.
     *
     * @return A list of Colour objects representing the colours.
     */
    public List<Colour> getColours() {
        return null;
    }

    /**
     * Retrieves a specific colour.
     *
     * @param colourId The ID of the colour to retrieve.
     * @return The requested Colour object.
     */
    public Colour getColour(int colourId) {
        return null;
    }
}
