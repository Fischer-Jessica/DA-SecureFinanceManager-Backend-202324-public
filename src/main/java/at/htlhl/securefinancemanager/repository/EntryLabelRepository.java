package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.EntryLabel;
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
import java.util.Objects;

/**
 * The EntryLabelRepository class handles the persistence operations for EntryLabel data.
 * It serves as a Spring Data JPA repository for the EntryLabel entity.
 *
 * <p>
 * This repository interfaces with the EntryLabel entity class, which is a POJO (Plain Old Java Object) or entity class that maps to the 'entry_labels' table in the database.
 * </p>
 *
 * <p>
 * The EntryLabelRepository serves as an abstraction layer between the EntryLabelController and the underlying data storage, enabling seamless access and manipulation of EntryLabel entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for EntryLabel entities.
 * </p>
 *
 * @author Fischer
 * @version 1.5
 * @since 15.10.2023 (version 1.5)
 */
@Repository
public class EntryLabelRepository {
    /**
     * SQL query to retrieve labels for a specific entry and user from the 'labels' and 'entry_labels' tables in the database.
     */
    private static final String SELECT_LABELS_FOR_ENTRY = "SELECT pk_label_id, label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "JOIN entry_labels ON labels.pk_label_id = entry_labels.fk_label_id " +
            "WHERE entry_labels.fk_entry_id = ? AND entry_labels.fk_user_id = ? AND entry_labels.fk_user_id = ?;";

    /**
     * SQL query to add a label to an entry in the 'entry_labels' table in the database.
     */
    private static final String ADD_LABEL_TO_ENTRY = "INSERT INTO entry_labels " +
            "(fk_entry_id, fk_label_id, fk_user_id) " +
            "VALUES (?, ?, ?);";

    /**
     * SQL query to remove a label from an entry in the 'entry_labels' table in the database.
     */
    private static final String REMOVE_LABEL_FROM_ENTRY = "DELETE FROM entry_labels " +
            "WHERE fk_entry_id = ? AND fk_label_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of labels associated with a specific entry and user from the 'labels' and 'entry_labels' tables in the database.
     *
     * @param entryId   The ID of the entry to retrieve labels for.
     * @param username  The username of the logged-in user.
     * @return A list of Label objects representing the labels associated with the entry and user.
     */
    public List<Label> getLabelsForEntry(int entryId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABELS_FOR_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, activeUserId);
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
     * Adds a label to an entry in the 'entry_labels' table in the database.
     *
     * @param entryId   The ID of the entry to add the label to.
     * @param labelId   The ID of the label to add.
     * @param username  The username of the logged-in user.
     * @return The ID of the newly created entry label.
     */
    public EntryLabel addLabelToEntry(int entryId, int labelId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(ADD_LABEL_TO_ENTRY, new String[]{"pk_entry_label_id"});
                ps.setInt(1, entryId);
                ps.setInt(2, labelId);
                ps.setInt(3, activeUserId);
                return ps;
            }, keyHolder);

            return new EntryLabel(Objects.requireNonNull(keyHolder.getKey()).intValue(), entryId, labelId, activeUserId);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Removes a label from an entry in the 'entry_labels' table in the database.
     *
     * @param entryId   The ID of the entry to remove the label from.
     * @param labelId   The ID of the label to remove.
     * @param username  The username of the logged-in user.
     */
    public void removeLabelFromEntry(int entryId, int labelId, String username) throws ValidationException {
        int activeUserId = UserRepository.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(REMOVE_LABEL_FROM_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, labelId);
            ps.setInt(3, activeUserId);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
