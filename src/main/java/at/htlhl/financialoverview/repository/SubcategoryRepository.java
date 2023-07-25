package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.exception.ValidationException;
import at.htlhl.financialoverview.model.Subcategory;
import at.htlhl.financialoverview.model.User;
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
 * @version 1.3
 * @since 25.07.2023 (version 1.3)
 */
@Repository
public class SubcategoryRepository {
    /** SQL query to select all subcategories for a specific category and user. */
    private static final String SELECT_SUBCATEGORIES = "SELECT pk_subcategory_id, subcategory_name, subcategory_description, fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE fk_user_id = ? AND fk_category_id = ?;";

    /** SQL query to select a specific subcategory for a specific category and user. */
    private static final String SELECT_SUBCATEGORY = "SELECT subcategory_name, subcategory_description, fk_subcategory_colour_id " +
            "FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /** SQL query to insert a new subcategory for a specific category and user. */
    private static final String INSERT_SUBCATEGORY = "INSERT INTO subcategories " +
            "(fk_category_id, subcategory_name, subcategory_description, fk_subcategory_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?, ?);";

    /** SQL query to update an existing subcategory for a specific category and user. */
    private static final String UPDATE_SUBCATEGORY = "UPDATE subcategories " +
            "SET fk_category_id = ?, subcategory_name = ?, subcategory_description = ?, fk_subcategory_colour_id = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ?;";

    /** SQL query to update the category ID of an existing subcategory for a specific user. */
    private static final String UPDATE_CATEGORY_ID = "UPDATE subcategories " +
            "SET fk_category_id = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ?;";

    /** SQL query to update the name of an existing subcategory for a specific category and user. */
    private static final String UPDATE_SUBCATEGORY_NAME = "UPDATE subcategories " +
            "SET subcategory_name = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /** SQL query to update the description of an existing subcategory for a specific category and user. */
    private static final String UPDATE_SUBCATEGORY_DESCRIPTION = "UPDATE subcategories " +
            "SET subcategory_description = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /** SQL query to update the color ID of an existing subcategory for a specific category and user. */
    private static final String UPDATE_SUBCATEGORY_COLOUR = "UPDATE subcategories " +
            "SET fk_subcategory_colour_id = ? " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /** SQL query to delete a subcategory for a specific category and user. */
    private static final String DELETE_SUBCATEGORY = "DELETE FROM subcategories " +
            "WHERE pk_subcategory_id = ? AND fk_user_id = ? AND fk_category_id = ?;";

    /**
     * Retrieves a list of all subcategories for a specific category and user.
     *
     * @param categoryId        The ID of the category.
     * @param loggedInUser      The logged-in user.
     * @return A list of subcategories for the specified category.
     */
    public List<Subcategory> getSubcategories(int categoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORIES);
                ps.setInt(1, loggedInUser.getUserId());
                ps.setInt(2, categoryId);
                ResultSet rs = ps.executeQuery();

                List<Subcategory> subcategories = new ArrayList<>();
                while (rs.next()) {
                    int subcategoryId = rs.getInt("pk_subcategory_id");
                    byte[] subcategoryName = rs.getBytes("subcategory_name");
                    byte[] subcategoryDescription = rs.getBytes("subcategory_description");
                    int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");

                    Subcategory subcategory = new Subcategory(subcategoryId, categoryId, Base64.getEncoder().encodeToString(subcategoryName), Base64.getEncoder().encodeToString(subcategoryDescription), subcategoryColourId, loggedInUser.getUserId());
                    subcategories.add(subcategory);
                }

                return subcategories;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Retrieves a specific subcategory for a specific category and user.
     *
     * @param categoryId        The ID of the category.
     * @param subcategoryId     The ID of the subcategory.
     * @param loggedInUser      The logged-in user.
     * @return The requested subcategory.
     */
    public Subcategory getSubcategory(int categoryId, int subcategoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_SUBCATEGORY);
                ps.setInt(1, subcategoryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.setInt(3, categoryId);
                ResultSet rs = ps.executeQuery();

                Subcategory getSubcategory = null;
                if (rs.next()) {
                    byte[] subcategoryName = rs.getBytes("subcategory_name");
                    byte[] subcategoryDescription = rs.getBytes("subcategory_description");
                    int subcategoryColourId = rs.getInt("fk_subcategory_colour_id");

                    getSubcategory = new Subcategory(subcategoryId, categoryId, Base64.getEncoder().encodeToString(subcategoryName), Base64.getEncoder().encodeToString(subcategoryDescription), subcategoryColourId, loggedInUser.getUserId());
                }
                return getSubcategory;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Adds a new subcategory to a specific category and user.
     *
     * @param categoryId                The ID of the category.
     * @param subcategoryName           The name of the subcategory.
     * @param subcategoryDescription    The description of the subcategory.
     * @param subcategoryColourId       The ID of the color for the subcategory.
     * @param loggedInUser              The logged-in user.
     * @return The ID of the newly created subcategory.
     */
    public int addSubcategory(int categoryId, String subcategoryName, String subcategoryDescription, int subcategoryColourId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

                UserRepository.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = conn.prepareStatement(INSERT_SUBCATEGORY, new String[]{"pk_subcategory_id"});
                    ps.setInt(1, categoryId);
                    ps.setBytes(2, Base64.getDecoder().decode(subcategoryName));
                    ps.setBytes(3, Base64.getDecoder().decode(subcategoryDescription));
                    ps.setInt(4, subcategoryColourId);
                    ps.setInt(5, loggedInUser.getUserId());
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
     * Updates an existing subcategory for a specific category and user.
     *
     * @param categoryId                            The ID of the category.
     * @param updatedSubcategoryName                The updated subcategory name.
     * @param updatedSubcategoryDescription         The updated subcategory description.
     * @param updatedSubcategoryColour              The updated subcategory colour.
     * @param loggedInUser                          The logged-in user.
     */
    public void updateSubcategory(int categoryId, int subcategoryId, String updatedSubcategoryName,
                                  String updatedSubcategoryDescription, int updatedSubcategoryColour, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY);
                ps.setInt(1, categoryId);
                ps.setBytes(2, Base64.getDecoder().decode(updatedSubcategoryName));
                ps.setBytes(3, Base64.getDecoder().decode(updatedSubcategoryDescription));
                ps.setInt(4, updatedSubcategoryColour);
                ps.setInt(5, subcategoryId);
                ps.setInt(6, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the category ID of an existing subcategory for a specific user.
     *
     * @param categoryId        The updated ID of the category.
     * @param subcategoryId     The ID of the subcategory.
     * @param loggedInUser      The logged-in user.
     */
    public void updateCategoryOfSubcategory(int categoryId, int subcategoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY_ID);
                ps.setInt(1, categoryId);
                ps.setInt(2, subcategoryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Changes the name of an existing subcategory for a specific category and user.
     *
     * @param categoryId                    The ID of the category.
     * @param updatedSubcategoryName        The updated subcategory name.
     * @param loggedInUser                  The logged-in user.
     */
    public void updateSubcategoryName(int categoryId, int subcategoryId, String updatedSubcategoryName, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY_NAME);
                ps.setBytes(1, Base64.getDecoder().decode(updatedSubcategoryName));
                ps.setInt(2, subcategoryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, categoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Changes the description of an existing subcategory for a specific category and user.
     *
     * @param categoryId                            The ID of the category.
     * @param updatedSubcategoryDescription         The updated subcategory description.
     * @param loggedInUser                          The logged-in user.
     */
    public void updateSubcategoryDescription(int categoryId, int subcategoryId, String updatedSubcategoryDescription, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY_DESCRIPTION);
                ps.setBytes(1, Base64.getDecoder().decode(updatedSubcategoryDescription));
                ps.setInt(2, subcategoryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, categoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Changes the colour of an existing subcategory for a specific category and user.
     *
     * @param categoryId                    The ID of the category.
     * @param updatedSubcategoryColour      The updated subcategory colour ID.
     * @param loggedInUser                  The logged-in user.
     */
    public void updateSubcategoryColour(int categoryId, int subcategoryId, int updatedSubcategoryColour, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY_COLOUR);
                ps.setInt(1, updatedSubcategoryColour);
                ps.setInt(2, subcategoryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, categoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Deletes a subcategory for a specific category and user.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory to delete.
     * @param loggedInUser          The logged-in user.
     */
    public void deleteSubcategory(int categoryId, int subcategoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(DELETE_SUBCATEGORY);
                ps.setInt(1, subcategoryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.setInt(3, categoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}