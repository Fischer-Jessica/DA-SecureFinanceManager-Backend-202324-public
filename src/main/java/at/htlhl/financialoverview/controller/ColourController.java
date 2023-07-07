package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Colour;
import at.htlhl.financialoverview.repository.ColourRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class ColourController {
    @Autowired
    ColourRepository colourRepository;

    @GetMapping("/colours")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all colours")
    public List<Colour> getColours() {
        return colourRepository.getColours();
    }

    @GetMapping("/colours")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one colour")
    public Colour getColour(@RequestParam int colourId) {
        return colourRepository.getColour(colourId);
    }
}
