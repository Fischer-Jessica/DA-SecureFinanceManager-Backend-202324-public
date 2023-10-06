package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Label;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
 * @version 1.9
 * @since 06.10.2023 (version 1.9)
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
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return A list of labels.
     */
    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all labels")
    public ResponseEntity<Object> getLabels(@RequestParam int loggedInUserId,
                                             @RequestParam String loggedInUsername,
                                             @RequestParam String loggedInPassword,
                                             @RequestParam String loggedInEMailAddress,
                                             @RequestParam String loggedInFirstName,
                                             @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(labelRepository.getLabels(new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId               The ID of the label to retrieve.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return The requested label.
     */
    @GetMapping("/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one label")
    public ResponseEntity<Object> getLabel(@PathVariable int labelId,
                                          @RequestParam int loggedInUserId,
                                          @RequestParam String loggedInUsername,
                                          @RequestParam String loggedInPassword,
                                          @RequestParam String loggedInEMailAddress,
                                          @RequestParam String loggedInFirstName,
                                          @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(labelRepository.getLabel(labelId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param labelName        The name of the new label.
     * @param labelDescription The description of the new label.
     * @param labelColourId    The ID of the color for the new label.
     * @param loggedInUser     The logged-in user.
     * @return The ID of the newly created label.
     */
    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "add a new label")
    public ResponseEntity<Object> addLabel(@RequestParam String labelName,
                                            @RequestParam String labelDescription,
                                            @RequestParam int labelColourId,
                                            @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(labelRepository.addLabel(labelName, labelDescription, labelColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param labelId                       The ID of the label that will be changed.
     * @param updatedLabelName              The updated name of the label.
     * @param updatedLabelDescription       The updated description of the label.
     * @param updatedLabelColour            The updated colour of the label.
     * @param loggedInUser                  The logged-in user.
     */
    @PatchMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing label")
    public ResponseEntity<Object> updateLabel(@RequestParam int labelId,
                                      @RequestParam(required = false) String updatedLabelName,
                                      @RequestParam(required = false) String updatedLabelDescription,
                                      @RequestParam(defaultValue = "-1", required = false) int updatedLabelColour,
                                      @RequestBody User loggedInUser) {
        try {
            labelRepository.updateLabel(new Label(labelId, updatedLabelName, updatedLabelDescription, updatedLabelColour, loggedInUser.getUserId()),
                    loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId      The ID of the label to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a label")
    public ResponseEntity<Object> deleteCategory(@RequestParam int labelId,
                                         @RequestBody User loggedInUser) {
        try {
            labelRepository.deleteLabel(labelId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
