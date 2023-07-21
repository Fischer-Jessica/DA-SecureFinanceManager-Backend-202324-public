package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.EntryLabel;
import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.repository.CategoryRepository;
import at.htlhl.financialoverview.repository.EntryLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
