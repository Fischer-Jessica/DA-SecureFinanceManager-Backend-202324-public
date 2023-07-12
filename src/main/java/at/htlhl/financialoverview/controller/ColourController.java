package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Colour;
import at.htlhl.financialoverview.repository.ColourRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ColourController class handles the HTTP requests related to colour management.
 *
 * @author Fischer
 * @version 1.3
 * @since 12.07.2023 (version 1.3)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class ColourController {
    /** The CategoryRepository instance for accessing colour data. */
    @Autowired
    ColourRepository colourRepository;

    /**
     * Returns a list of all colours.
     *
     * @return A list of colours.
     */
    @GetMapping("/colours")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all colours")
    public List<Colour> getColours() {
        return colourRepository.getColours();
    }

    /**
     * Returns a specific colour.
     *
     * @param colourId The ID of the colour to retrieve.
     * @return The requested colour.
     */
    @GetMapping("/colours/{colourId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one colour")
    public Colour getColour(@PathVariable int colourId) {
        return colourRepository.getColour(colourId);
    }
}
