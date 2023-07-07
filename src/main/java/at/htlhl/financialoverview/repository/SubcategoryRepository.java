package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Subcategory;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The SubcategoryRepository class handles the persistence operations for subcategory data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class SubcategoryRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of all subcategories for a specific category.
     *
     * @param categoryId  The ID of the category.
     * @param loggedInUser The logged-in user.
     * @return A list of subcategories for the specified category.
     */
    public List<Subcategory> getSubcategories(int categoryId, User loggedInUser) {
        return null;
    }

    /**
     * Retrieves a specific subcategory for a specific category.
     *
     * @param categoryId     The ID of the category.
     * @param subcategoryId  The ID of the subcategory.
     * @param loggedInUser The logged-in user.
     * @return The requested subcategory.
     */
    public Subcategory getSubcategory(int categoryId, int subcategoryId, User loggedInUser) {
        return null;
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
    public int addSubcategory(int categoryId, byte[] subcategoryName, byte[] subcategoryDescription, int subcategoryColourId, User loggedInUser) {
        return 0;
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId         The ID of the category.
     * @param updatedSubcategory The updated subcategory.
     * @param loggedInUser     The logged-in user.
     */
    public void updateSubcategory(int categoryId, Subcategory updatedSubcategory, User loggedInUser) {
    }

    /**
     * Changes the name of an existing subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param updatedSubcategoryName The updated subcategory name.
     * @param loggedInUser        The logged-in user.
     */
    public void updateSubcategoryName(int categoryId, byte[] updatedSubcategoryName, User loggedInUser) {
    }

    /**
     * Changes the description of an existing subcategory for a specific category.
     *
     * @param categoryId                 The ID of the category.
     * @param updatedSubcategoryDescription The updated subcategory description.
     * @param loggedInUser               The logged-in user.
     */
    public void updateSubcategoryDescription(int categoryId, byte[] updatedSubcategoryDescription, User loggedInUser) {
    }

    /**
     * Changes the colour of an existing subcategory for a specific category.
     *
     * @param categoryId                The ID of the category.
     * @param updatedSubcategoryColour  The updated subcategory colour ID.
     * @param loggedInUser            The logged-in user.
     */
    public void updateSubcategoryColour(int categoryId, int updatedSubcategoryColour, User loggedInUser) {
    }

    /**
     * Deletes a subcategory for a specific category.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory to delete.
     * @param loggedInUser The logged-in user.
     */
    public void deleteSubcategory(int categoryId, int subcategoryId, User loggedInUser) {
    }
}