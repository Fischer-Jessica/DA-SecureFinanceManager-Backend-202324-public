package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.EntryLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The EntryLabelController class handles HTTP requests related to the management of associations between Entry and Label entities (EntryLabel).
 * It provides endpoints for adding and removing labels from entries, as well as retrieving associated labels for a specific entry.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on EntryLabel associations.
 * It interacts with the EntryLabelRepository to access and manipulate the EntryLabel entities in the 'entry_labels' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The EntryLabelController class works in conjunction with the EntryLabelRepository and other related classes to enable efficient management of Entry-Label associations in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.1
 * @since 21.07.2023 (version 1.1)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/entry-labels")
public class EntryLabelController {
    /** The EntryLabelRepository instance for accessing entry-label data. */
    @Autowired
    EntryLabelRepository entryLabelRepository;

    /**
     * Retrieves a list of labels associated with a specific entry.
     *
     * @param entryId               The ID of the entry.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return A list of labels associated with the entry.
     */
    @GetMapping("/entries/{entryId}/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getLabelsForEntry(@PathVariable int entryId,
                                         @RequestParam int loggedInUserId,
                                         @RequestParam String loggedInUsername,
                                         @RequestParam String loggedInPassword,
                                         @RequestParam String loggedInEMailAddress,
                                         @RequestParam String loggedInFirstName,
                                         @RequestParam String loggedInLastName) {
        return entryLabelRepository.getLabelsForEntry(entryId,
                new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName));
    }

    /**
     * Adds a label to a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to add.
     * @param loggedInUser The logged-in user.
     * @return The ID of the newly created EntryLabel association.
     */
    @PostMapping("/entries/{entryId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public int addLabelToEntry(@PathVariable int entryId,
                               @PathVariable int labelId,
                               @RequestBody User loggedInUser) {
        return entryLabelRepository.addLabelToEntry(entryId, labelId, loggedInUser);
    }

    /**
     * Removes a label from a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to remove.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/entries/{entryId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLabelFromEntry(@PathVariable int entryId,
                                     @PathVariable int labelId,
                                     @RequestBody User loggedInUser) {
        entryLabelRepository.removeLabelFromEntry(entryId, labelId, loggedInUser);
    }
}
