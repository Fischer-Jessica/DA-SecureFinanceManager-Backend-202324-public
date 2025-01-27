package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseSubcategory;
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
 * The {@code SubcategoryRepository} class handles the persistence operations for subcategory data.
 * It serves as a Spring Data JPA repository for the Subcategory entity.
 *
 * <p>
 * The {@code SubcategoryRepository} serves as an abstraction layer between the SubcategoryController and the underlying data storage, enabling seamless access and manipulation of Subcategory entities.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 3.4
 * @since 31.03.2024 (version 3.4)
 */
@Repository
public class SubcategoryRepository {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * SQL query to select all subcategories for a specific category and user.
     */
    private static final String SELECT_SUBCATEGORIES = "SELECT pk_subcategory_id, " +
            "pgp_sym_decrypt(subcategory_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_subcategory_name," +
            "pgp_sym_decrypt(subcategory_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_subcategory_description," +
            "fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE fk_user_id = ? AND fk_category_id = ?;";

    /**
     * SQL query to select a specific subcategory for a specific category and user.
     */
    private static final String SELECT_SUBCATEGORY = "SELECT pgp_sym_decrypt(subcategory_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_subcategory_name, " +
            "pgp_sym_decrypt(subcategory_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_subcategory_description," +
            "fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /**
     * SQL query to retrieve the total decrypted amount from entries within a specific subcategory for a logged-in user.
     * The decrypted amount is calculated by decrypting the 'entry_amount' field using the provided encryption key,
     * and then summing up the numeric values.
     */
    private static final String SELECT_TOTAL_AMOUNT_OF_SUBCATEGORY = "SELECT SUM(pgp_sym_decrypt(entry_amount, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "')::numeric) AS total_decrypted_amount " +
            "FROM entries " +
            "WHERE fk_subcategory_id = ? AND fk_user_id = ?;";

    /**
     * SQL query to insert a new subcategory for a specific category and user.
     */
    private static final String INSERT_SUBCATEGORY = "INSERT INTO subcategories " +
            "(fk_category_id, subcategory_name, subcategory_description, fk_subcategory_colour_id, fk_user_id) " +
            "VALUES (?, pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), ?, ?);";

    /**
     * SQL query to update an existing subcategory for a specific category and user.
     */
    private static final String UPDATE_SUBCATEGORY = "UPDATE subcategories " +
            "SET fk_category_id = ?, subcategory_name = pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), subcategory_description = pgp_sym_encrypt(?, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "'), fk_subcategory_colour_id = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ?";

    /**
     * SQL query to delete a subcategory for a specific category and user.
     */
    private static final String DELETE_SUBCATEGORY = "DELETE FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /**
     * Retrieves a list of all subcategories for a specific category and user.
     *
     * @param categoryId The ID of the category.
     * @param username   The username of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    public List<DatabaseSubcategory> getSubcategories(int categoryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORIES)) {
            ps.setInt(1, activeUserId);
            ps.setInt(2, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                List<DatabaseSubcategory> databaseSubcategories = new ArrayList<>();
                while (rs.next()) {
                    int subcategoryId = rs.getInt("pk_subcategory_id");
                    String decryptedSubcategoryName = rs.getString("decrypted_subcategory_name");
                    String decryptedSubcategoryDescription = rs.getString("decrypted_subcategory_description");
                    int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");
                    databaseSubcategories.add(new DatabaseSubcategory(subcategoryId, categoryId, decryptedSubcategoryName,
                            decryptedSubcategoryDescription, subcategoryColourId, activeUserId));
                }
                if (databaseSubcategories.isEmpty()) {
                    throw new ValidationException("No subcategories found for category with ID " + categoryId + ".");
                }
                return databaseSubcategories;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specific subcategory for a specific category and user.
     *
     * @param categoryId    The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param username      The username of the logged-in user.
     * @return The requested subcategory.
     * @throws ValidationException If the specified subcategory does not exist or if the provided username is invalid.
     *                             This exception may indicate that the subcategoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the subcategory.
     */
    public DatabaseSubcategory getSubcategory(int categoryId, int subcategoryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORY)) {
            ps.setInt(1, subcategoryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String decryptedSubcategoryName = rs.getString("decrypted_subcategory_name");
                    String decryptedSubcategoryDescription = rs.getString("decrypted_subcategory_description");
                    int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");
                    return new DatabaseSubcategory(subcategoryId, categoryId, decryptedSubcategoryName,
                            decryptedSubcategoryDescription, subcategoryColourId, activeUserId);
                }
                throw new ValidationException("Subcategory with ID " + subcategoryId + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the total amount from a specific subcategory for a logged-in user.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param username      The username of the logged-in user.
     * @return The total amount from the specified subcategory.
     * @throws ValidationException If the specified subcategory does not exist or if the provided username is invalid.
     *                             This exception may indicate that the subcategoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the subcategory.
     */
    public float getValueOfSubcategory(int categoryId, int subcategoryId, String username) throws ValidationException {
        getSubcategory(categoryId, subcategoryId, username);
        int activeUserId = userSingleton.getUserId(username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TOTAL_AMOUNT_OF_SUBCATEGORY)) {
            ps.setInt(1, subcategoryId);
            ps.setInt(2, activeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal totalSum = BigDecimal.valueOf(rs.getFloat("total_decrypted_amount"));
                    totalSum = totalSum.setScale(2, RoundingMode.HALF_UP);
                    return totalSum.floatValue();
                }
                throw new ValidationException("Subcategory with ID " + subcategoryId + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new subcategory to a specific category and user.
     *
     * @param newSubcategory The new subcategory to be added.
     * @return The newly created subcategory.
     */
    public DatabaseSubcategory addSubcategory(DatabaseSubcategory newSubcategory) {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection()) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_SUBCATEGORY, new String[]{"pk_subcategory_id"});
                ps.setInt(1, newSubcategory.getSubcategoryCategoryId());
                ps.setString(2, newSubcategory.getSubcategoryName());
                if (newSubcategory.getSubcategoryDescription() == null) {
                    ps.setNull(3, Types.NULL);
                } else {
                    ps.setString(3, newSubcategory.getSubcategoryDescription());
                }
                ps.setInt(4, newSubcategory.getSubcategoryColourId());
                ps.setInt(5, newSubcategory.getSubcategoryUserId());
                return ps;
            }, keyHolder);
            return new DatabaseSubcategory(Objects.requireNonNull(keyHolder.getKey()).intValue(),
                    newSubcategory.getSubcategoryCategoryId(), newSubcategory.getSubcategoryName(),
                    newSubcategory.getSubcategoryDescription(), newSubcategory.getSubcategoryColourId(),
                    newSubcategory.getSubcategoryUserId());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing subcategory for a specific category and user.
     *
     * @param updatedSubcategory The updated subcategory data.
     * @param username           The username of the logged-in user.
     * @return The updated subcategory.
     * @throws ValidationException If the specified subcategory does not exist or if the provided username is invalid.
     *                             This exception may indicate that the subcategoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the subcategory.
     */
    public DatabaseSubcategory updateSubcategory(DatabaseSubcategory updatedSubcategory, String username) throws ValidationException {
        DatabaseSubcategory oldDatabaseSubcategory = getSubcategory(updatedSubcategory.getSubcategoryCategoryId(), updatedSubcategory.getSubcategoryId(), username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY)) {
            ps.setInt(1, updatedSubcategory.getSubcategoryCategoryId());
            if (updatedSubcategory.getSubcategoryName() != null) {
                ps.setString(2, updatedSubcategory.getSubcategoryName());
            } else {
                ps.setString(2, oldDatabaseSubcategory.getSubcategoryName());
            }
            if (updatedSubcategory.getSubcategoryDescription() != null) {
                ps.setString(3, updatedSubcategory.getSubcategoryDescription());
            } else {
                if (oldDatabaseSubcategory.getSubcategoryDescription() == null) {
                    ps.setNull(3, Types.NULL);
                } else {
                    ps.setString(3, oldDatabaseSubcategory.getSubcategoryDescription());
                }
            }
            if (updatedSubcategory.getSubcategoryColourId() != 0) {
                ps.setInt(4, updatedSubcategory.getSubcategoryColourId());
            } else {
                ps.setInt(4, oldDatabaseSubcategory.getSubcategoryColourId());
            }
            ps.setInt(5, updatedSubcategory.getSubcategoryId());
            ps.setInt(6, updatedSubcategory.getSubcategoryUserId());
            ps.executeUpdate();
            return getSubcategory(updatedSubcategory.getSubcategoryCategoryId(), updatedSubcategory.getSubcategoryId(), username);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes a subcategory for a specific category and user.
     *
     * @param categoryId    The ID of the category.
     * @param subcategoryId The ID of the subcategory to delete.
     * @param username      The username of the logged-in user.
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException If the specified subcategory does not exist or if the provided username is invalid.
     *                             This exception may indicate that the subcategoryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the subcategory.
     */
    public int deleteSubcategory(int categoryId, int subcategoryId, String username) throws ValidationException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SUBCATEGORY)) {
            ps.setInt(1, subcategoryId);
            ps.setInt(2, userSingleton.getUserId(username));
            ps.setInt(3, categoryId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new ValidationException("Subcategory with ID " + subcategoryId + " not found.");
            }
            return rowsAffected;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}