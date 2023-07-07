package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class LabelController {
    @Autowired
    LabelRepository labelRepository;

    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all labels")
    public List<Label> getLabels(@RequestBody User loggedInUser) {
        return labelRepository.getLabels(loggedInUser);
    }

    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one label")
    public Label getLabel(@RequestParam int labelId, @RequestBody User loggedInUser) {
        return labelRepository.getLabel(labelId, loggedInUser);
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "add a new label")
    public int addLabel(@RequestParam byte[] labelName,
                        @RequestParam byte[] labelDescription,
                        @RequestParam int labelColourId,
                        @RequestBody User loggedInUser) {
        return labelRepository.addLabel(labelName, labelDescription, labelColourId, loggedInUser);
    }

    @PatchMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing label")
    public void updateLabel(@RequestBody Label updatedLabel, @RequestBody User loggedInUser) {
        labelRepository.updateLabel(updatedLabel, loggedInUser);
    }

    @PatchMapping("labels/labelName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of the label")
    public void updateLabelName(@RequestParam int labelId, @RequestParam byte[] labelName, @RequestBody User loggedInUser) {
        labelRepository.updateLabelName(labelId, labelName, loggedInUser);
    }

    @PatchMapping("labels/labelDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of the label")
    public void updateLabelDescription(@RequestParam int labelId, @RequestParam byte[] labelDescription, @RequestBody User loggedInUser) {
        labelRepository.updateLabelDescription(labelId, labelDescription, loggedInUser);
    }

    @PatchMapping("labels/labelColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing label")
    public void updateLabelColour(@RequestParam int labelId, @RequestParam int labelColourId, @RequestBody User loggedInUser) {
        labelRepository.updateLabelColourId(labelId, labelColourId, loggedInUser);
    }

    @DeleteMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a label")
    public void deleteCategory(@RequestParam int labelId, @RequestBody User loggedInUser) {
        labelRepository.deleteLabel(labelId, loggedInUser);
    }
}
