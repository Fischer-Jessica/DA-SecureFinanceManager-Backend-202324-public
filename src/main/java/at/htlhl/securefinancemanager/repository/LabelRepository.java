package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The {@code LabelRepository} class handles the persistence operations for label data.
 * It serves as a Spring Data JPA repository for the Label entity.
 *
 * <p>
 * The {@code LabelRepository} serves as an abstraction layer between the LabelController and the underlying data storage, enabling seamless access and manipulation of Label entities.
 * </p>
 *
 * @author Fischer
 * @version 2.7
 * @since 17.11.2023 (version 2.7)
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

    /** SQL query to delete a label for the logged-in user. */
    private static final String DELETE_LABEL = "DELETE FROM labels " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of labels for the logged-in user.
     *
     * @param username The username of the logged-in user.
     * @return A list of Label objects representing the labels.
     */
    public List<DatabaseLabel> getLabels(String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABELS);
            ps.setInt(1, activeUserId);
            ResultSet rs = ps.executeQuery();

            List<DatabaseLabel> databaseLabels = new ArrayList<>();
            while (rs.next()) {
                int labelId = rs.getInt("pk_label_id");
                byte[] labelName = rs.getBytes("label_name");
                byte[] labelDescription = rs.getBytes("label_description");
                int labelColourId = rs.getInt("fk_label_colour_id");

                if (labelDescription == null) {
                    databaseLabels.add(new DatabaseLabel(labelId, Base64.getEncoder().encodeToString(labelName), null, labelColourId, activeUserId));
                } else {
                    databaseLabels.add(new DatabaseLabel(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, activeUserId));
                }
            }
            if (databaseLabels.isEmpty()) {
                throw new ValidationException("No labels found for the authenticated user.");
            }
            return databaseLabels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specific label for the logged-in user.
     *
     * @param labelId The ID of the label to retrieve.
     * @param username The username of the logged-in user.
     * @return The requested Label object.
     * @throws ValidationException  If the specified label does not exist or if the provided username is invalid.
     *                              This exception may indicate that the labelId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the label.
     */
    public DatabaseLabel getLabel(int labelId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABEL);
            ps.setInt(1, activeUserId);
            ps.setInt(2, labelId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                byte[] labelName = rs.getBytes("label_name");
                byte[] labelDescription = rs.getBytes("label_description");
                int labelColourId = rs.getInt("fk_label_colour_id");

                if (labelDescription == null) {
                    return new DatabaseLabel(labelId, Base64.getEncoder().encodeToString(labelName), null, labelColourId, activeUserId);
                } else {
                    return new DatabaseLabel(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, activeUserId);
                }
            }
            throw new ValidationException("Label with ID " + labelId + " not found.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param newLabel The Label object representing the new label.
     * @return The newly created label.
     */
    public DatabaseLabel addLabel(DatabaseLabel newLabel) {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_LABEL, new String[]{"pk_label_id"});
                ps.setBytes(1, Base64.getDecoder().decode(newLabel.getLabelName()));
                if (newLabel.getLabelDescription() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(newLabel.getLabelDescription()));
                }
                ps.setInt(3, newLabel.getLabelColourId());
                ps.setInt(4, newLabel.getLabelUserId());
                return ps;
            }, keyHolder);

            return new DatabaseLabel(Objects.requireNonNull(keyHolder.getKey()).intValue(), newLabel.getLabelName(), newLabel.getLabelDescription(), newLabel.getLabelColourId(), newLabel.getLabelUserId());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param updatedLabel The updated Label object.
     * @param username The username of the logged-in user.
     * @return The updated Label object.
     * @throws ValidationException  If the specified label does not exist or if the provided username is invalid.
     *                              This exception may indicate that the labelId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the label.
     */
    public DatabaseLabel updateLabel(DatabaseLabel updatedLabel, String username) throws ValidationException {
        DatabaseLabel oldDatabaseLabel = getLabel(updatedLabel.getLabelId(), username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL);

            if (updatedLabel.getLabelName() != null) {
                ps.setBytes(1, Base64.getDecoder().decode(updatedLabel.getLabelName()));
            } else {
                ps.setBytes(1, Base64.getDecoder().decode(oldDatabaseLabel.getLabelName()));
            }

            if (updatedLabel.getLabelDescription() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedLabel.getLabelDescription()));
            } else {
                if (oldDatabaseLabel.getLabelDescription() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(oldDatabaseLabel.getLabelDescription()));
                }
            }

            if (updatedLabel.getLabelColourId() != 0) {
                ps.setInt(3, updatedLabel.getLabelColourId());
            } else {
                ps.setInt(3, oldDatabaseLabel.getLabelColourId());
            }

            ps.setInt(4, updatedLabel.getLabelId());
            ps.setInt(5, updatedLabel.getLabelUserId());
            ps.executeUpdate();
            conn.close();
            return getLabel(updatedLabel.getLabelId(), username);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId The ID of the label to delete.
     * @param username The username of the logged-in user.
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException  If the specified label does not exist or if the provided username is invalid.
     *                              This exception may indicate that the labelId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the label.
     */
    public int deleteLabel(int labelId, String username) throws ValidationException {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_LABEL);
            ps.setInt(1, labelId);
            ps.setInt(2, userSingleton.getUserId(username));
            int rowsAffected = ps.executeUpdate();
            conn.close();

            if (rowsAffected == 0) {
                throw new ValidationException("Label with ID " + labelId + " not found.");
            }
            return rowsAffected;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}