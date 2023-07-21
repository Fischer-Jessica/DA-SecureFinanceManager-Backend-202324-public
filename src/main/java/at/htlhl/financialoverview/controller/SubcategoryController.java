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
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Subcategory entities.
 * It interacts with the SubcategoryRepository to access and manipulate the Subcategory entities in the 'subcategories' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The SubcategoryController class works in conjunction with the SubcategoryRepository and other related classes to enable efficient management of subcategories in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.4
 * @since 21.07.2023 (version 1.4)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}")
public class SubcategoryController {
    /** The SubcategoryRepository instance for accessing subcategory data. */
    @Autowired
    SubcategoryRepository subcategoryRepository;

    /**
     * Returns a list of all subcategories for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    @GetMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all subcategories of one category")
    public List<Subcategory> getSubcategories(@PathVariable int categoryId,
                                              @RequestParam int loggedInUserId,
                                              @RequestParam String loggedInUsername,
                                              @RequestParam String loggedInPassword,
                                              @RequestParam String loggedInEMailAddress,
                                              @RequestParam String loggedInFirstName,
                                              @RequestParam String loggedInLastName) {
        return subcategoryRepository.getSubcategories(categoryId,
                new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName));
    }

    /**
     * Returns a specific subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return The requested subcategory.
     */
    @GetMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one subcategory")
    public Subcategory getSubcategory(@PathVariable int categoryId,
                                      @PathVariable int subcategoryId,
                                      @RequestParam int loggedInUserId,
                                      @RequestParam String loggedInUsername,
                                      @RequestParam String loggedInPassword,
                                      @RequestParam String loggedInEMailAddress,
                                      @RequestParam String loggedInFirstName,
                                      @RequestParam String loggedInLastName) {
        return subcategoryRepository.getSubcategory(categoryId, subcategoryId,
                new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName));
    }

    /**
     * Adds a new subcategory to a specific category.
     *
     * @param categoryId                The ID of the category, in which the subcategory will be created.
     * @param subcategoryName           The name of the new subcategory.
     * @param subcategoryDescription    The description of the new subcategory.
     * @param subcategoryColourId       The ID of the color for the new subcategory.
     * @param loggedInUser              The logged-in user.
     * @return The ID of the newly created subcategory.
     */
    @PostMapping("/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new subcategory")
    public int addSubcategory(@PathVariable int categoryId,
                              @RequestParam String subcategoryName,
                              @RequestParam String subcategoryDescription,
                              @RequestParam int subcategoryColourId,
                              @RequestBody User loggedInUser) {
        return subcategoryRepository.addSubcategory(categoryId,
                subcategoryName, subcategoryDescription, subcategoryColourId,
                loggedInUser);
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId                            The ID of the category.
     * @param subcategoryId                         The ID of the subcategory to update.
     * @param updatedSubcategoryName                The updated name of the subcategory.
     * @param updatedSubcategoryDescription         The updated description of the subcategory.
     * @param updatedSubcategoryColour              The updated colour of the subcategory.
     * @param loggedInUser                          The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public void updateSubcategory(@PathVariable int categoryId,
                                  @PathVariable int subcategoryId,
                                  @RequestParam String updatedSubcategoryName,
                                  @RequestParam String updatedSubcategoryDescription,
                                  @RequestParam int updatedSubcategoryColour,
                                  @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategory(categoryId,
                subcategoryId, updatedSubcategoryName, updatedSubcategoryDescription, updatedSubcategoryColour,
                loggedInUser);
    }

    /**
     * Changing which category the subcategory is subordinate.
     *
     * @param categoryId        The updated categoryId.
     * @param subcategoryId     The ID of the subcategory.
     * @param loggedInUser      The logged-in User.
     */

    @PostMapping("/subcategories/{subcategoryId}/categoryOfSubcategory")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the category of a subcategory")
    public void updateCategoryOfSubcategory(@PathVariable int categoryId,
                                            @PathVariable int subcategoryId,
                                            @RequestBody User loggedInUser) {
        subcategoryRepository.updateCategoryOfSubcategory(categoryId, subcategoryId, loggedInUser);
    }

    /**
     * Changes the name of an existing subcategory for a specific category.
     *
     * @param categoryId                The ID of the category.
     * @param subcategoryId             The ID of the subcategory to update.
     * @param updatedSubcategoryName    The updated subcategory name.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing subcategory")
    public void updateSubcategoryName(@PathVariable int categoryId,
                                      @PathVariable int subcategoryId,
                                      @RequestParam String updatedSubcategoryName,
                                      @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryName(categoryId,
                subcategoryId, updatedSubcategoryName,
                loggedInUser);
    }

    /**
     * Changes the description of an existing subcategory for a specific category.
     *
     * @param categoryId                        The ID of the category.
     * @param subcategoryId                     The ID of the subcategory to update.
     * @param updatedSubcategoryDescription     The updated subcategory description.
     * @param loggedInUser                      The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing subcategory")
    public void updateSubcategoryDescription(@PathVariable int categoryId,
                                             @PathVariable int subcategoryId,
                                             @RequestParam String updatedSubcategoryDescription,
                                             @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryDescription(categoryId,
                subcategoryId, updatedSubcategoryDescription,
                loggedInUser);
    }

    /**
     * Changes the colour of an existing subcategory for a specific category.
     *
     * @param categoryId                    The ID of the category.
     * @param subcategoryId                 The ID of the subcategory to update.
     * @param updatedSubcategoryColour      The updated subcategory colour ID.
     * @param loggedInUser                  The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryColour")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing subcategory")
    public void updateSubcategoryColour(@PathVariable int categoryId,
                                        @PathVariable int subcategoryId,
                                        @RequestParam int updatedSubcategoryColour,
                                        @RequestBody User loggedInUser) {
        subcategoryRepository.updateSubcategoryColour(categoryId,
                subcategoryId, updatedSubcategoryColour,
                loggedInUser);
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
    public void deleteSubcategory(@PathVariable int categoryId,
                                  @PathVariable int subcategoryId,
                                  @RequestBody User loggedInUser) {
        subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
    }
}
