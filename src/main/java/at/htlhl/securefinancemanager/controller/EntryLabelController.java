package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.repository.EntryLabelRepository;
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
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on EntryLabel associations.
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
 * to enable efficient management of Entry-Label associations in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.8
 * @since 14.11.2023 (version 1.8)
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
     * @return The ID of the newly created EntryLabel association.
     */
    @PostMapping(value = "/entries/{entryId}/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
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
        }
    }

    /**
     * Removes a label from a specific entry.
     *
     * @param entryId      The ID of the entry.
     * @param labelId      The ID of the label to remove.
     * @param userDetails  The UserDetails object representing the logged-in user.
     */
    @DeleteMapping(value = "/entries/{entryId}/labels/{labelId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> removeLabelFromEntry(@PathVariable int entryId,
                                                       @PathVariable int labelId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            } else if (labelId <= 0) {
                throw new MissingRequiredParameter("labelId cannot be less than or equal to 0");
            }
            entryLabelRepository.removeLabelFromEntry(entryId, labelId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}
