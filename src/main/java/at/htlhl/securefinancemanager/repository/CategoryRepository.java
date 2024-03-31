package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.DATABASE_ENCRYPTION_DECRYPTION_KEY;
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
 * This class is annotated with the {@code @Repository} annotation to indicate that it is a Spring-managed repository component.
 * </p>
 *
 * <p>
 * The methods in this class are implemented to provide the necessary data access and database operations for Category entities.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 3.5
 * @since 31.03.2024 (version 3.5)
 */
@Repository
public class CategoryRepository {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Autowired instance of SubcategoryRepository for accessing subcategory-related data.
     */
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    /**
     * SQL query to retrieve a list of categories for the logged-in user from the 'categories' table in the database.
     */
    private static final String SELECT_CATEGORIES = "SELECT pk_category_id, " + "pgp_sym_decrypt(category_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_category_name," + "pgp_sym_decrypt(category_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_category_description," + "fk_category_colour_id " + "FROM categories " + "WHERE fk_user_id = ?;";

    /**
     * SQL query to retrieve a specific category for the logged-in user from the 'categories' table in the database.
     */
    private static final String SELECT_CATEGORY = "SELECT pgp_sym_decrypt(category_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_category_name, " + "pgp_sym_decrypt(category_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_category_description, fk_category_colour_id " + "FROM categories " + "WHERE fk_user_id = ? AND pk_category_id = ?;";

    /**
     * SQL query to insert a new category for the logged-in user into the 'categories' table in the database.
     */
    private static final String INSERT_CATEGORY = "INSERT INTO categories " + "(category_name, category_description, fk_category_colour_id, fk_user_id) " + "VALUES (pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), ?, ?);";

    /**
     * SQL query to update an existing category for the logged-in user in the 'categories' table in the database.
     */
    private static final String UPDATE_CATEGORY = "UPDATE categories " + "SET category_name = pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), category_description = pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), fk_category_colour_id = ? " + "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /**
     * SQL query to delete a category for the logged-in user from the 'categories' table in the database.
     */
    private static final String DELETE_CATEGORY = "DELETE FROM categories " + "WHERE pk_category_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of categories for the logged-in user.
     *
     * @param username The username of the logged-in user.
     * @return A list of Category objects representing the categories.
     * @throws ValidationException If the authenticated user does not have any categories.
     */
    public List<DatabaseCategory> getCategories(String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);

        List<DatabaseCategory> databaseCategories = new ArrayList<>();

        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORIES)) {

            ps.setInt(1, activeUserId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int categoryId = rs.getInt("pk_category_id");
                    String decryptedCategoryName = rs.getString("decrypted_category_name");
                    String decryptedCategoryDescription = rs.getString("decrypted_category_description");
                    int categoryColourId = rs.getInt("fk_category_colour_id");

                    databaseCategories.add(new DatabaseCategory(categoryId, decryptedCategoryName, decryptedCategoryDescription, categoryColourId, activeUserId));
                }
                if (databaseCategories.isEmpty()) {
                    throw new ValidationException("No categories found for the authenticated user.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return databaseCategories;
    }

    /**
     * Retrieves a specific category for the logged-in user.
     *
     * @param categoryId The ID of the category to retrieve.
     * @param username   The username of the logged-in user.
     * @return The requested Category object.
     * @throws ValidationException If the specified category does not exist or if the provided username is invalid.
     *                             This exception indicates that the categoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the category.
     */
    public DatabaseCategory getCategory(int categoryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);

        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORY)) {

            ps.setInt(1, activeUserId);
            ps.setInt(2, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String decryptedCategoryName = rs.getString("decrypted_category_name");
                    String decryptedCategoryDescription = rs.getString("decrypted_category_description");
                    int categoryColourId = rs.getInt("fk_category_colour_id");

                    return new DatabaseCategory(categoryId, decryptedCategoryName, decryptedCategoryDescription, categoryColourId, activeUserId);
                }
                throw new ValidationException("Category with ID " + categoryId + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the total value of entries within all subcategories of a specific category for a logged-in user.
     * This method retrieves the total value by summing up the values of all subcategories within the specified category.
     *
     * @param categoryId The ID of the category for which to calculate the total value.
     * @param username   The username of the logged-in user.
     * @return The total value of entries within all subcategories of the specified category, as a float.
     * @throws ValidationException If the provided category ID is invalid or if the provided username is invalid.
     *                             This exception may indicate that the category does not exist or that the provided username
     *                             does not have permission to access the category.
     */
    public float getValueOfCategory(int categoryId, String username) throws ValidationException {
        try {
            double categorySum = subcategoryRepository.getSubcategories(categoryId, username).stream().mapToDouble(subcategory -> {
                try {
                    return subcategoryRepository.getValueOfSubcategory(categoryId, subcategory.getSubcategoryId(), username);
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            }).sum();
            BigDecimal roundedSum = BigDecimal.valueOf(categorySum).setScale(2, RoundingMode.HALF_UP);
            return roundedSum.floatValue();
        } catch (ValidationException exception) {
            return 0;
        }
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param newCategory The Category object representing the new category.
     * @return The newly created Category object.
     */
    public DatabaseCategory addCategory(DatabaseCategory newCategory) {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection()) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY, new String[]{"pk_category_id"});
                ps.setString(1, newCategory.getCategoryName());
                if (newCategory.getCategoryDescription() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setString(2, newCategory.getCategoryDescription());
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
     * @throws ValidationException If the specified category does not exist or if the provided username is invalid.
     *                             This exception indicates that the categoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the category.
     */
    public DatabaseCategory updateCategory(DatabaseCategory updatedCategory, String username) throws ValidationException {
        DatabaseCategory oldDatabaseCategory = getCategory(updatedCategory.getCategoryId(), username);

        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY)) {

            if (updatedCategory.getCategoryName() != null) {
                ps.setString(1, updatedCategory.getCategoryName());
            } else {
                ps.setString(1, oldDatabaseCategory.getCategoryName());
            }

            if (updatedCategory.getCategoryDescription() != null) {
                ps.setString(2, updatedCategory.getCategoryDescription());
            } else {
                if (oldDatabaseCategory.getCategoryDescription() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setString(2, oldDatabaseCategory.getCategoryDescription());
                }
            }

            if (updatedCategory.getCategoryColourId() != 0) {
                ps.setInt(3, updatedCategory.getCategoryColourId());
            } else {
                ps.setInt(3, oldDatabaseCategory.getCategoryColourId());
            }

            ps.setInt(4, updatedCategory.getCategoryId());
            ps.setInt(5, updatedCategory.getCategoryUserId());
            ps.executeUpdate();
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
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException If the specified category does not exist or if the provided username is invalid.
     *                             This exception indicates that the categoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the category.
     */
    public int deleteCategory(int categoryId, String username) throws ValidationException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, userSingleton.getUserId(username));
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new ValidationException("Category with ID " + categoryId + " not found.");
            }
            return rowsAffected;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}