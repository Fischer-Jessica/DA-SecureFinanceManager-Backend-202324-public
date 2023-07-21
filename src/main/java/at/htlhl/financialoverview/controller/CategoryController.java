package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The CategoryController class handles the HTTP requests related to category management.
 *
 * @author Fischer
 * @version 1.4
 * @since 21.07.2023 (version 1.4)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class CategoryController {
    /** The CategoryRepository instance for accessing user data. */
    @Autowired
    CategoryRepository categoryRepository;

    /**
     * Returns a list of all categories for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of categories.
     */
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all categories")
    public List<Category> getCategories(@RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return categoryRepository.getCategories(new User(userId, username, password, eMailAddress, firstName, lastName));
    }

    /**
     * Returns a specific category for the logged-in user.
     *
     * @param categoryId   The ID of the category to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested category.
     */
    @GetMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one category")
    public Category getCategory(@PathVariable int categoryId,
                                @RequestParam int loggedInUserId, @RequestParam String loggedInUserUsername, @RequestParam String loggedInUserPassword, @RequestParam String loggedInUserEmailAddress, @RequestParam String loggedInUserFirstName, @RequestParam String loggedInUserLastName) {
        return categoryRepository.getCategory(categoryId, new User(loggedInUserId, loggedInUserUsername, loggedInUserPassword, loggedInUserEmailAddress, loggedInUserFirstName, loggedInUserLastName));
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param categoryName        The name of the new category.
     * @param categoryDescription The description of the new category.
     * @param categoryColourId    The ID of the color for the new category.
     * @param loggedInUser        The logged-in user.
     * @return The ID of the newly created category.
     */
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new category")
    public int addCategory(@RequestParam String categoryName,
                           @RequestParam String categoryDescription,
                           @RequestParam int categoryColourId,
                           @RequestBody User loggedInUser) {
        return categoryRepository.addCategory(categoryName, categoryDescription, categoryColourId, loggedInUser);
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param updatedCategory The updated category.
     * @param loggedInUser    The logged-in user.
     */
    @PatchMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing category")
    public void updateCategory(@RequestParam int categoryId, @RequestParam String updatedCategoryName, @RequestParam String updatedCategoryDescription, @RequestParam int updatedColourId
            , @RequestBody User loggedInUser) {
        categoryRepository.updateCategory(new Category(categoryId, updatedCategoryName, updatedCategoryDescription, updatedColourId, loggedInUser.getUserId()), loggedInUser);
    }

    /**
     * Changes the name of an existing category for the logged-in user.
     *
     * @param categoryId         The ID of the category to update.
     * @param updatedCategoryName The updated category name.
     * @param loggedInUser       The logged-in user.
     */
    @PatchMapping("/categories/categoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing category")
    public void updateCategoryName(@RequestParam int categoryId, @RequestParam String updatedCategoryName, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryName(categoryId, updatedCategoryName, loggedInUser);
    }

    /**
     * Changes the description of an existing category for the logged-in user.
     *
     * @param categoryId               The ID of the category to update.
     * @param updatedCategoryDescription The updated category description.
     * @param loggedInUser             The logged-in user.
     */
    @PatchMapping("/categories/categoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing category")
    public void updateCategoryDescription(@RequestParam int categoryId, @RequestParam String updatedCategoryDescription, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryDescription(categoryId, updatedCategoryDescription, loggedInUser);
    }

    /**
     * Changes the color of an existing category for the logged-in user.
     *
     * @param categoryId              The ID of the category to update.
     * @param updatedCategoryColourId The updated category color ID.
     * @param loggedInUser            The logged-in user.
     */
    @PatchMapping("/categories/categoryColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing category")
    public void updateCategoryColourId(@RequestParam int categoryId, @RequestParam int updatedCategoryColourId, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryColourId(categoryId, updatedCategoryColourId, loggedInUser);
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId   The ID of the category to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a category")
    public void deleteCategory(@RequestParam int categoryId, @RequestBody User loggedInUser) {
        categoryRepository.deleteCategory(categoryId, loggedInUser);
    }
}
