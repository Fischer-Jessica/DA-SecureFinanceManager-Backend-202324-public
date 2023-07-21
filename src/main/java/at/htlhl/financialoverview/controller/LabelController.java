package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The LabelController class handles the HTTP requests related to label management.
 *
 * @author Fischer
 * @version 1.3
 * @since 21.07.2023 (version 1.3)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class LabelController {
    /** The LabelRepository instance for accessing user data. */
    @Autowired
    LabelRepository labelRepository;

    /**
     * Returns a list of all labels for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of labels.
     */
    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all labels")
    public List<Label> getLabels(@RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return labelRepository.getLabels(new User(userId, username, password, eMailAddress, firstName, lastName));
    }

    /**
     * Returns a specific label for the logged-in user.
     *
     * @param labelId      The ID of the label to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested label.
     */
    @GetMapping("/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one label")
    public Label getLabel(@PathVariable int labelId,
                          @RequestParam int userId, @RequestParam String username, @RequestParam String eMailAddress, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName) {
        return labelRepository.getLabel(labelId, new User(userId, username, password, eMailAddress, firstName, lastName));
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
    public int addLabel(@RequestParam String labelName,
                        @RequestParam String labelDescription,
                        @RequestParam int labelColourId,
                        @RequestBody User loggedInUser) {
        return labelRepository.addLabel(labelName, labelDescription, labelColourId, loggedInUser);
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param updatedLabel  The updated label.
     * @param loggedInUser The logged-in user.
     */
    @PatchMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing label")
    public void updateLabel(@RequestParam int labelId, @RequestParam String updatedLabelName, @RequestParam String updatedLabelDescription, @RequestParam int updatedLabelColour
            , @RequestBody User loggedInUser) {
        labelRepository.updateLabel(new Label(labelId, updatedLabelName, updatedLabelDescription, updatedLabelColour, loggedInUser.getUserId()), loggedInUser);
    }

    /**
     * Changes the name of an existing label for the logged-in user.
     *
     * @param labelId       The ID of the label to update.
     * @param labelName     The updated label name.
     * @param loggedInUser The logged-in user.
     */
    @PatchMapping("labels/labelName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of the label")
    public void updateLabelName(@RequestParam int labelId, @RequestParam String labelName, @RequestBody User loggedInUser) {
        labelRepository.updateLabelName(labelId, labelName, loggedInUser);
    }

    /**
     * Changes the description of an existing label for the logged-in user.
     *
     * @param labelId          The ID of the label to update.
     * @param labelDescription The updated label description.
     * @param loggedInUser    The logged-in user.
     */
    @PatchMapping("labels/labelDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of the label")
    public void updateLabelDescription(@RequestParam int labelId, @RequestParam String labelDescription, @RequestBody User loggedInUser) {
        labelRepository.updateLabelDescription(labelId, labelDescription, loggedInUser);
    }

    /**
     * Changes the color of an existing label for the logged-in user.
     *
     * @param labelId         The ID of the label to update.
     * @param labelColourId   The updated label color ID.
     * @param loggedInUser The logged-in user.
     */
    @PatchMapping("labels/labelColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing label")
    public void updateLabelColour(@RequestParam int labelId, @RequestParam int labelColourId, @RequestBody User loggedInUser) {
        labelRepository.updateLabelColourId(labelId, labelColourId, loggedInUser);
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId       The ID of the label to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a label")
    public void deleteCategory(@RequestParam int labelId, @RequestBody User loggedInUser) {
        labelRepository.deleteLabel(labelId, loggedInUser);
    }
}
