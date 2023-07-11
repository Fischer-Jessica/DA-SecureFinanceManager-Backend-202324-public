package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Subcategory;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.SubcategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The SubcategoryController class handles the HTTP requests related to subcategories.
 *
 * @author Fischer
 * @version 1.2
 * @since 11.07.2023 (version 1.2)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}")
public class SubcategoryController {
    /** The CategoryRepository instance for accessing user data. */
    @Autowired
    SubcategoryRepository subcategoryRepository;

    /**
     * Returns a list of all subcategories for a specific category.
     *
     * @param categoryId  The ID of the category.
     * @param loggedInUser The logged-in user.
     * @return A list of subcategories for the specified category.
     */
    @GetMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all subcategories of one category")
    public List<Subcategory> getSubcategories(@PathVariable int categoryId, @RequestBody User loggedInUser) {
        return subcategoryRepository.getSubcategories(categoryId, loggedInUser);
    }

    /**
     * Returns a specific subcategory for a specific category.
     *
     * @param categoryId     The ID of the category.
     * @param subcategoryId  The ID of the subcategory.
     * @param loggedInUser The logged-in user.
     * @return The requested subcategory.
     */
    @GetMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one subcategory")
    public Subcategory getSubcategory(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestBody User loggedInUser) {
        return subcategoryRepository.getSubcategory(categoryId, subcategoryId, loggedInUser);
    }

    /**
     * Adds a new subcategory to a specific category.
     *
     * @param categoryId              The ID of the category.
     * @param subcategoryName         The name of the subcategory.
     * @param subcategoryDescription  The description of the subcategory.
     * @param subcategoryColourId     The ID of the color for the subcategory.
     * @param loggedInUser         The logged-in user.
     * @return The ID of the newly created subcategory.
     */
    @PostMapping("/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new subcategory")
    public int addSubcategory(@PathVariable int categoryId, @RequestParam byte[] subcategoryName,
                              @RequestParam byte[] subcategoryDescription, @RequestParam int subcategoryColourId,
                              @RequestBody User loggedInUser) {
        return subcategoryRepository.addSubcategory(categoryId, subcategoryName, subcategoryDescription, subcategoryColourId, loggedInUser);
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId         The ID of the category.
     * @param updatedSubcategory The updated subcategory.
     * @param loggedInUser     The logged-in user.
     */
    @PatchMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public void updateSubcategory(@PathVariable int categoryId, @RequestBody Subcategory updatedSubcategory, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategory(categoryId, updatedSubcategory, loggedInUser);
    }

    /**
     * Changes the name of an existing subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param updatedSubcategoryName The updated subcategory name.
     * @param loggedInUser        The logged-in user.
     */
    @PatchMapping("/subcategories/subcategoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing subcategory")
    public void updateSubcategoryName(@PathVariable int categoryId, @RequestBody byte[] updatedSubcategoryName, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryName(categoryId, updatedSubcategoryName, loggedInUser);
    }

    /**
     * Changes the description of an existing subcategory for a specific category.
     *
     * @param categoryId                 The ID of the category.
     * @param updatedSubcategoryDescription The updated subcategory description.
     * @param loggedInUser               The logged-in user.
     */
    @PatchMapping("/subcategories/subcategoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing subcategory")
    public void updateSubcategoryDescription(@PathVariable int categoryId, @RequestBody byte[] updatedSubcategoryDescription,
                                             @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryDescription(categoryId, updatedSubcategoryDescription, loggedInUser);
    }

    /**
     * Changes the colour of an existing subcategory for a specific category.
     *
     * @param categoryId                The ID of the category.
     * @param updatedSubcategoryColour  The updated subcategory colour ID.
     * @param loggedInUser            The logged-in user.
     */
    @PatchMapping("/subcategories/subcategoryColour")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing subcategory")
    public void updateSubcategoryColour(@PathVariable int categoryId, @RequestParam int updatedSubcategoryColour, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryColour(categoryId, updatedSubcategoryColour, loggedInUser);
    }

    /**
     * Deletes a subcategory for a specific category.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a subcategory")
    public void deleteSubcategory(@PathVariable int categoryId, @RequestParam int subcategoryId, @RequestBody User loggedInUser) {
        subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
    }
}
