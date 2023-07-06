package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Category> getCategories() {
        return null;
    }

    public Category getCategory(int categoryId) {
        return null;
    }

    public int addCategory(byte[] categoryName, byte[] categoryDescription, int categoryColourId) {
        return 0;
    }

    public void updateCategory(int categoryId, Category category) {

    }

    public void deleteCategory(int categoryId, byte[] password) {

    }
}
