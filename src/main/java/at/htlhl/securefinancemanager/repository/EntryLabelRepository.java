package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import at.htlhl.securefinancemanager.model.database.DatabaseEntryLabel;
import at.htlhl.securefinancemanager.model.database.DatabaseLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.DATABASE_ENCRYPTION_DECRYPTION_KEY;
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
 * @fullName Fischer, Jessica Christina
 * @version 3.1
 * @since 01.04.2024 (version 3.1)
 */
@Repository
public class EntryLabelRepository {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Repository for managing label-related data in the database.
     */
    @Autowired
    private LabelRepository labelRepository;

    /**
     * Repository for managing entry-related data in the database.
     */
    @Autowired
    private EntryRepository entryRepository;

    /**
     * SQL query to retrieve labels for a specific entry and user from the 'labels' and 'entry_labels' tables in the database.
     */
    private static final String SELECT_LABELS_FOR_ENTRY = "SELECT pk_label_id, " +
            "pgp_sym_decrypt(label_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_label_name," +
            "pgp_sym_decrypt(label_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_label_description," +
            "fk_label_colour_id " +
            "FROM labels " +
            "JOIN entry_labels ON labels.pk_label_id = entry_labels.fk_label_id " +
            "WHERE entry_labels.fk_entry_id = ? AND entry_labels.fk_user_id = ? AND labels.fk_user_id = ?;";

    /**
     * SQL query to retrieve entries for a specific label and user from the 'entries' and 'entry_labels' tables in the database.
     */
    private static final String SELECT_ENTRIES_FOR_LABEL = "SELECT pk_entry_id, " +
            "pgp_sym_decrypt(entry_name, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_name, " +
            "pgp_sym_decrypt(entry_description, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_description, " +
            "pgp_sym_decrypt(entry_amount, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_amount, " +
            "pgp_sym_decrypt(entry_creation_time, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_creation_time, " +
            "pgp_sym_decrypt(entry_time_of_transaction, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_time_of_transaction, " +
            "pgp_sym_decrypt(entry_attachment, '" + DATABASE_ENCRYPTION_DECRYPTION_KEY + "') AS decrypted_entry_attachment, " +
            "fk_subcategory_id " +
            "FROM entries " +
            "JOIN entry_labels ON entries.pk_entry_id = entry_labels.fk_entry_id " +
            "WHERE entry_labels.fk_label_id = ? AND entry_labels.fk_user_id = ? AND entries.fk_user_id = ?;";

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
     * @param entryId  The ID of the entry to retrieve labels for.
     * @param username The username of the logged-in user.
     * @return A list of Label objects representing the labels associated with the entry and user.
     * @throws ValidationException If the specified entryLabel does not exist or if the provided username is invalid.
     *                             This exception may indicate that the entryId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the entry.
     */
    public List<DatabaseLabel> getLabelsForEntry(int entryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_LABELS_FOR_ENTRY)) {
            ps.setInt(1, entryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, activeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                List<DatabaseLabel> databaseLabels = new ArrayList<>();
                while (rs.next()) {
                    int labelId = rs.getInt("pk_label_id");
                    String decryptedLabelName = rs.getString("decrypted_label_name");
                    String decryptedLabelDescription = rs.getString("decrypted_label_description");
                    int labelColourId = rs.getInt("fk_label_colour_id");

                    databaseLabels.add(new DatabaseLabel(labelId, decryptedLabelName, decryptedLabelDescription, labelColourId, activeUserId));
                }
                if (databaseLabels.isEmpty()) {
                    throw new ValidationException("No labels found for an entry with ID " + entryId);
                }
                return databaseLabels;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of entries associated with a specific label and user from the 'entries' and 'entry_labels' tables in the database.
     *
     * @param labelId  The ID of the label to retrieve entries for.
     * @param username The username of the logged-in user.
     * @return A list of Label objects representing the entries associated with the label and user.
     * @throws ValidationException If the specified entryLabel does not exist or if the provided username is invalid.
     *                             This exception may indicate that the labelId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the label.
     */
    public List<DatabaseEntry> getEntriesForLabel(int labelId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ENTRIES_FOR_LABEL)) {
            ps.setInt(1, labelId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, activeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                List<DatabaseEntry> databaseEntries = new ArrayList<>();
                while (rs.next()) {
                    int entryId = rs.getInt("pk_entry_id");
                    String decryptedEntryName = rs.getString("decrypted_entry_name");
                    String decryptedEntryDescription = rs.getString("decrypted_entry_description");
                    String decryptedEntryAmount = rs.getString("decrypted_entry_amount");
                    String decryptedEntryCreationTime = rs.getString("decrypted_entry_creation_time");
                    String decryptedEntryTimeOfTransaction = rs.getString("decrypted_entry_time_of_transaction");
                    String decryptedEntryAttachment = rs.getString("decrypted_entry_attachment");
                    int subcategoryId = rs.getInt("fk_subcategory_id");

                    databaseEntries.add(new DatabaseEntry(entryId, subcategoryId, decryptedEntryName, decryptedEntryDescription,
                            decryptedEntryAmount, decryptedEntryTimeOfTransaction, decryptedEntryAttachment, decryptedEntryCreationTime, activeUserId));
                }
                if (databaseEntries.isEmpty()) {
                    throw new ValidationException("No entries found for an label with ID " + labelId);
                }
                return databaseEntries;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a label to an entry in the 'entry_labels' table in the database.
     *
     * @param entryId  The ID of the entry to add the label to.
     * @param labelId  The ID of the label to add.
     * @param username The username of the logged-in user.
     * @return The newly created entryLabel.
     * @throws ValidationException If the specified entry or label does not exist or if the provided username is invalid.
     *                             This exception may indicate that the entryId or the labelId is not found or that the
     *                             userId associated with the provided username does not match the expected owner of the
     *                             entry or the label.
     */
    public DatabaseEntryLabel addLabelToEntry(int entryId, int labelId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            labelRepository.getLabel(labelId, username);
            entryRepository.getEntryWithoutSubcategoryId(entryId, username);

            try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
                 PreparedStatement ps = conn.prepareStatement(ADD_LABEL_TO_ENTRY, new String[]{"pk_entry_label_id"})) {
                ps.setInt(1, entryId);
                ps.setInt(2, labelId);
                ps.setInt(3, activeUserId);
                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> ps, keyHolder);
                return new DatabaseEntryLabel(Objects.requireNonNull(keyHolder.getKey()).intValue(), entryId, labelId, activeUserId);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Removes a label from an entry in the 'entry_labels' table in the database.
     *
     * @param entryId  The ID of the entry to remove the label from.
     * @param labelId  The ID of the label to remove.
     * @param username The username of the logged-in user.
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException If the specified entryLabel does not exist or if the provided username is invalid.
     *                             This exception may indicate that the entryId or the labelId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the entry and the label.
     */
    public int removeLabelFromEntry(int entryId, int labelId, String username) throws ValidationException {
        try {
            labelRepository.getLabel(labelId, username);
            entryRepository.getEntryWithoutSubcategoryId(entryId, username);

            try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
                 PreparedStatement ps = conn.prepareStatement(REMOVE_LABEL_FROM_ENTRY)) {
                ps.setInt(1, entryId);
                ps.setInt(2, labelId);
                ps.setInt(3, userSingleton.getUserId(username));
                return ps.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}