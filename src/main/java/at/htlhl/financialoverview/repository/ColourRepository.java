package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Colour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ColourRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Colour> getColours() {
        return null;
    }

    public Colour getColour(int colourId) {
        return null;
    }
}
