package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Category> getCategories(User loggedInUser) {
        return null;
    }

    public Category getCategory(int categoryId, User loggedInUser) {
        return null;
    }

    public int addCategory(byte[] categoryName, byte[] categoryDescription, int categoryColourId, User loggedInUser) {
        return 0;
    }

    public void updateCategory(Category category, User loggedInUser) {

    }

    public void updateCategoryName(int categoryId, byte[] updatedCategoryName, User loggedInUser) {
    }

    public void updateCategoryDescription(int categoryId, byte[] updatedCategoryDescription, User loggedInUser) {
    }

    public void updateCategoryColourId(int categoryId, int updatedCategoryColourId, User loggedInUser) {
    }

    public void deleteCategory(int categoryId, User loggedInUser) {

    }
}
