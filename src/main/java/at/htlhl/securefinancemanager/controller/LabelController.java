package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Label;
import at.htlhl.securefinancemanager.repository.LabelRepository;
import at.htlhl.securefinancemanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * The LabelController class handles the HTTP requests related to label management.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Label entities.
 * It interacts with the LabelRepository to access and manipulate the Label entities in the 'labels' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The LabelController class works in conjunction with the LabelRepository and other related classes to enable efficient management of labels in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.2
 * @since 15.10.2023 (version 2.2)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/labels")
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
        try {
            return ResponseEntity.ok(labelRepository.getLabels(userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId The ID of the label to retrieve.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The requested label.
     */
    @GetMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one label")
    public ResponseEntity<Object> getLabel(@PathVariable int labelId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(labelRepository.getLabel(labelId, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param newLabel The Label object representing the new label.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The ID of the newly created label.
     */
    @PostMapping(value = "/labels", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "add a new label")
    public ResponseEntity<Object> addLabel(@RequestBody Label newLabel,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            newLabel.setLabelUserId(UserRepository.getUserId(userDetails.getUsername()));
            return ResponseEntity.ok(labelRepository.addLabel(newLabel));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param labelId The ID of the label that will be changed.
     * @param updatedLabel The Label object with updated information.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return The updated Label object.
     */
    @PatchMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing label")
    public ResponseEntity<Object> updateLabel(@PathVariable int labelId,
                                              @RequestBody Label updatedLabel,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // TODO: That does not feel clean
            updatedLabel.setLabelId(labelId);
            updatedLabel.setLabelUserId(UserRepository.getUserId(userDetails.getUsername()));
            return ResponseEntity.ok(labelRepository.updateLabel(updatedLabel, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId The ID of the label to delete.
     * @param userDetails The UserDetails object representing the logged-in user.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete a label")
    public ResponseEntity<Object> deleteCategory(@PathVariable int labelId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            labelRepository.deleteLabel(labelId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
