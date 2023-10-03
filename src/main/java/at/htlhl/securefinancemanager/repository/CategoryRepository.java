package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Category;
import at.htlhl.securefinancemanager.model.User;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * The CategoryRepository class handles the persistence operations for category data.
 * It provides methods to access and manipulate the 'categories' table in the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This class interacts with the Category entity class, which represents a POJO (Plain Old Java Object) or entity class that maps to the 'categories' table in the database.
 * It uses Spring Data JPA to provide generic CRUD (Create, Read, Update, Delete) operations for the Category entity, reducing boilerplate code.
 * </p>
 *
 * <p>
 * The CategoryRepository serves as an abstraction layer between the CategoryController and the underlying data storage, enabling seamless access and manipulation of Category entities.
 * </p>
 *
 * <p>
 * This class should be annotated with the @Repository annotation to indicate that it is a Spring-managed repository component.
 * </p>
 *
 * <p>
 * The methods in this class should be implemented to provide the necessary data access and database operations for Category entities.
 * </p>
 *
 * @author Fischer
 * @version 1.5
 * @since 03.10.2023 (version 1.5)
 */
@Repository
public class CategoryRepository {
    /** SQL query to retrieve a list of categories for the logged-in user from the 'categories' table in the database. */
    private static final String SELECT_CATEGORIES = "SELECT pk_category_id, category_name, category_description, fk_category_colour_id " +
            "FROM categories " +
            "WHERE fk_user_id = ?;";

    /**  SQL query to retrieve a specific category for the logged-in user from the 'categories' table in the database. */
    private static final String SELECT_CATEGORY = "SELECT category_name, category_description, fk_category_colour_id " +
            "FROM categories " +
            "WHERE fk_user_id = ? AND pk_category_id = ?;";

    /** SQL query to insert a new category for the logged-in user into the 'categories' table in the database. */
    private static final String INSERT_CATEGORY = "INSERT INTO categories " +
            "(category_name, category_description, fk_category_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?);";

    /** SQL query to update an existing category for the logged-in user in the 'categories' table in the database. */
    private static final String UPDATE_CATEGORY = "UPDATE categories " +
            "SET category_name = ?, category_description = ?, fk_category_colour_id = ? " +
            "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /** SQL query to delete a category for the logged-in user from the 'categories' table in the database. */
    private static final String DELETE_CATEGORY = "DELETE FROM categories " +
            "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of categories for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of Category objects representing the categories.
     */
    public List<Category> getCategories(User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORIES);
                ps.setInt(1, loggedInUser.getUserId());
                ResultSet rs = ps.executeQuery();

                List<Category> categories = new ArrayList<>();
                while (rs.next()) {
                    int categoryId = rs.getInt("pk_category_id");
                    byte[] categoryName = rs.getBytes("category_name");
                    byte[] categoryDescription = rs.getBytes("category_description");
                    int categoryColourId = rs.getInt("fk_category_colour_id");

                    Category category = new Category(categoryId, Base64.getEncoder().encodeToString(categoryName), Base64.getEncoder().encodeToString(categoryDescription), categoryColourId, loggedInUser.getUserId());
                    categories.add(category);
                }

                return categories;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }


    /**
     * Retrieves a specific category for the logged-in user.
     *
     * @param categoryId   The ID of the category to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested Category object.
     */
    public Category getCategory(int categoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORY);
                ps.setInt(1, loggedInUser.getUserId());
                ps.setInt(2, categoryId);
                ResultSet rs = ps.executeQuery();

                Category getCategory = null;
                if (rs.next()) {
                    byte[] categoryName = rs.getBytes("category_name");
                    byte[] categoryDescription = rs.getBytes("category_description");
                    int categoryColourId = rs.getInt("fk_category_colour_id");
                    
                    getCategory = new Category(categoryId, Base64.getEncoder().encodeToString(categoryName), Base64.getEncoder().encodeToString(categoryDescription), categoryColourId, loggedInUser.getUserId());
                }
                return getCategory;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param categoryName              The name of the new category.
     * @param categoryDescription       The description of the new category.
     * @param categoryColourId          The ID of the color for the new category.
     * @param loggedInUser              The logged-in user.
     * @return The ID of the newly created category.
     */
    public int addCategory(String categoryName, String categoryDescription, int categoryColourId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

                UserRepository.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY, new String[]{"pk_category_id"});
                    ps.setBytes(1, Base64.getDecoder().decode(categoryName));
                    ps.setBytes(2, Base64.getDecoder().decode(categoryDescription));
                    ps.setInt(3, categoryColourId);
                    ps.setInt(4, loggedInUser.getUserId());
                    return ps;
                }, keyHolder);

                return keyHolder.getKey().intValue();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            return 0;
        }
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param updatedCategory     The updated Category object.
     * @param loggedInUser        The logged-in user.
     */
    public void updateCategory(Category updatedCategory, User loggedInUser) throws ValidationException {
        Category oldCategory = getCategory(updatedCategory.getCategoryId(), loggedInUser);
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY);

                if (updatedCategory.getCategoryName() != null) {
                    ps.setBytes(1, Base64.getDecoder().decode(updatedCategory.getCategoryName()));
                } else {
                    ps.setBytes(1, Base64.getDecoder().decode(oldCategory.getCategoryName()));
                }

                if (updatedCategory.getCategoryDescription() != null) {
                    ps.setBytes(2, Base64.getDecoder().decode(updatedCategory.getCategoryDescription()));
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(oldCategory.getCategoryDescription()));
                }

                if (updatedCategory.getCategoryColourId() != -1) {
                    ps.setInt(3, updatedCategory.getCategoryColourId());
                } else {
                    ps.setInt(3, oldCategory.getCategoryColourId());
                }

                ps.setInt(4, updatedCategory.getCategoryId());
                ps.setInt(5, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId        The ID of the category to delete.
     * @param loggedInUser      The logged-in user.
     */
    public void deleteCategory(int categoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY);
                ps.setInt(1, categoryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
