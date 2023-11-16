package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseEntryLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import at.htlhl.securefinancemanager.repository.EntryLabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * The EntryLabelController class handles HTTP requests related to the management of associations
 * between Entry and Label entities (EntryLabel). It provides endpoints for adding and removing labels
 * from entries, as well as retrieving associated labels for a specific entry.
 *
 * <p>
 * This controller is responsible for handling most CRUD operations (Create, Read, Delete) on EntryLabel associations.
 * It interacts with the EntryLabelRepository to access and manipulate the EntryLabel entities
 * in the 'entry_labels' table of the 'secure_finance_manager' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, indicating that it is a controller
 * that handles RESTful HTTP requests. The {@link CrossOrigin} annotation allows cross-origin requests to this controller,
 * enabling it to be accessed from different domains. The {@link RequestMapping} annotation specifies the base path
 * for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The EntryLabelController class works in conjunction with the EntryLabelRepository and other related classes
 * to enable efficient management of Entry-Label associations in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @version 2.1
 * @since 16.11.2023 (version 2.1)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/entry-labels")
public class EntryLabelController {
    /** The EntryLabelRepository instance for accessing entry-label data. */
    @Autowired
    EntryLabelRepository entryLabelRepository;

    /**
     * Retrieves a list of labels associated with a specific entry.
     *
     * @param entryId       The ID of the entry.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return A list of labels associated with the entry.
     */
    @GetMapping(value = "/entries/{entryId}/labels", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "retrieves a list of labels associated with a specific entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all labels for the given entryId",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested entryId does not exist or is not found for the authenticated user or the entry does not have any labels associated with it",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getLabelsForEntry(@PathVariable int entryId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryLabelRepository.getLabelsForEntry(entryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a label to a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to add.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The newly created EntryLabel association.
     */
    @PostMapping(value = "/entries/{entryId}/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds a label to a specific entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully added the given label to the given entry",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntryLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId or the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry or label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addLabelToEntry(@PathVariable int entryId,
                                                  @PathVariable int labelId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            } else if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryLabelRepository.addLabelToEntry(entryId, labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Removes a label from a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to remove.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/entries/{entryId}/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "removes a label from a specific entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully removed the given label from the given entry",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId or the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry, label or association does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> removeLabelFromEntry(@PathVariable int entryId,
                                                       @PathVariable int labelId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            } else if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.status(HttpStatus.OK).body(entryLabelRepository.removeLabelFromEntry(entryId, labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}