package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Label;
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
 * The LabelRepository class handles the persistence operations for label data.
 * It serves as a Spring Data JPA repository for the Label entity.
 *
 * <p>
 * The LabelRepository serves as an abstraction layer between the LabelController and the underlying data storage, enabling seamless access and manipulation of Label entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for Label entities.
 * </p>
 *
 * <p>
 * Note: This implementation directly uses JDBC for database operations. For a more modern approach, consider using Spring Data JPA's repository interfaces and entity managers.
 * </p>
 *
 * @author Fischer
 * @version 1.4
 * @since 25.07.2023 (version 1.4)
 */
@Repository
public class LabelRepository {
    /** SQL query to select all labels for a given user. */
    private static final String SELECT_LABELS = "SELECT pk_label_id, label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "WHERE fk_user_id = ?;";

    /** SQL query to select a specific label for a given user and label ID. */
    private static final String SELECT_LABEL = "SELECT label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "WHERE fk_user_id = ? AND pk_label_id = ?;";

    /** SQL query to insert a new label for the logged-in user. */
    private static final String INSERT_LABEL = "INSERT INTO labels " +
            "(label_name, label_description, fk_label_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?);";

    /** SQL query to update an existing label for the logged-in user. */
    private static final String UPDATE_LABEL = "UPDATE labels " +
            "SET label_name = ?, label_description = ?, fk_label_colour_id = ? " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /** SQL query to update the name of an existing label for the logged-in user. */
    private static final String UPDATE_LABEL_NAME = "UPDATE labels " +
            "SET label_name = ? " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /** SQL query to update the description of an existing label for the logged-in user. */
    private static final String UPDATE_LABEL_DESCRIPTION = "UPDATE labels " +
            "SET label_description = ? " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /** SQL query to update the color of an existing label for the logged-in user. */
    private static final String UPDATE_LABEL_COLOUR_ID = "UPDATE labels " +
            "SET fk_label_colour_id = ? " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /** SQL query to delete a label for the logged-in user. */
    private static final String DELETE_LABEL = "DELETE FROM labels " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of labels for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of Label objects representing the labels.
     */
    public List<Label> getLabels(User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_LABELS);
                ps.setInt(1, loggedInUser.getUserId());
                ResultSet rs = ps.executeQuery();

                List<Label> labels = new ArrayList<>();
                while (rs.next()) {
                    int labelId = rs.getInt("pk_label_id");
                    byte[] labelName = rs.getBytes("label_name");
                    byte[] labelDescription = rs.getBytes("label_description");
                    int labelColourId = rs.getInt("fk_label_colour_id");

                    Label label = new Label(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, loggedInUser.getUserId());
                    labels.add(label);
                }

                return labels;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Retrieves a specific label for the logged-in user.
     *
     * @param labelId      The ID of the label to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested Label object.
     */
    public Label getLabel(int labelId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_LABEL);
                ps.setInt(1, loggedInUser.getUserId());
                ps.setInt(2, labelId);
                ResultSet rs = ps.executeQuery();

                Label getLabel = null;
                if (rs.next()) {
                    byte[] labelName = rs.getBytes("label_name");
                    byte[] labelDescription = rs.getBytes("label_description");
                    int labelColourId = rs.getInt("fk_label_colour_id");

                    getLabel = new Label(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, loggedInUser.getUserId());
                }
                return getLabel;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param labelName        The name of the new label.
     * @param labelDescription The description of the new label.
     * @param labelColourId    The ID of the color for the new label.
     * @param loggedInUser     The logged-in user.
     * @return The ID of the newly created label.
     */
    public int addLabel(String labelName, String labelDescription, int labelColourId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

                UserRepository.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = conn.prepareStatement(INSERT_LABEL, new String[]{"pk_label_id"});
                    ps.setBytes(1, Base64.getDecoder().decode(labelName));
                    ps.setBytes(2, Base64.getDecoder().decode(labelDescription));
                    ps.setInt(3, labelColourId);
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
     * Updates an existing label for the logged-in user.
     *
     * @param updatedLabel      The updated Label object.
     * @param loggedInUser      The logged-in user.
     */
    public void updateLabel(Label updatedLabel, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL);
                ps.setBytes(1, Base64.getDecoder().decode(updatedLabel.getLabelName()));
                ps.setBytes(2, Base64.getDecoder().decode(updatedLabel.getLabelDescription()));
                ps.setInt(3, updatedLabel.getLabelColourId());
                ps.setInt(4, updatedLabel.getLabelId());
                ps.setInt(5, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the name of an existing label for the logged-in user.
     *
     * @param labelId               The ID of the label to update.
     * @param updatedLabelName      The updated label name.
     * @param loggedInUser          The logged-in user.
     */
    public void updateLabelName(int labelId, String updatedLabelName, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL_NAME);
                ps.setBytes(1, Base64.getDecoder().decode(updatedLabelName));
                ps.setInt(2, labelId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the description of an existing label for the logged-in user.
     *
     * @param labelId                       The ID of the label to update.
     * @param updatedLabelDescription       The updated label description.
     * @param loggedInUser                  The logged-in user.
     */
    public void updateLabelDescription(int labelId, String updatedLabelDescription, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL_DESCRIPTION);
                ps.setBytes(1, Base64.getDecoder().decode(updatedLabelDescription));
                ps.setInt(2, labelId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the color of an existing label for the logged-in user.
     *
     * @param labelId                   The ID of the label to update.
     * @param updatedLabelColour        The updated label color ID.
     * @param loggedInUser              The logged-in user.
     */
    public void updateLabelColourId(int labelId, int updatedLabelColour, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL_COLOUR_ID);
                ps.setInt(1, updatedLabelColour);
                ps.setInt(2, labelId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId           The ID of the label to delete.
     * @param loggedInUser      The logged-in user.
     */
    public void deleteLabel(int labelId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(DELETE_LABEL);
                ps.setInt(1, labelId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}