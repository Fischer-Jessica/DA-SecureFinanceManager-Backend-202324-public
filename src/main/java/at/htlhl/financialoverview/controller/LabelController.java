package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.exception.ValidationException;
import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
 * @version 1.5
 * @since 25.07.2023 (version 1.5)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/labels")
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
    public ResponseEntity<List<Label>> getLabels(@RequestParam int loggedInUserId,
                                                 @RequestParam String loggedInUsername,
                                                 @RequestParam String loggedInPassword,
                                                 @RequestParam String loggedInEMailAddress,
                                                 @RequestParam String loggedInFirstName,
                                                 @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(labelRepository.getLabels(new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<Label> getLabel(@PathVariable int labelId,
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<Integer> addLabel(@RequestParam String labelName,
                                            @RequestParam String labelDescription,
                                            @RequestParam int labelColourId,
                                            @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(labelRepository.addLabel(labelName, labelDescription, labelColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity updateLabel(@RequestParam int labelId,
                                      @RequestParam String updatedLabelName,
                                      @RequestParam String updatedLabelDescription,
                                      @RequestParam int updatedLabelColour,
                                      @RequestBody User loggedInUser) {
        try {
            labelRepository.updateLabel(new Label(labelId, updatedLabelName, updatedLabelDescription, updatedLabelColour, loggedInUser.getUserId()),
                    loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the name of an existing label for the logged-in user.
     *
     * @param labelId          The ID of the label to update.
     * @param updatedLabelName The updated label name.
     * @param loggedInUser     The logged-in user.
     */
    @PatchMapping("labels/labelName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of the label")
    public ResponseEntity updateLabelName(@RequestParam int labelId,
                                          @RequestParam String updatedLabelName,
                                          @RequestBody User loggedInUser) {
        try {
            labelRepository.updateLabelName(labelId, updatedLabelName, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the description of an existing label for the logged-in user.
     *
     * @param labelId                 The ID of the label to update.
     * @param updatedLabelDescription The updated label description.
     * @param loggedInUser            The logged-in user.
     */
    @PatchMapping("labels/labelDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of the label")
    public ResponseEntity updateLabelDescription(@RequestParam int labelId,
                                                 @RequestParam String updatedLabelDescription,
                                                 @RequestBody User loggedInUser) {
        try {
            labelRepository.updateLabelDescription(labelId, updatedLabelDescription, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the color of an existing label for the logged-in user.
     *
     * @param labelId              The ID of the label to update.
     * @param updatedLabelColourId The updated label color ID.
     * @param loggedInUser         The logged-in user.
     */
    @PatchMapping("labels/labelColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing label")
    public ResponseEntity updateLabelColour(@RequestParam int labelId,
                                            @RequestParam int updatedLabelColourId,
                                            @RequestBody User loggedInUser) {
        try {
            labelRepository.updateLabelColourId(labelId, updatedLabelColourId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity deleteCategory(@RequestParam int labelId,
                                         @RequestBody User loggedInUser) {
        try {
            labelRepository.deleteLabel(labelId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
