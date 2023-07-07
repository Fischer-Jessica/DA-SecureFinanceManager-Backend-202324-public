package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Subcategory;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.SubcategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}")
public class SubcategoryController {
    @Autowired
    SubcategoryRepository subcategoryRepository;

    @GetMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all subcategories of one category")
    public List<Subcategory> getSubcategories(@PathVariable int categoryId, @RequestParam User loggedInUser) {
        return subcategoryRepository.getSubcategories(categoryId, loggedInUser);
    }

    @GetMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one subcategory")
    public Subcategory getSubcategory(@PathVariable int categoryId, @RequestParam int subcategoryId, @RequestParam User loggedInUser) {
        return subcategoryRepository.getSubcategory(categoryId, subcategoryId, loggedInUser);
    }

    @PostMapping("/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new subcategory")
    public int addSubcategory(@PathVariable int categoryId, @RequestParam byte[] subcategoryName,
                              @RequestParam byte[] subcategoryDescription, @RequestParam int subcategoryColourId,
                              @RequestParam User loggedInUser) {
        return subcategoryRepository.addSubcategory(categoryId, subcategoryName, subcategoryDescription, subcategoryColourId, loggedInUser);
    }

    @PatchMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public void updateSubcategory(@PathVariable int categoryId, @RequestBody Subcategory updatedSubcategory, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategory(categoryId, updatedSubcategory, loggedInUser);
    }

    @PatchMapping("/subcategories/subcategoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing subcategory")
    public void updateSubcategoryName(@PathVariable int categoryId, @RequestBody byte[] updatedSubcategoryName, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryName(categoryId, updatedSubcategoryName, loggedInUser);
    }

    @PatchMapping("/subcategories/subcategoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing subcategory")
    public void updateSubcategoryDescription(@PathVariable int categoryId, @RequestBody byte[] updatedSubcategoryDescription,
                                             @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryDescription(categoryId, updatedSubcategoryDescription, loggedInUser);
    }

    @PatchMapping("/subcategories/subcategoryColour")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing subcategory")
    public void updateSubcategoryColour(@PathVariable int categoryId, @RequestParam int updatedSubcategoryColour, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryColour(categoryId, updatedSubcategoryColour, loggedInUser);
    }

    @DeleteMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a subcategory")
    public void deleteSubcategory(@PathVariable int categoryId, @RequestParam int subcategoryId, @RequestBody User loggedInUser) {
        subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
    }
}
