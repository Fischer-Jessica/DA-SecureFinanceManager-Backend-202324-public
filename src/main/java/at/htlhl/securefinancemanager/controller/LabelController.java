package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import at.htlhl.securefinancemanager.repository.LabelRepository;
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

import java.util.ArrayList;
import java.util.List;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The LabelController class handles HTTP requests related to label management.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Label entities.
 * It interacts with the LabelRepository to access and manipulate the Label entities
 * in the 'labels' table of the 'secure_finance_manager' PostgreSQL database.
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
 * The LabelController class works in conjunction with the LabelRepository and other related classes
 * to enable efficient management of labels in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @version 3.3
 * @since 17.11.2023 (version 3.3)
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class LabelController {
    /** The LabelRepository instance for accessing label data. */
    @Autowired
    LabelRepository labelRepository;

    /**
     * Returns a list of all labels for the logged-in user.
     *
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return A list of all labels.
     */
    @GetMapping(value = "/labels", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all labels of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all labels of the authenticated user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "404", description = "no labels found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getLabels(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(labelRepository.getLabels(userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId      The ID of the label to retrieve.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The requested label.
     */
    @GetMapping(value = "/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested label",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getLabel(@PathVariable int labelId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(labelRepository.getLabel(labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds new labels for the logged-in user.
     *
     * @param newApiLabels      The Labels representing the new labels.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return A List of the newly created labels.
     */
    @PostMapping(value = "/labels", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "adds new labels for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given labels for the authenticated user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "a labelName is empty or a labelColourId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addLabels(@RequestBody List<ApiLabel> newApiLabels,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<DatabaseLabel> createdLabels = new ArrayList<>();
            for (ApiLabel newApiLabel : newApiLabels) {
                if (newApiLabel.getLabelName() == null || newApiLabel.getLabelName().isBlank()) {
                    throw new MissingRequiredParameter("labelName is required");
                } else if (newApiLabel.getLabelColourId() <= 0) {
                    throw new MissingRequiredParameter("labelColour cannot be less than or equal to 0");
                }
                createdLabels.add(labelRepository.addLabel(new DatabaseLabel(newApiLabel, userSingleton.getUserId(userDetails.getUsername()))));
            }
            return ResponseEntity.ok(createdLabels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates existing labels for the logged-in user.
     *
     * @param labelIds              The IDs of the labels that will be changed.
     * @param updatedApiLabels      The updated labels.
     * @param userDetails           The UserDetails object representing the logged-in user.
     * @return A List of the updated labels.
     */
    @PatchMapping(value = "/labels/{labelId}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given labels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of labelIds and updatedLabels are not equal or a labelId is less than or equal to 0 or a labelColourId is less than 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> updateLabels(@PathVariable List<Integer> labelIds,
                                               @RequestBody List<ApiLabel> updatedApiLabels,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelIds.size() != updatedApiLabels.size()) {
                throw new MissingRequiredParameter("the number of labelIds and updatedLabels must be equal");
            }
            List<DatabaseLabel> updatedLabels = new ArrayList<>();
            for (int i = 0; i < updatedApiLabels.size(); i++) {
                if (updatedApiLabels.get(i).getLabelColourId() < 0) {
                    throw new MissingRequiredParameter("labelColourId can not be negative, use 0 for no colour change");
                } else if (labelIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
                }
                updatedLabels.add(labelRepository.updateLabel(new DatabaseLabel(labelIds.get(i), updatedApiLabels.get(i), userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
            }
            return ResponseEntity.ok(updatedLabels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes labels for the logged-in user.
     *
     * @param labelIds      The IDs of the labels to delete.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/labels/{labelId}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given labels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "a labelId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given label does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> deleteLabels(@PathVariable List<Integer> labelIds,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<Integer> deletedLabels = new ArrayList<>();
            for (int labelId : labelIds) {
                if (labelId <= 0) {
                    throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
                }
                deletedLabels.add(labelRepository.deleteLabel(labelId, userDetails.getUsername()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedLabels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}
