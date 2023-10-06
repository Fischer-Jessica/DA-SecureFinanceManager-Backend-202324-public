package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.model.Colour;
import at.htlhl.securefinancemanager.repository.ColourRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ColourController class handles HTTP requests related to colour management.
 * It provides endpoints for retrieving all colours and getting a specific colour by ID.
 *
 * <p>
 * This controller is responsible for handling colour-related operations, including fetching a list of all colours and retrieving a specific colour by its ID.
 * It interacts with the ColourRepository to access and manipulate the Colour entities in the 'colours' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The ColourController class works in conjunction with the ColourRepository and other related classes to enable efficient management of colours in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.6
 * @since 06.10.2023 (version 1.6)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class ColourController {
    /** The ColourRepository instance for accessing colour data. */
    @Autowired
    ColourRepository colourRepository;

    /**
     * Returns a list of all colours.
     *
     * @return A list of colours.
     */
    @GetMapping(value = "/colours", headers = "API-Version=0")
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
    @GetMapping(value = "/colours/{colourId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one colour")
    public Colour getColour(@PathVariable int colourId) {
        return colourRepository.getColour(colourId);
    }
}
