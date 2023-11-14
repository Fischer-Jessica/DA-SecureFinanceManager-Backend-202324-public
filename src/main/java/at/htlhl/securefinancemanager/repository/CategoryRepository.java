package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseCategory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The {@code CategoryRepository} class handles the persistence operations for category data.
 * It provides methods to access and manipulate the 'categories' table in the 'secure_finance_manager' PostgreSQL database.
 *
 * <p>
 * This class interacts with the Category entity class, which represents a POJO (Plain Old Java Object) or entity class that maps to the 'categories' table in the database.
 * It uses Spring Data JPA to provide generic CRUD (Create, Read, Update, Delete) operations for the Category entity, reducing boilerplate code.
 * </p>
 *
 * <p>
 * The {@code CategoryRepository} serves as an abstraction layer between the CategoryController and the underlying data storage, enabling seamless access and manipulation of Category entities.
 * </p>
 *
 * <p>
 * This class should be annotated with the {@code @Repository} annotation to indicate that it is a Spring-managed repository component.
 * </p>
 *
 * <p>
 * The methods in this class should be implemented to provide the necessary data access and database operations for Category entities.
 * </p>
 *
 * @author Fischer
 * @version 2.4
 * @since 14.11.2023 (version 2.4)
 */
@Repository
public class CategoryRepository {
    /**
     * SQL query to retrieve a list of categories for the logged-in user from the 'categories' table in the database.
     */
    private static final String SELECT_CATEGORIES = "SELECT pk_category_id, category_name, category_description, fk_category_colour_id " +
            "FROM categories " +
            "WHERE fk_user_id = ?;";

    /**
     * SQL query to retrieve a specific category for the logged-in user from the 'categories' table in the database.
     */
    private static final String SELECT_CATEGORY = "SELECT category_name, category_description, fk_category_colour_id " +
            "FROM categories " +
            "WHERE fk_user_id = ? AND pk_category_id = ?;";

    /**
     * SQL query to insert a new category for the logged-in user into the 'categories' table in the database.
     */
    private static final String INSERT_CATEGORY = "INSERT INTO categories " +
            "(category_name, category_description, fk_category_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?);";

    /**
     * SQL query to update an existing category for the logged-in user in the 'categories' table in the database.
     */
    private static final String UPDATE_CATEGORY = "UPDATE categories " +
            "SET category_name = ?, category_description = ?, fk_category_colour_id = ? " +
            "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /**
     * SQL query to delete a category for the logged-in user from the 'categories' table in the database.
     */
    private static final String DELETE_CATEGORY = "DELETE FROM categories " +
            "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of categories for the logged-in user.
     *
     * @param username The username of the logged-in user.
     * @return A list of Category objects representing the categories.
     */
    public List<DatabaseCategory> getCategories(String username) {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORIES);
            ps.setInt(1, activeUserId);
            ResultSet rs = ps.executeQuery();

            List<DatabaseCategory> databaseCategories = new ArrayList<>();
            while (rs.next()) {
                int categoryId = rs.getInt("pk_category_id");
                byte[] categoryName = rs.getBytes("category_name");
                byte[] categoryDescription = rs.getBytes("category_description");
                int categoryColourId = rs.getInt("fk_category_colour_id");

                if (categoryDescription == null) {
                    databaseCategories.add(new DatabaseCategory(categoryId, Base64.getEncoder().encodeToString(categoryName), null, categoryColourId, activeUserId));
                } else {
                    databaseCategories.add(new DatabaseCategory(categoryId, Base64.getEncoder().encodeToString(categoryName), Base64.getEncoder().encodeToString(categoryDescription), categoryColourId, activeUserId));
                }
            }

            return databaseCategories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specific category for the logged-in user.
     *
     * @param categoryId The ID of the category to retrieve.
     * @param username   The username of the logged-in user.
     * @return The requested Category object.
     * @throws ValidationException  If the specified category does not exist or if the provided username is invalid.
     *                              This exception indicates that the categoryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the category.
     */
    public DatabaseCategory getCategory(int categoryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORY);
            ps.setInt(1, activeUserId);
            ps.setInt(2, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                byte[] categoryName = rs.getBytes("category_name");
                byte[] categoryDescription = rs.getBytes("category_description");
                int categoryColourId = rs.getInt("fk_category_colour_id");

                if (categoryDescription == null) {
                    return new DatabaseCategory(categoryId, Base64.getEncoder().encodeToString(categoryName), null, categoryColourId, activeUserId);
                } else {
                    return new DatabaseCategory(categoryId, Base64.getEncoder().encodeToString(categoryName), Base64.getEncoder().encodeToString(categoryDescription), categoryColourId, activeUserId);
                }
            }
            throw new ValidationException("Category with ID " + categoryId + " not found.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param newCategory The Category object representing the new category.
     * @return The ID of the newly created category.
     */
    public DatabaseCategory addCategory(DatabaseCategory newCategory) {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY, new String[]{"pk_category_id"});
                ps.setBytes(1, Base64.getDecoder().decode(newCategory.getCategoryName()));
                if (newCategory.getCategoryDescription() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(newCategory.getCategoryDescription()));
                }
                ps.setInt(3, newCategory.getCategoryColourId());
                ps.setInt(4, newCategory.getCategoryUserId());
                return ps;
            }, keyHolder);

            return new DatabaseCategory(Objects.requireNonNull(keyHolder.getKey()).intValue(), newCategory.getCategoryName(), newCategory.getCategoryDescription(), newCategory.getCategoryColourId(), newCategory.getCategoryUserId());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param updatedCategory The updated Category object.
     * @param username        The username of the logged-in user.
     * @return The updated Category object.
     * @throws ValidationException  If the specified category does not exist or if the provided username is invalid.
     *                              This exception indicates that the categoryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the category.
     */
    public DatabaseCategory updateCategory(DatabaseCategory updatedCategory, String username) throws ValidationException {
        DatabaseCategory oldDatabaseCategory = getCategory(updatedCategory.getCategoryId(), username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY);

            if (updatedCategory.getCategoryName() != null) {
                ps.setBytes(1, Base64.getDecoder().decode(updatedCategory.getCategoryName()));
            } else {
                ps.setBytes(1, Base64.getDecoder().decode(oldDatabaseCategory.getCategoryName()));
            }

            if (updatedCategory.getCategoryDescription() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedCategory.getCategoryDescription()));
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(oldDatabaseCategory.getCategoryDescription()));
            }

            if (updatedCategory.getCategoryColourId() != 0) {
                ps.setInt(3, updatedCategory.getCategoryColourId());
            } else {
                ps.setInt(3, oldDatabaseCategory.getCategoryColourId());
            }

            ps.setInt(4, updatedCategory.getCategoryId());
            ps.setInt(5, updatedCategory.getCategoryUserId());
            ps.executeUpdate();
            conn.close();
            return getCategory(updatedCategory.getCategoryId(), username);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId The ID of the category to delete.
     * @param username   The username of the logged-in user.
     * @throws ValidationException  If the specified category does not exist or if the provided username is invalid.
     *                              This exception indicates that the categoryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the category.
     */
    public void deleteCategory(int categoryId, String username) throws ValidationException {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY);
            ps.setInt(1, categoryId);
            ps.setInt(2, userSingleton.getUserId(username));
            int rowsAffected = ps.executeUpdate();
            conn.close();

            if (rowsAffected == 0) {
                throw new ValidationException("Category with ID " + categoryId + " not found.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}