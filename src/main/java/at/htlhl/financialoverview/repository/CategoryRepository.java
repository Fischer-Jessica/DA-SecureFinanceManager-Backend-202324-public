package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The CategoryRepository class handles the persistence operations for category data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class CategoryRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of categories for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of Category objects representing the categories.
     */
    public List<Category> getCategories(User loggedInUser) {
        return null;
    }

    /**
     * Retrieves a specific category for the logged-in user.
     *
     * @param categoryId   The ID of the category to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested Category object.
     */
    public Category getCategory(int categoryId, User loggedInUser) {
        return null;
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
    public int addCategory(byte[] categoryName, byte[] categoryDescription, int categoryColourId, User loggedInUser) {
        return 0;
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param category     The updated Category object.
     * @param loggedInUser The logged-in user.
     */
    public void updateCategory(Category category, User loggedInUser) {

    }

    /**
     * Updates the name of an existing category for the logged-in user.
     *
     * @param categoryId         The ID of the category to update.
     * @param updatedCategoryName The updated category name.
     * @param loggedInUser       The logged-in user.
     */
    public void updateCategoryName(int categoryId, byte[] updatedCategoryName, User loggedInUser) {
    }

    /**
     * Updates the description of an existing category for the logged-in user.
     *
     * @param categoryId               The ID of the category to update.
     * @param updatedCategoryDescription The updated category description.
     * @param loggedInUser             The logged-in user.
     */
    public void updateCategoryDescription(int categoryId, byte[] updatedCategoryDescription, User loggedInUser) {
    }

    /**
     * Updates the color of an existing category for the logged-in user.
     *
     * @param categoryId              The ID of the category to update.
     * @param updatedCategoryColourId The updated category color ID.
     * @param loggedInUser            The logged-in user.
     */
    public void updateCategoryColourId(int categoryId, int updatedCategoryColourId, User loggedInUser) {
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId   The ID of the category to delete.
     * @param loggedInUser The logged-in user.
     */
    public void deleteCategory(int categoryId, User loggedInUser) {

    }
}
