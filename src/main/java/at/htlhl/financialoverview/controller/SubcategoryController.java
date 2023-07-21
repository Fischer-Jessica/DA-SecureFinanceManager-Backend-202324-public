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
 * @version 1.3
 * @since 21.07.2023 (version 1.3)
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
    public List<Subcategory> getSubcategories(@PathVariable int categoryId, @RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return subcategoryRepository.getSubcategories(categoryId, new User(userId, username, password, eMailAddress, firstName, lastName));
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
    public Subcategory getSubcategory(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                      @RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return subcategoryRepository.getSubcategory(categoryId, subcategoryId, new User(userId, username, password, eMailAddress, firstName, lastName));
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
    public int addSubcategory(@PathVariable int categoryId, @RequestParam String subcategoryName,
                              @RequestParam String subcategoryDescription, @RequestParam int subcategoryColourId,
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
    @PatchMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public void updateSubcategory(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestParam String updatedSubcategoryName, @RequestParam String updatedSubcategoryDescription, @RequestParam int updatedSubcategoryColour,
                                  @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategory(categoryId, subcategoryId, updatedSubcategoryName, updatedSubcategoryDescription, updatedSubcategoryColour, loggedInUser);
    }

    @PostMapping("/subcategories/{subcategoryId}/categoryOfSubcategory")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the category of a subcategory")
    public void updateCategoryOfSubcategory(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestBody User loggedInUser) {
        subcategoryRepository.updateCategoryOfSubcategory(categoryId, subcategoryId, loggedInUser);
    }

    /**
     * Changes the name of an existing subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param updatedSubcategoryName The updated subcategory name.
     * @param loggedInUser        The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing subcategory")
    public void updateSubcategoryName(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestParam String updatedSubcategoryName, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryName(categoryId, subcategoryId, updatedSubcategoryName, loggedInUser);
    }

    /**
     * Changes the description of an existing subcategory for a specific category.
     *
     * @param categoryId                 The ID of the category.
     * @param updatedSubcategoryDescription The updated subcategory description.
     * @param loggedInUser               The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing subcategory")
    public void updateSubcategoryDescription(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestParam String updatedSubcategoryDescription,
                                             @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryDescription(categoryId, subcategoryId, updatedSubcategoryDescription, loggedInUser);
    }

    /**
     * Changes the colour of an existing subcategory for a specific category.
     *
     * @param categoryId                The ID of the category.
     * @param updatedSubcategoryColour  The updated subcategory colour ID.
     * @param loggedInUser            The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryColour")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing subcategory")
    public void updateSubcategoryColour(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestParam int updatedSubcategoryColour, @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryColour(categoryId, subcategoryId, updatedSubcategoryColour, loggedInUser);
    }

    /**
     * Deletes a subcategory for a specific category.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a subcategory")
    public void deleteSubcategory(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestBody User loggedInUser) {
        subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
    }
}
