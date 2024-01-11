package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseEntryLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import at.htlhl.securefinancemanager.repository.EntryLabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.ArrayList;
import java.util.List;

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
 * @version 2.8
 * @since 11.01.2024 (version 2.8)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/entry-labels/entries")
public class EntryLabelController {
    /** The EntryLabelRepository instance for accessing entry-label data. */
    @Autowired
    EntryLabelRepository entryLabelRepository;

    // GET /labels *******************************************************************************************************

    /**
     * Retrieves a list of labels associated with a specific entry.
     *
     * @param entryId       The ID of the entry.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return A list of labels associated with the entry.
     */
    @GetMapping(value = "/{entryId}/labels", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "retrieves a list of labels associated with a specific entry",
            description = "Returns a list of labels associated with the specified entryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all labels for the given entryId",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested entryId does not exist or is not found for the authenticated user or the entry does not have any labels associated with it",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getLabelsForEntryV1(@Parameter(description = "The entryId added to the URL to retrieve the associated labels.")@PathVariable int entryId,
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
     * Retrieves lists of labels associated with a specific entries.
     *
     * @param entryIds      The IDs of the entries.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return Lists of labels associated with the entries.
     */
    @GetMapping(value = "/{entryIds}/labels", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "retrieves lists of labels associated with specific entries",
            description = "Returns a list of labels with the specified entryIds from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all labels for the given entryIds",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "a entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a requested entryId does not exist or is not found for the authenticated user or the entry does not have any labels associated with it",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getLabelsForEntriesV2(@Parameter(description = "List of entryIds added to the URL to retrieve the associated labels.")@PathVariable List<Integer> entryIds,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<List<DatabaseLabel>> labels = new ArrayList<>();
            for (int entryId : entryIds) {
                if (entryId <= 0) {
                    throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
                }
                labels.add(entryLabelRepository.getLabelsForEntry(entryId, userDetails.getUsername()));
            }
            return ResponseEntity.ok(labels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Retrieves a list of entries associated with a specific label.
     *
     * @param labelId       The ID of the label.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return A list of labels associated with the entry.
     */
    @GetMapping(value = "/{labelId}/entries", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "retrieves a list of entries associated with a specific label",
            description = "Returns a list of entries associated with the specified labelId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all entries for the given labelId",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested labelId does not exist or is not found for the authenticated user or the label does not have any entries associated with it",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getEntriesForLabelV1(@Parameter(description = "The labelId added to the URL to retrieve the associated entries.")@PathVariable int labelId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryLabelRepository.getEntriesForLabel(labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    // POST /labels/{labelIds} *******************************************************************************************

    /**
     * Adds a label to a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to add.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The newly created EntryLabel association.
     */
    @PostMapping(value = "/{entryId}/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds a label to a specific entry",
            description = "Adds a specified label to a specified entry, whose ids are added to the URL. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully added the given label to the given entry",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntryLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId or the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry or label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addLabelToEntryV1(@Parameter(description = "The entryId added to the URL to retrieve the associated entry.") @PathVariable int entryId,
                                                    @Parameter(description = "The labelId added to the URL to retrieve the associated label.") @PathVariable int labelId,
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
     * Adds a labels to a specific entries.
     *
     * @param entryIds     The IDs of the entries.
     * @param labelIds     The IDs of the labels to add the entries to.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A List of the newly created EntryLabel associations.
     */
    @PostMapping(value = "/{entryIds}/labels/{labelIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adds labels to specific entries",
        description = "Adds specified labels to specified entries, whose ids are added to the URL. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully added the given labels to the given entries",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntryLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of given entryIds and labelIds are not equal or a entryId or a labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given entry or label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addLabelsToEntriesV2(@Parameter(description = "A list of entryIdd added to the URL, in the same order as the associated labelIds, to retrieve the associated entries.") @PathVariable List<Integer> entryIds,
                                                       @Parameter(description = "A list of labelIds added to the URL, in the same order as the associated entryIds, to retrieve the associated labels.") @PathVariable List<Integer> labelIds,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryIds.size() != labelIds.size()) {
                throw new MissingRequiredParameter("the number of entryIds and labelIds must be equal");
            }
            List<DatabaseEntryLabel> entryLabels = new ArrayList<>();
            for (int i = 0; i < entryIds.size(); i++) {
                if (entryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
                } else if (labelIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
                }
                entryLabels.add(entryLabelRepository.addLabelToEntry(entryIds.get(i), labelIds.get(i), userDetails.getUsername()));
            }
            return ResponseEntity.ok(entryLabels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    // DELETE /labels/{labelIds} *****************************************************************************************

    /**
     * Removes a label from a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to remove.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{entryId}/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "removes a label from a specific entry",
            description = "Removes a specified label from a specified entry, whose ids are added to the URL. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully removed the given label from the given entry",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "the entryId or the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry, label or association does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> removeLabelFromEntryV1(@Parameter(description = "The entryId added to the URL to retrieve the associated entry.") @PathVariable int entryId,
                                                         @Parameter(description = "The labelId added to the URL to retrieve the associated label.") @PathVariable int labelId,
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

    /**
     * Removes labels from specific entries.
     *
     * @param entryIds     The IDs of the entries.
     * @param labelIds     The IDs of the labels to remove.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{entryIds}/labels/{labelIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "removes labels from specific entries",
            description = "Removes specified labels from specified entries, whose ids are added to the URL. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully removed the given labels from the given entries",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of entryIds and labelIds are not equal or a entryId or a labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given entry, label or association does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> removeLabelsFromEntriesV2(@Parameter(description = "A list of entryIds added to the URL, in the same order as the associated labelIds, to retrieve the associated entries.") @PathVariable List<Integer> entryIds,
                                                            @Parameter(description = "A list of labelIds added to the URL, in the same order as the associated entryIds, to retrieve the associated labels.") @PathVariable List<Integer> labelIds,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryIds.size() != labelIds.size()) {
                throw new MissingRequiredParameter("the number of entryIds and labelIds must be equal");
            }
            List<Integer> deletedRows = new ArrayList<>();
            for (int i = 0; i < entryIds.size(); i++) {
                if (entryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
                } else if (labelIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
                }
                deletedRows.add(entryLabelRepository.removeLabelFromEntry(entryIds.get(i), labelIds.get(i), userDetails.getUsername()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedRows);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}