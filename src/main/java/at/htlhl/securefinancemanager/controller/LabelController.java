package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.model.api.ApiLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import at.htlhl.securefinancemanager.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
 * to enable efficient management of labels in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.7
 * @since 14.11.2023 (version 2.7)
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
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return A list of labels.
     */
    @GetMapping(value = "/labels", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all labels")
    public ResponseEntity<Object> getLabels(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(labelRepository.getLabels(userDetails.getUsername()));
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId      The ID of the label to retrieve.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The requested label.
     */
    @GetMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one label")
    public ResponseEntity<Object> getLabel(@PathVariable int labelId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(labelRepository.getLabel(labelId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param newApiLabel The Label object representing the new label.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The ID of the newly created label.
     */
    @PostMapping(value = "/labels", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "add a new label")
    public ResponseEntity<Object> addLabel(@RequestBody ApiLabel newApiLabel,
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
     * Updates an existing label for the logged-in user.
     *
     * @param labelId The ID of the label that will be changed.
     * @param updatedApiLabel The Label object with updated information.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The updated Label object.
     */
    @PatchMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing label")
    public ResponseEntity<Object> updateLabel(@PathVariable int labelId,
                                              @RequestBody ApiLabel updatedApiLabel,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (updatedApiLabel.getLabelColourId() < 0) {
                throw new MissingRequiredParameter("labelColour can not be negative, use 0 for no colour change");
            } else if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(labelRepository.updateLabel(new DatabaseLabel(labelId, updatedApiLabel, userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId      The ID of the label to delete.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete a label")
    public ResponseEntity<Object> deleteCategory(@PathVariable int labelId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            labelRepository.deleteLabel(labelId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }
}
