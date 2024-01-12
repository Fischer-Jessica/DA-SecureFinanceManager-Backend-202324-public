package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import at.htlhl.securefinancemanager.model.database.DatabaseEntryLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The {@code EntryLabelRepository} class handles the persistence operations for EntryLabel data.
 * It serves as a Spring Data JPA repository for the EntryLabel entity.
 *
 * <p>
 * This repository interfaces with the EntryLabel entity class, which is a POJO (Plain Old Java Object) or entity class that maps to the 'entry_labels' table in the database.
 * </p>
 *
 * <p>
 * The {@code EntryLabelRepository} serves as an abstraction layer between the EntryLabelController and the underlying data storage, enabling seamless access and manipulation of EntryLabel entities.
 * </p>
 *
 * @author Fischer
 * @version 2.4
 * @since 12.01.2024 (version 2.4)
 */
@Repository
public class EntryLabelRepository {
    /** SQL query to retrieve labels for a specific entry and user from the 'labels' and 'entry_labels' tables in the database. */
    private static final String SELECT_LABELS_FOR_ENTRY = "SELECT pk_label_id, label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "JOIN entry_labels ON labels.pk_label_id = entry_labels.fk_label_id " +
            "WHERE entry_labels.fk_entry_id = ? AND entry_labels.fk_user_id = ? AND labels.fk_user_id = ?;";

    /** SQL query to retrieve entries for a specific label and user from the 'entries' and 'entry_labels' tables in the database. */
    private static final String SELECT_ENTRIES_FOR_LABEL = "SELECT pk_entry_id, entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_transaction, entry_attachment, fk_subcategory_id " +
            "FROM entries " +
            "JOIN entry_labels ON entries.pk_entry_id = entry_labels.fk_entry_id " +
            "WHERE entry_labels.fk_label_id = ? AND entry_labels.fk_user_id = ? AND entries.fk_user_id = ?;";

    /** SQL query to add a label to an entry in the 'entry_labels' table in the database. */
    private static final String ADD_LABEL_TO_ENTRY = "INSERT INTO entry_labels " +
            "(fk_entry_id, fk_label_id, fk_user_id) " +
            "VALUES (?, ?, ?);";

    /** SQL query to remove a label from an entry in the 'entry_labels' table in the database. */
    private static final String REMOVE_LABEL_FROM_ENTRY = "DELETE FROM entry_labels " +
            "WHERE fk_entry_id = ? AND fk_label_id = ? AND fk_user_id = ?;";

    /**
     * Retrieves a list of labels associated with a specific entry and user from the 'labels' and 'entry_labels' tables in the database.
     *
     * @param entryId   The ID of the entry to retrieve labels for.
     * @param username  The username of the logged-in user.
     * @return A list of Label objects representing the labels associated with the entry and user.
     * @throws ValidationException  If the specified entryLabel does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the entry.
     */
    public List<DatabaseLabel> getLabelsForEntry(int entryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_LABELS_FOR_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, activeUserId);
            ResultSet rs = ps.executeQuery();

            List<DatabaseLabel> databaseLabels = new ArrayList<>();
            while (rs.next()) {
                int labelId = rs.getInt("pk_label_id");
                byte[] encodedLabelName = rs.getBytes("label_name");
                byte[] encodedLabelDescription = rs.getBytes("label_description");
                int labelColourId = rs.getInt("fk_label_colour_id");

                if (encodedLabelDescription == null) {
                    databaseLabels.add(new DatabaseLabel(labelId, new String(Base64.getDecoder().decode(encodedLabelName), StandardCharsets.UTF_8), null, labelColourId, activeUserId));
                } else {
                    databaseLabels.add(new DatabaseLabel(labelId, new String(Base64.getDecoder().decode(encodedLabelName), StandardCharsets.UTF_8), new String(Base64.getDecoder().decode(encodedLabelDescription), StandardCharsets.UTF_8), labelColourId, activeUserId));
                }
            }
            if (databaseLabels.isEmpty()) {
                throw new ValidationException("No labels found for an entry with ID " + entryId);
            }
            return databaseLabels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of entries associated with a specific label and user from the 'entries' and 'entry_labels' tables in the database.
     *
     * @param labelId   The ID of the label to retrieve entries for.
     * @param username  The username of the logged-in user.
     * @return A list of Label objects representing the entries associated with the label and user.
     * @throws ValidationException  If the specified entryLabel does not exist or if the provided username is invalid.
     *                              This exception may indicate that the labelId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the label.
     */
    public List<DatabaseEntry> getEntriesForLabel(int labelId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ENTRIES_FOR_LABEL);
            ps.setInt(1, labelId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, activeUserId);
            ResultSet rs = ps.executeQuery();

            List<DatabaseEntry> databaseLabels = new ArrayList<>();
            while (rs.next()) {
                int entryId = rs.getInt("pk_entry_id");
                byte[] encodedEntryName = rs.getBytes("entry_name");
                byte[] encodedEntryDescription = rs.getBytes("entry_description");
                byte[] encodedEntryAmount = rs.getBytes("entry_amount");
                byte[] encodedEntryCreationTime = rs.getBytes("entry_creation_time");
                byte[] encodedEntryTimeOfTransaction = rs.getBytes("entry_time_of_transaction");
                byte[] encodedEntryAttachment = rs.getBytes("entry_attachment");
                int subcategoryId = rs.getInt("fk_subcategory_id");

                String stringEntryName = null;
                String stringEntryDescription = null;
                String stringEntryAttachment = null;

                if (encodedEntryName != null) {
                    stringEntryName = new String(Base64.getDecoder().decode(encodedEntryName), StandardCharsets.UTF_8);
                }
                if (encodedEntryDescription != null) {
                    stringEntryDescription = new String(Base64.getDecoder().decode(encodedEntryDescription), StandardCharsets.UTF_8);
                }
                if (encodedEntryAttachment != null) {
                    stringEntryAttachment = new String(Base64.getDecoder().decode(encodedEntryAttachment), StandardCharsets.UTF_8);
                }

                databaseLabels.add(new DatabaseEntry(entryId, subcategoryId, stringEntryName, stringEntryDescription,
                        new String(Base64.getDecoder().decode(encodedEntryAmount), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(encodedEntryCreationTime), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(encodedEntryTimeOfTransaction), StandardCharsets.UTF_8), stringEntryAttachment, activeUserId));
            }
            if (databaseLabels.isEmpty()) {
                throw new ValidationException("No entries found for an label with ID " + labelId);
            }
            return databaseLabels;
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
     * @return The newly created entryLabel.
     * @throws ValidationException  If the specified entry or label does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId or the labelId is not found or that the
     *                              userId associated with the provided username does not match the expected owner of the
     *                              entry or the label.
     */
    public DatabaseEntryLabel addLabelToEntry(int entryId, int labelId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            new LabelRepository().getLabel(labelId, username);
            new EntryRepository().getEntryWithoutSubcategoryId(entryId, username);

            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(ADD_LABEL_TO_ENTRY, new String[]{"pk_entry_label_id"});
                ps.setInt(1, entryId);
                ps.setInt(2, labelId);
                ps.setInt(3, activeUserId);
                return ps;
            }, keyHolder);

            return new DatabaseEntryLabel(Objects.requireNonNull(keyHolder.getKey()).intValue(), entryId, labelId, activeUserId);
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
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException  If the specified entryLabel does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId or the labelId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the entry and the label.
     */
    public int removeLabelFromEntry(int entryId, int labelId, String username) throws ValidationException {
        try {
            new LabelRepository().getLabel(labelId, username);
            new EntryRepository().getEntryWithoutSubcategoryId(entryId, username);

            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(REMOVE_LABEL_FROM_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, labelId);
            ps.setInt(3, userSingleton.getUserId(username));
            int rowsAffected = ps.executeUpdate();
            conn.close();

            if (rowsAffected == 0) {
                throw new ValidationException("No entryLabel found for an entry with ID " + entryId + " and a label with ID " + labelId);
            }
            return rowsAffected;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}