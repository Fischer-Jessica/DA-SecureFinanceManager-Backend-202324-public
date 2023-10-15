package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Label;
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
 * @version 1.8
 * @since 15.10.2023 (version 1.8)
 */
@Repository
public class LabelRepository {
    /**
     * SQL query to select all labels for a given user.
     */
    private static final String SELECT_LABELS = "SELECT pk_label_id, label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "WHERE fk_user_id = ?;";

    /**
     * SQL query to select a specific label for a given user and label ID.
     */
    private static final String SELECT_LABEL = "SELECT label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "WHERE fk_user_id = ? AND pk_label_id = ?;";

    /**
     * SQL query to insert a new label for the logged-in user.
     */
    private static final String INSERT_LABEL = "INSERT INTO labels " +
            "(label_name, label_description, fk_label_colour_id, fk_user_id) " +
            "VALUES (?, ?, ?, ?);";

    /**
     * SQL query to update an existing label for the logged-in user.
     */
    private static final String UPDATE_LABEL = "UPDATE labels " +
            "SET label_name = ?, label_description = ?, fk_label_colour_id = ? " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /**
     * SQL query to delete a label for the logged-in user.
     */
    private static final String DELETE_LABEL = "DELETE FROM labels " +
            "WHERE pk_label_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of labels for the logged-in user.
     *
     * @param username The username of the logged-in user.
     * @return A list of Label objects representing the labels.
     * @throws ValidationException if there's a validation issue.
     */
    public List<Label> getLabels(String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABELS);
            ps.setInt(1, activeUserId);
            ResultSet rs = ps.executeQuery();

            List<Label> labels = new ArrayList<>();
            while (rs.next()) {
                int labelId = rs.getInt("pk_label_id");
                byte[] labelName = rs.getBytes("label_name");
                byte[] labelDescription = rs.getBytes("label_description");
                int labelColourId = rs.getInt("fk_label_colour_id");

                Label label = new Label(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, activeUserId);
                labels.add(label);
            }

            return labels;
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
     * @throws ValidationException if there's a validation issue.
     */
    public Label getLabel(int labelId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABEL);
            ps.setInt(1, activeUserId);
            ps.setInt(2, labelId);
            ResultSet rs = ps.executeQuery();

            Label getLabel = null;
            if (rs.next()) {
                byte[] labelName = rs.getBytes("label_name");
                byte[] labelDescription = rs.getBytes("label_description");
                int labelColourId = rs.getInt("fk_label_colour_id");

                getLabel = new Label(labelId, Base64.getEncoder().encodeToString(labelName), Base64.getEncoder().encodeToString(labelDescription), labelColourId, activeUserId);
            }
            return getLabel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new label for the logged-in user.
     *
     * @param newLabel The Label object representing the new label.
     * @return The ID of the newly created label.
     * @throws ValidationException if there's a validation issue.
     */
    public Label addLabel(Label newLabel) throws ValidationException {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_LABEL, new String[]{"pk_label_id"});
                ps.setBytes(1, Base64.getDecoder().decode(newLabel.getLabelName()));
                ps.setBytes(2, Base64.getDecoder().decode(newLabel.getLabelDescription()));
                ps.setInt(3, newLabel.getLabelColourId());
                ps.setInt(4, newLabel.getLabelUserId());
                return ps;
            }, keyHolder);

            return new Label(Objects.requireNonNull(keyHolder.getKey()).intValue(), newLabel.getLabelName(), newLabel.getLabelDescription(), newLabel.getLabelColourId(), newLabel.getLabelUserId());
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
     * @throws ValidationException if there's a validation issue.
     */
    public Label updateLabel(Label updatedLabel, String username) throws ValidationException {
        Label oldLabel = getLabel(updatedLabel.getLabelId(), username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_LABEL);

            if (updatedLabel.getLabelName() != null) {
                ps.setBytes(1, Base64.getDecoder().decode(updatedLabel.getLabelName()));
            } else {
                ps.setBytes(1, Base64.getDecoder().decode(oldLabel.getLabelName()));
            }

            if (updatedLabel.getLabelDescription() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedLabel.getLabelDescription()));
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(oldLabel.getLabelDescription()));
            }

            if (updatedLabel.getLabelColourId() != -1) {
                ps.setInt(3, updatedLabel.getLabelColourId());
            } else {
                ps.setInt(3, oldLabel.getLabelColourId());
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
     * @throws ValidationException if there's a validation issue.
     */
    public void deleteLabel(int labelId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_LABEL);
            ps.setInt(1, labelId);
            ps.setInt(2, activeUserId);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}