package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseColour;
import at.htlhl.securefinancemanager.repository.ColourRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The ColourController class handles HTTP requests related to colour management.
 * It provides endpoints for retrieving all colours and getting a specific colour by ID.
 *
 * <p>
 * This controller is responsible for handling colour-related operations, including fetching a list of all colours
 * and retrieving a specific colour by its ID. It interacts with the ColourRepository to access
 * the Colour entities in the 'colours' table of the 'secure_finance_manager' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, indicating that it is a controller that
 * handles RESTful HTTP requests. The {@link CrossOrigin} annotation allows cross-origin requests to this controller,
 * enabling it to be accessed from different domains. The {@link RequestMapping} annotation specifies the base path
 * for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The ColourController class works in conjunction with the ColourRepository and other related classes to enable
 * efficient retrieving of colours in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @version 2.1
 * @since 16.11.2023 (version 2.1)
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
     * @return A list of all colours.
     */
    @GetMapping(value = "/colours", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns a list of all colours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all colours",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseColour.class)) }),
            @ApiResponse(responseCode = "404", description = "no colours found",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getColours() {
        try {
            return ResponseEntity.ok(colourRepository.getColours());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific colour.
     *
     * @param colourId The ID of the colour to retrieve.
     * @return The requested colour.
     */
    @GetMapping(value = "/colours/{colourId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns the requested category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested colour",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseColour.class)) }),
            @ApiResponse(responseCode = "400", description = "the colourId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested colour does not exist",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getColour(@PathVariable int colourId) {
        try {
            if (colourId <= 0) {
                throw new MissingRequiredParameter("colourId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(colourRepository.getColour(colourId));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
