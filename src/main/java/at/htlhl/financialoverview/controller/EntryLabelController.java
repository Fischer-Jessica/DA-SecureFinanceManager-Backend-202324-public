package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.EntryLabel;
import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.repository.CategoryRepository;
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
 * @version 1.0
 * @since 21.07.2023 (version 1.0)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class EntryLabelController {
    @Autowired
    EntryLabelRepository entryLabelRepository;

    // Methode zum Abrufen von Labels für einen bestimmten Eintrag
    @GetMapping("/entries/{entryId}/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getLabelsForEntry(@PathVariable int entryId) {
        return entryLabelRepository.getLabelsForEntry(entryId);
    }

    // Methode zum Hinzufügen von Labels zu einem bestimmten Eintrag
    @PostMapping("/entries/{entryId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLabelToEntry(@PathVariable int entryId, @PathVariable int labelId) {
        entryLabelRepository.addLabelToEntry(entryId, labelId);
    }

    // Methode zum Entfernen von Labels von einem bestimmten Eintrag
    @DeleteMapping("/entries/{entryId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLabelFromEntry(@PathVariable int entryId, @PathVariable int labelId) {
        entryLabelRepository.removeLabelFromEntry(entryId, labelId);
    }
}
