package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Subcategory;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubcategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Subcategory> getSubcategories(int categoryId, User loggedInUser) {
        return null;
    }

    public Subcategory getSubcategory(int categoryId, int subcategoryId, User loggedInUser) {
        return null;
    }

    public int addSubcategory(int categoryId, byte[] subcategoryName, byte[] subcategoryDescription, int subcategoryColourId, User loggedInUser) {
        return 0;
    }

    public void updateSubcategory(int categoryId, Subcategory updatedSubcategory, User loggedInUser) {
    }

    public void updateSubcategoryName(int categoryId, byte[] updatedSubcategoryName, User loggedInUser) {
    }

    public void updateSubcategoryDescription(int categoryId, byte[] updatedSubcategoryDescription, User loggedInUser) {
    }

    public void updateSubcategoryColour(int categoryId, int updatedSubcategoryColour, User loggedInUser) {
    }

    public void deleteSubcategory(int categoryId, int subcategoryId, User loggedInUser) {
    }
}