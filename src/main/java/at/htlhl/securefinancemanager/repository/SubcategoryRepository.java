package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Subcategory;
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
import java.util.Objects;

/**
 * The SubcategoryRepository class handles the persistence operations for subcategory data.
 * It serves as a Spring Data JPA repository for the Subcategory entity.
 *
 * <p>
 * The SubcategoryRepository serves as an abstraction layer between the SubcategoryController and the underlying data storage, enabling seamless access and manipulation of Subcategory entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for Subcategory entities.
 * </p>
 *
 * <p>
 * Note: This implementation directly uses JDBC for database operations. For a more modern approach, consider using Spring Data JPA's repository interfaces and entity managers.
 * </p>
 *
 * @author Fischer
 * @version 1.8
 * @since 15.10.2023 (version 1.8)
 */
@Repository
public class SubcategoryRepository {
    /**
     * SQL query to select all subcategories for a specific category and user.
     */
    private static final String SELECT_SUBCATEGORIES = "SELECT pk_subcategory_id, subcategory_name, subcategory_description, fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE fk_user_id = ? AND fk_category_id = ?;";

    /**
     * SQL query to select a specific subcategory for a specific category and user.
     */
    private static final String SELECT_SUBCATEGORY = "SELECT subcategory_name, subcategory_description, fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /**
     * SQL query to insert a new subcategory for a specific category and user.
     */
    private static final String INSERT_SUBCATEGORY = "INSERT INTO subcategories " +
            "(fk_category_id, subcategory_name, subcategory_description, fk_subcategory_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?, ?);";

    /**
     * SQL query to update an existing subcategory for a specific category and user.
     */
    private static final String UPDATE_SUBCATEGORY = "UPDATE subcategories " +
            "SET fk_category_id = ?, subcategory_name = ?, subcategory_description = ?, fk_subcategory_colour_id = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ?";

    /**
     * SQL query to delete a subcategory for a specific category and user.
     */
    private static final String DELETE_SUBCATEGORY = "DELETE FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /**
     * Retrieves a list of all subcategories for a specific category and user.
     *
     * @param categoryId   The ID of the category.
     * @param username     The username of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    public List<Subcategory> getSubcategories(int categoryId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORIES);
            ps.setInt(1, activeUserId);
            ps.setInt(2, categoryId);
            ResultSet rs = ps.executeQuery();

            List<Subcategory> subcategories = new ArrayList<>();
            while (rs.next()) {
                int subcategoryId = rs.getInt("pk_subcategory_id");
                byte[] subcategoryName = rs.getBytes("subcategory_name");
                byte[] subcategoryDescription = rs.getBytes("subcategory_description");
                int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");

                Subcategory subcategory = new Subcategory(subcategoryId, categoryId, Base64.getEncoder().encodeToString(subcategoryName), Base64.getEncoder().encodeToString(subcategoryDescription), subcategoryColourId, activeUserId);
                subcategories.add(subcategory);
            }

            return subcategories;
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
     */
    public Subcategory getSubcategory(int categoryId, int subcategoryId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORY);
            ps.setInt(1, subcategoryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, categoryId);
            ResultSet rs = ps.executeQuery();

            Subcategory getSubcategory = null;
            if (rs.next()) {
                byte[] subcategoryName = rs.getBytes("subcategory_name");
                byte[] subcategoryDescription = rs.getBytes("subcategory_description");
                int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");

                getSubcategory = new Subcategory(subcategoryId, categoryId, Base64.getEncoder().encodeToString(subcategoryName), Base64.getEncoder().encodeToString(subcategoryDescription), subcategoryColourId, activeUserId);
            }
            return getSubcategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new subcategory to a specific category and user.
     *
     * @param newSubcategory The new subcategory to be added.
     * @return The ID of the newly created subcategory.
     */
    public Subcategory addSubcategory(Subcategory newSubcategory) throws ValidationException {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_SUBCATEGORY, new String[]{"pk_subcategory_id"});
                ps.setInt(1, newSubcategory.getSubcategoryCategoryId());
                ps.setBytes(2, Base64.getDecoder().decode(newSubcategory.getSubcategoryName()));
                ps.setBytes(3, Base64.getDecoder().decode(newSubcategory.getSubcategoryDescription()));
                ps.setInt(4, newSubcategory.getSubcategoryColourId());
                ps.setInt(5, newSubcategory.getSubcategoryUserId());
                return ps;
            }, keyHolder);

            return new Subcategory(Objects.requireNonNull(keyHolder.getKey()).intValue(), newSubcategory.getSubcategoryCategoryId(), newSubcategory.getSubcategoryName(), newSubcategory.getSubcategoryDescription(), newSubcategory.getSubcategoryColourId(), newSubcategory.getSubcategoryUserId());
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
     */
    public Subcategory updateSubcategory(Subcategory updatedSubcategory, String username) throws ValidationException {
        Subcategory oldSubcategory = getSubcategory(updatedSubcategory.getSubcategoryCategoryId(), updatedSubcategory.getSubcategoryId(), username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY);

            ps.setInt(1, updatedSubcategory.getSubcategoryCategoryId());

            if (updatedSubcategory.getSubcategoryName() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedSubcategory.getSubcategoryName()));
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(oldSubcategory.getSubcategoryName()));
            }

            if (updatedSubcategory.getSubcategoryDescription() != null) {
                ps.setBytes(3, Base64.getDecoder().decode(updatedSubcategory.getSubcategoryDescription()));
            } else {
                ps.setBytes(3, Base64.getDecoder().decode(oldSubcategory.getSubcategoryDescription()));
            }

            if (updatedSubcategory.getSubcategoryColourId() != -1) {
                ps.setInt(4, updatedSubcategory.getSubcategoryColourId());
            } else {
                ps.setInt(4, oldSubcategory.getSubcategoryColourId());
            }

            ps.setInt(5, updatedSubcategory.getSubcategoryId());
            ps.setInt(6, updatedSubcategory.getSubcategoryUserId());
            ps.executeUpdate();
            conn.close();
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
     */
    public void deleteSubcategory(int categoryId, int subcategoryId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_SUBCATEGORY);
            ps.setInt(1, subcategoryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, categoryId);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}