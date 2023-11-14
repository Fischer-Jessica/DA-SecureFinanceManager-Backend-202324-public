package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The {@code EntryRepository} class handles the persistence operations for entry data.
 * It serves as a Spring Data JPA repository for the Entry entity.
 *
 * <p>
 * The {@code EntryRepository} serves as an abstraction layer between the EntryController and the underlying data storage, enabling seamless access and manipulation of Entry entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for Entry entities.
 * </p>
 *
 * @author Fischer
 * @version 2.7
 * @since 14.11.2023 (version 2.7)
 */
@Repository
public class EntryRepository {
    /**
     * SQL query to retrieve entries from the 'entries' table in the database for a specific subcategory and user.
     */
    private static final String SELECT_ENTRIES = "SELECT pk_entry_id, entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_transaction, entry_attachment " +
            "FROM entries " +
            "WHERE fk_user_id = ? AND fk_subcategory_id = ?;";

    /**
     * SQL query to retrieve a specific entry from the 'entries' table in the database for a specific subcategory and user.
     */
    private static final String SELECT_ENTRY = "SELECT entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_transaction, entry_attachment " +
            "FROM entries " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /**
     * SQL query to add a new entry to the 'entries' table in the database.
     */
    private static final String INSERT_ENTRY = "INSERT INTO entries " +
            "(fk_subcategory_id, entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_transaction, entry_attachment, fk_user_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * SQL query to update an existing entry in the 'entries' table in the database.
     */
    private static final String UPDATE_ENTRY = "UPDATE entries " +
            "SET fk_subcategory_id = ?, entry_name = ?, entry_description = ?, entry_amount = ?, entry_time_of_transaction = ?, entry_attachment = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /**
     * SQL query to delete an entry from the 'entries' table in the database.
     */
    private static final String DELETE_ENTRY = "DELETE FROM entries " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /**
     * Retrieves all entries from a specific subcategory for a given user.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param username  The logged-in user.
     * @return A list of entries.
     */
    public List<DatabaseEntry> getEntries(int subcategoryId, String username) {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ENTRIES);
            ps.setInt(1, activeUserId);
            ps.setInt(2, subcategoryId);
            ResultSet rs = ps.executeQuery();

            List<DatabaseEntry> databaseEntries = new ArrayList<>();

            while (rs.next()) {
                int entryId = rs.getInt("pk_entry_id");
                byte[] entryName = rs.getBytes("entry_name");
                byte[] entryDescription = rs.getBytes("entry_description");
                byte[] entryAmount = rs.getBytes("entry_amount");
                byte[] entryCreationTime = rs.getBytes("entry_creation_time");
                byte[] entryTimeOfTransaction = rs.getBytes("entry_time_of_transaction");
                byte[] entryAttachment = rs.getBytes("entry_attachment");

                String stringEntryName = null;
                String stringEntryDescription = null;
                String stringEntryAttachment = null;

                if (entryName != null) {
                    stringEntryName = Base64.getEncoder().encodeToString(entryName);
                }
                if (entryDescription != null) {
                    stringEntryDescription = Base64.getEncoder().encodeToString(entryDescription);
                }
                if (entryAttachment != null) {
                    stringEntryAttachment = Base64.getEncoder().encodeToString(entryAttachment);
                }

                databaseEntries.add(new DatabaseEntry(entryId, subcategoryId, stringEntryName , stringEntryDescription,
                        Base64.getEncoder().encodeToString(entryAmount), Base64.getEncoder().encodeToString(entryCreationTime),
                        Base64.getEncoder().encodeToString(entryTimeOfTransaction),stringEntryAttachment, activeUserId));
            }
            return databaseEntries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specific entry from a subcategory for a given user.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId       The ID of the entry.
     * @param username      The username of the logged-in user.
     * @return The entry object.
     * @throws ValidationException  If the specified entry does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the entry.
     */
    public DatabaseEntry getEntry(int subcategoryId, int entryId, String username) throws ValidationException {
        int activeUserId = userSingleton.getUserId(username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, activeUserId);
            ps.setInt(3, subcategoryId);
            ResultSet rs = ps.executeQuery();

            DatabaseEntry databaseEntry = null;
            if (rs.next()) {
                byte[] entryName = rs.getBytes("entry_name");
                byte[] entryDescription = rs.getBytes("entry_description");
                byte[] entryAmount = rs.getBytes("entry_amount");
                byte[] entryCreationTime = rs.getBytes("entry_creation_time");
                byte[] entryTimeOfTransaction = rs.getBytes("entry_time_of_transaction");
                byte[] entryAttachment = rs.getBytes("entry_attachment");

                String stringEntryName = null;
                String stringEntryDescription = null;
                String stringEntryAttachment = null;

                if (entryName != null) {
                    stringEntryName = Base64.getEncoder().encodeToString(entryName);
                }
                if (entryDescription != null) {
                    stringEntryDescription = Base64.getEncoder().encodeToString(entryDescription);
                }
                if (entryAttachment != null) {
                    stringEntryAttachment = Base64.getEncoder().encodeToString(entryAttachment);
                }

                databaseEntry = new DatabaseEntry(entryId, subcategoryId, stringEntryName, stringEntryDescription,
                        Base64.getEncoder().encodeToString(entryAmount), Base64.getEncoder().encodeToString(entryCreationTime),
                        Base64.getEncoder().encodeToString(entryTimeOfTransaction), stringEntryAttachment, activeUserId);
            }
            if (databaseEntry == null) {
                throw new ValidationException("Entry with ID " + entryId + " not found.");
            }
            return databaseEntry;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new entry to a specific subcategory for a given user.
     *
     * @param newEntry  The new entry to be added.
     * @return The ID of the added entry.
     */
    public DatabaseEntry addEntry(DatabaseEntry newEntry) {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            byte[] entryCreationTimeBytes = currentTimestamp.toString().getBytes();

            UserRepository.jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_ENTRY, new String[]{"pk_entry_id"});
                ps.setInt(1, newEntry.getEntrySubcategoryId());
                if (newEntry.getEntryName() == null) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(newEntry.getEntryName()));
                }
                if (newEntry.getEntryDescription() == null) {
                    ps.setNull(3, Types.NULL);
                } else {
                    ps.setBytes(3, Base64.getDecoder().decode(newEntry.getEntryDescription()));
                }
                ps.setBytes(4, Base64.getDecoder().decode(newEntry.getEntryAmount()));
                ps.setBytes(5, entryCreationTimeBytes);
                ps.setBytes(6, Base64.getDecoder().decode(newEntry.getEntryTimeOfTransaction()));
                if (newEntry.getEntryAttachment() == null) {
                    ps.setNull(7, Types.NULL);
                } else {
                    ps.setBytes(7, Base64.getDecoder().decode(newEntry.getEntryAttachment()));
                }
                ps.setInt(8, newEntry.getEntryUserId());
                return ps;
            }, keyHolder);

            return new DatabaseEntry(Objects.requireNonNull(keyHolder.getKey()).intValue(), newEntry.getEntrySubcategoryId(), newEntry.getEntryName(), newEntry.getEntryDescription(), newEntry.getEntryAmount(), Base64.getEncoder().encodeToString(entryCreationTimeBytes), newEntry.getEntryTimeOfTransaction(), newEntry.getEntryAttachment(), newEntry.getEntryUserId());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing entry in a specific subcategory for a given user.
     *
     * @param updatedEntry  The updated entry data.
     * @param username      The username of the logged-in user.
     * @return The updated entry.
     * @throws ValidationException  If the specified entry does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the entry.
     */
    public DatabaseEntry updateEntry(DatabaseEntry updatedEntry, String username) throws ValidationException {
        DatabaseEntry oldDatabaseEntry = getEntry(updatedEntry.getEntrySubcategoryId(), updatedEntry.getEntryId(), username);
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY);

            ps.setInt(1, updatedEntry.getEntrySubcategoryId());

            if (updatedEntry.getEntryName() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedEntry.getEntryName()));
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(oldDatabaseEntry.getEntryName()));
            }

            if (updatedEntry.getEntryDescription() != null) {
                ps.setBytes(3, Base64.getDecoder().decode(updatedEntry.getEntryDescription()));
            } else {
                ps.setBytes(3, Base64.getDecoder().decode(oldDatabaseEntry.getEntryDescription()));
            }

            if (updatedEntry.getEntryAmount() != null) {
                ps.setBytes(4, Base64.getDecoder().decode(updatedEntry.getEntryAmount()));
            } else {
                ps.setBytes(4, Base64.getDecoder().decode(oldDatabaseEntry.getEntryAmount()));
            }

            if (updatedEntry.getEntryTimeOfTransaction() != null) {
                ps.setBytes(5, Base64.getDecoder().decode(updatedEntry.getEntryTimeOfTransaction()));
            } else {
                ps.setBytes(5, Base64.getDecoder().decode(oldDatabaseEntry.getEntryTimeOfTransaction()));
            }

            if (updatedEntry.getEntryAttachment() != null) {
                ps.setBytes(6, Base64.getDecoder().decode(updatedEntry.getEntryAttachment()));
            } else {
                ps.setBytes(6, Base64.getDecoder().decode(oldDatabaseEntry.getEntryAttachment()));
            }

            ps.setInt(7, updatedEntry.getEntryId());
            ps.setInt(8, updatedEntry.getEntryUserId());
            ps.setInt(9, updatedEntry.getEntrySubcategoryId());
            ps.executeUpdate();
            conn.close();
            return getEntry(updatedEntry.getEntrySubcategoryId(), updatedEntry.getEntryId(), username);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes an entry from a specific subcategory for a given user.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId       The ID of the entry to be deleted.
     * @param username      The username of the logged-in user.
     * @throws ValidationException  If the specified entry does not exist or if the provided username is invalid.
     *                              This exception may indicate that the entryId is not found or that the userId associated
     *                              with the provided username does not match the expected owner of the entry.
     */
    public int deleteEntry(int subcategoryId, int entryId, String username) throws ValidationException {
        try {
            Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_ENTRY);
            ps.setInt(1, entryId);
            ps.setInt(2, userSingleton.getUserId(username));
            ps.setInt(3, subcategoryId);
            int rowsAffected = ps.executeUpdate();
            conn.close();

            if (rowsAffected == 0) {
                throw new ValidationException("Entry with ID " + entryId + " not found.");
            }
            return rowsAffected;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}