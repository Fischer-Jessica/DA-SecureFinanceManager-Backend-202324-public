package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import at.htlhl.securefinancemanager.model.response.ResponseLabel;
import at.htlhl.securefinancemanager.repository.LabelRepository;
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
 * @fullName Fischer, Jessica Christina
 * @version 4.0
 * @since 09.02.2024 (version 4.0)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class LabelController {
    /**
     * The LabelRepository instance for accessing label data.
     */
    @Autowired
    LabelRepository labelRepository;

    /**
     * Returns a list of all labels for the logged-in user.
     *
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return A list of all labels.
     */
    @GetMapping(value = "/labels", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all labels of the authenticated user",
            description = "Returns a list of all labels created by the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all labels of the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "404", description = "no labels found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getLabelsV1(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(labelRepository.getLabels(userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId     The ID of the label to retrieve.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The requested label.
     */
    @GetMapping(value = "/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one label",
            description = "Returns the label with the specified labelId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested label",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the requested label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getLabelV1(@Parameter(description = "The labelId added to the URL to retrieve the associated label.") @PathVariable int labelId,
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
     * Retrieves the sum of all transactions for entries associated with the specified label for the authenticated user.
     *
     * @param labelId     The ID of the label for which to retrieve the sum of transactions.
     * @param userDetails The UserDetails object containing information about the authenticated user.
     * @return The sum of transactions for entries associated with the specified label for the authenticated user.
     */
    @GetMapping(value = "labels/{labelId}/sum", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns the sum of all transactions for entries with a specific label",
            description = "Returns the sum of all transactions for entries associated with the specified label ID for the authenticated user. Requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the sum of transactions for entries with the specified label",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))}),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the specified label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getLabelSumV1(@Parameter(description = "The labelId added to the URL to retrieve the sum of all transactions for entries with the specified label.") @PathVariable int labelId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            float totalSum = labelRepository.getValueOfLabel(labelId, userDetails.getUsername());
            return ResponseEntity.ok(totalSum);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param newApiLabel The Label object representing the new label.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The newly created label.
     */
    @PostMapping(value = "/labels", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "adds a new label for the authenticated user",
            description = "Creates a new label for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given label for the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "the labelName is empty or the labelColourId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addLabelV1(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The new Label-Object.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiLabel.class)
            )
    ) @RequestBody ApiLabel newApiLabel,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (newApiLabel.getLabelName() == null || newApiLabel.getLabelName().isBlank()) {
                throw new MissingRequiredParameter("labelName is required");
            } else if (newApiLabel.getLabelColourId() <= 0) {
                throw new MissingRequiredParameter("labelColour cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(labelRepository.addLabel(new DatabaseLabel(newApiLabel, userSingleton.getUserId(userDetails.getUsername()))));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds new labels for the logged-in user.
     *
     * @param newApiLabels The Labels representing the new labels.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A List of the newly created labels.
     */
    @PostMapping(value = "/labels", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "adds new labels for the authenticated user",
            description = "Creates new labels for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given labels for the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "a labelName is empty or a labelColourId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addLabelsV2(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of new labels. As it is a List, the objects need to be enclosed in [].",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiLabel.class)
            )
    ) @RequestBody List<ApiLabel> newApiLabels,
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
     * Adds new labels for the logged-in user.
     *
     * @param mobileLabelIds The IDs of the labels in the mobile application.
     * @param newApiLabels   The Labels representing the new labels.
     * @param userDetails    The UserDetails object representing the logged-in user.
     * @return A List of the newly created labels.
     */
    @PostMapping(value = "/labels", headers = "API-Version=3")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "adds new labels for the authenticated user",
            description = "Creates new Labels for the authenticated user. It requires a Basic-Auth-Header. This version for mobile applications also requires a List of mobileLabelIds added to the URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given labels for the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "the number of mobileLabelIds and newApiLabels are not equal or a labelName is empty or a mobileLabelId or a labelColourId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addLabelsV3(@Parameter(description = "List of mobileLabelIds from mobile applications to be added to the URL. The mobileLabelIds and newApiLabels must be in the same order.") @RequestParam List<Integer> mobileLabelIds,
                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                      description = "List of new labels. As it is a List, the objects need to be enclosed in [].",
                                                      required = true,
                                                      content = @Content(
                                                              mediaType = "application/json",
                                                              schema = @Schema(implementation = ApiLabel.class)
                                                      )
                                              ) @RequestBody List<ApiLabel> newApiLabels,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ResponseLabel> createdLabels = new ArrayList<>();
            if (mobileLabelIds.size() != newApiLabels.size()) {
                throw new MissingRequiredParameter("the number of mobileLabelIds and newApiLabels must be equal");
            }
            for (int i = 0; i < newApiLabels.size(); i++) {
                if (newApiLabels.get(i).getLabelName() == null || newApiLabels.get(i).getLabelName().isBlank()) {
                    throw new MissingRequiredParameter("labelName is required");
                } else if (newApiLabels.get(i).getLabelColourId() <= 0) {
                    throw new MissingRequiredParameter("labelColour cannot be less than or equal to 0");
                }
                createdLabels.add(new ResponseLabel(labelRepository.addLabel(new DatabaseLabel(newApiLabels.get(i), userSingleton.getUserId(userDetails.getUsername()))), mobileLabelIds.get(i)));
            }
            return ResponseEntity.ok(createdLabels);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param labelId         The ID of the label that will be changed.
     * @param updatedApiLabel The Label object with updated information.
     * @param userDetails     The UserDetails object representing the logged-in user.
     * @return The updated Label object.
     */
    @PatchMapping(value = "/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates an existing label",
            description = "Updates an existing label, whose id is given in the URL, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given label",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0 or the labelColourId is less than 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateLabelV1(@Parameter(description = "The id of the label to be updated, added to the URL.") @PathVariable int labelId,
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        description = "The updated label-Object.",
                                                        required = true,
                                                        content = @Content(
                                                                mediaType = "application/json",
                                                                schema = @Schema(implementation = ApiLabel.class)
                                                        )
                                                ) @RequestBody ApiLabel updatedApiLabel,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (updatedApiLabel.getLabelColourId() < 0) {
                throw new MissingRequiredParameter("labelColourId can not be negative, use 0 for no colour change");
            } else if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(labelRepository.updateLabel(new DatabaseLabel(labelId, updatedApiLabel, userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates existing labels for the logged-in user.
     *
     * @param labelIds         The IDs of the labels that will be changed.
     * @param updatedApiLabels The updated labels.
     * @param userDetails      The UserDetails object representing the logged-in user.
     * @return A List of the updated labels.
     */
    @PatchMapping(value = "/labels/{labelIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing labels",
            description = "Updates existing labels, whose ids are given in the URL, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given labels",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseLabel.class))}),
            @ApiResponse(responseCode = "400", description = "the number of labelIds and updatedLabels are not equal or a labelId is less than or equal to 0 or a labelColourId is less than 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateLabelsV2(@Parameter(description = "List of labelIds to be updated. They need to be added to the URL in the same order as the updatedApiLabels.") @PathVariable List<Integer> labelIds,
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                         description = "List of the updated labels. As it is a List, the objects need to be enclosed in [].",
                                                         required = true,
                                                         content = @Content(
                                                                 mediaType = "application/json",
                                                                 schema = @Schema(implementation = ApiLabel.class)
                                                         )
                                                 ) @RequestBody List<ApiLabel> updatedApiLabels,
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
     * Deletes a label for the logged-in user.
     *
     * @param labelId     The ID of the label to delete.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/labels/{labelId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes a label",
            description = "Deletes a label, whose id is given in the URL, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given label",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "the labelId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteCategoryV1(@Parameter(description = "The id of the label to be deleted, added to the URL.") @PathVariable int labelId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.status(HttpStatus.OK).body(labelRepository.deleteLabel(labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes labels for the logged-in user.
     *
     * @param labelIds    The IDs of the labels to delete.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/labels/{labelIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes labels",
            description = "Deletes labels, whose ids are given in the URL, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given labels",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "a labelId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given label does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteLabelsV2(@Parameter(description = "List of labelIds to be deleted. They need to be added to the URL.") @PathVariable List<Integer> labelIds,
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