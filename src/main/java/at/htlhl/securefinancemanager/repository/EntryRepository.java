package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Entry;
import at.htlhl.securefinancemanager.model.User;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * The EntryRepository class handles the persistence operations for entry data.
 * It serves as a Spring Data JPA repository for the Entry entity.
 *
 * <p>
 * The EntryRepository serves as an abstraction layer between the EntryController and the underlying data storage, enabling seamless access and manipulation of Entry entities.
 * </p>
 *
 * <p>
 * This interface should be implemented by a concrete repository class that provides the necessary data access and database operations for Entry entities.
 * </p>
 *
 * @author Fischer
 * @version 1.4
 * @since 25.07.2023 (version 1.4)
 */
@Repository
public class EntryRepository {
    /** SQL query to retrieve entries from the 'entries' table in the database for a specific subcategory and user. */
    private static final String SELECT_ENTRIES = "SELECT pk_entry_id, entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_expense, entry_attachment " +
            "FROM entries " +
            "WHERE fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to retrieve a specific entry from the 'entries' table in the database for a specific subcategory and user. */
    private static final String SELECT_ENTRY = "SELECT entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_expense, entry_attachment " +
            "FROM entries " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to add a new entry to the 'entries' table in the database. */
    private static final String INSERT_ENTRY = "INSERT INTO entries " +
            "(fk_subcategory_id, entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_expense, entry_attachment, fk_user_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    /** SQL query to update an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY = "UPDATE entries " +
            "SET fk_subcategory_id = ?, entry_name = ?, entry_description = ?, entry_amount = ?, entry_time_of_expense = ?, entry_attachment = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ?;";

    /** SQL query to update the subcategory ID of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_SUBCATEGORY_ID = "UPDATE entries " +
            "SET fk_subcategory_id = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ?;";

    /** SQL query to update the name of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY_NAME = "UPDATE entries " +
            "SET entry_name = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to update the description of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY_DESCRIPTION = "UPDATE entries " +
            "SET entry_description = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to update the amount of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY_AMOUNT = "UPDATE entries " +
            "SET entry_amount = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to update the time of expense of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY_TIME_OF_EXPENSE = "UPDATE entries " +
            "SET entry_time_of_expense = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to update the attachment of an existing entry in the 'entries' table in the database. */
    private static final String UPDATE_ENTRY_ATTACHMENT = "UPDATE entries " +
            "SET entry_attachment = ? " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /** SQL query to delete an entry from the 'entries' table in the database. */
    private static final String DELETE_ENTRY = "DELETE FROM entries " +
            "WHERE pk_entry_id = ? AND fk_user_id = ? AND fk_subcategory_id = ?;";

    /**
     * Retrieves all entries from a specific subcategory for a given user.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param loggedInUser  The logged-in user.
     * @return A list of entries.
     */
    public List<Entry> getEntries(int subcategoryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ENTRIES);
                ps.setInt(1, loggedInUser.getUserId());
                ps.setInt(2, subcategoryId);
                ResultSet rs = ps.executeQuery();

                List<Entry> entries = new ArrayList<>();
                while (rs.next()) {
                    int entryId = rs.getInt("pk_entry_id");
                    byte[] entryName = rs.getBytes("entry_name");
                    byte[] entryDescription = rs.getBytes("entry_description");
                    byte[] entryAmount = rs.getBytes("entry_amount");
                    byte[] entryCreationTime = rs.getBytes("entry_creation_time");
                    byte[] entryTimeOfExpense = rs.getBytes("entry_time_of_expense");
                    byte[] entryAttachment = rs.getBytes("entry_attachment");

                    Entry entry = new Entry(entryId, Base64.getEncoder().encodeToString(entryName), Base64.getEncoder().encodeToString(entryDescription),
                            Base64.getEncoder().encodeToString(entryAmount), Base64.getEncoder().encodeToString(entryCreationTime), Base64.getEncoder().encodeToString(entryTimeOfExpense),
                            Base64.getEncoder().encodeToString(entryAttachment), subcategoryId, loggedInUser.getUserId());
                    entries.add(entry);
                }

                return entries;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Retrieves a specific entry from a subcategory for a given user.
     *
     * @param subcategoryId     The ID of the subcategory.
     * @param entryId           The ID of the entry.
     * @param loggedInUser      The logged-in user.
     * @return The entry object.
     */
    public Entry getEntry(int subcategoryId, int entryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ENTRY);
                ps.setInt(1, entryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.setInt(3, subcategoryId);
                ResultSet rs = ps.executeQuery();

                Entry entry = null;
                if (rs.next()) {
                    byte[] entryName = rs.getBytes("entry_name");
                    byte[] entryDescription = rs.getBytes("entry_description");
                    byte[] entryAmount = rs.getBytes("entry_amount");
                    byte[] entryCreationTime = rs.getBytes("entry_creation_time");
                    byte[] entryTimeOfExpense = rs.getBytes("entry_time_of_expense");
                    byte[] entryAttachment = rs.getBytes("entry_attachment");

                    entry = new Entry(entryId, Base64.getEncoder().encodeToString(entryName), Base64.getEncoder().encodeToString(entryDescription),
                            Base64.getEncoder().encodeToString(entryAmount), Base64.getEncoder().encodeToString(entryCreationTime), Base64.getEncoder().encodeToString(entryTimeOfExpense),
                            Base64.getEncoder().encodeToString(entryAttachment), subcategoryId, loggedInUser.getUserId());
                }
                return entry;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Adds a new entry to a specific subcategory for a given user.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param entryName                The name of the entry.
     * @param entryDescription         The description of the entry.
     * @param entryAmount              The amount of the entry.
     * @param entryTimeOfExpense       The time of expense of the entry.
     * @param entryAttachment          The attachment of the entry.
     * @param loggedInUser             The logged-in user.
     * @return The ID of the added entry.
     */
    public int addEntry(int subcategoryId, String entryName, String entryDescription, String entryAmount, String entryTimeOfExpense, String entryAttachment, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                byte[] entryCreationTimeBytes = currentTimestamp.toString().getBytes();

                UserRepository.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = conn.prepareStatement(INSERT_ENTRY, new String[]{"pk_entry_id"});
                    ps.setInt(1, subcategoryId);
                    ps.setBytes(2, Base64.getDecoder().decode(entryName));
                    ps.setBytes(3, Base64.getDecoder().decode(entryDescription));
                    ps.setBytes(4, Base64.getDecoder().decode(entryAmount));
                    ps.setBytes(5, entryCreationTimeBytes);
                    ps.setBytes(6, Base64.getDecoder().decode(entryTimeOfExpense));
                    ps.setBytes(7, Base64.getDecoder().decode(entryAttachment));
                    ps.setInt(8, loggedInUser.getUserId());
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
     * Updates an existing entry in a specific subcategory for a given user.
     *
     * @param updatedSubcategoryId              The updated ID of the subcategory.
     * @param entryId                           The ID of the entry to be updated.
     * @param updatedEntryName                  The updated name of the entry.
     * @param updatedEntryDescription           The updated description of the entry.
     * @param updatedEntryAmount                The updated amount of the entry.
     * @param updatedEntryTimeOfExpense         The updated time of expense of the entry.
     * @param updatedEntryAttachment            The updated attachment of the entry.
     * @param loggedInUser                      The logged-in user.
     */
    public void updateEntry(int updatedSubcategoryId, int entryId, String updatedEntryName, String updatedEntryDescription, String updatedEntryAmount, String updatedEntryTimeOfExpense,
                            String updatedEntryAttachment, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY);
                ps.setInt(1, updatedSubcategoryId);
                ps.setBytes(2, Base64.getDecoder().decode(updatedEntryName));
                ps.setBytes(3, Base64.getDecoder().decode(updatedEntryDescription));
                ps.setBytes(4, Base64.getDecoder().decode(updatedEntryAmount));
                ps.setBytes(5, Base64.getDecoder().decode(updatedEntryTimeOfExpense));
                ps.setBytes(6, Base64.getDecoder().decode(updatedEntryAttachment));
                ps.setInt(7, entryId);
                ps.setInt(8, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the subcategory of an existing entry in the 'entries' table in the database.
     *
     * @param updatedSubcategoryId      The updated ID of the subcategory.
     * @param entryId                   The ID of the entry to be updated.
     * @param loggedInUser              The logged-in user.
     */
    public void updateSubcategoryOfEntry(int updatedSubcategoryId, int entryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SUBCATEGORY_ID);
                ps.setInt(1, updatedSubcategoryId);
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the name of an existing entry in a specific subcategory for a given user.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be updated.
     * @param updatedEntryName         The updated name of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryName(int subcategoryId, int entryId, String updatedEntryName, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY_NAME);
                ps.setBytes(1, Base64.getDecoder().decode(updatedEntryName));
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the description of an existing entry in a specific subcategory for a given user.
     *
     * @param subcategoryId                 The ID of the subcategory.
     * @param entryId                       The ID of the entry to be updated.
     * @param updatedEntryDescription       The updated description of the entry.
     * @param loggedInUser                  The logged-in user.
     */
    public void updateEntryDescription(int subcategoryId, int entryId, String updatedEntryDescription, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY_DESCRIPTION);
                ps.setBytes(1, Base64.getDecoder().decode(updatedEntryDescription));
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the amount of an existing entry in a specific subcategory for a given user.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be updated.
     * @param updatedEntryAmount       The updated amount of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryAmount(int subcategoryId, int entryId, String updatedEntryAmount, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY_AMOUNT);
                ps.setBytes(1, Base64.getDecoder().decode(updatedEntryAmount));
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the time of expense of an existing entry in a specific subcategory for a given user.
     *
     * @param subcategoryId                     The ID of the subcategory.
     * @param entryId                           The ID of the entry to be updated.
     * @param updatedEntryTimeOfExpense         The updated time of expense of the entry.
     * @param loggedInUser                      The logged-in user.
     */
    public void updateEntryTimeOfExpense(int subcategoryId, int entryId, String updatedEntryTimeOfExpense, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY_TIME_OF_EXPENSE);
                ps.setBytes(1, Base64.getDecoder().decode(updatedEntryTimeOfExpense));
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the attachment of an existing entry in a specific subcategory for a given user.
     *
     * @param subcategoryId                 The ID of the subcategory.
     * @param entryId                       The ID of the entry to be updated.
     * @param updatedEntryAttachment        The updated attachment of the entry.
     * @param loggedInUser                  The logged-in user.
     */
    public void updateEntryAttachment(int subcategoryId, int entryId, String updatedEntryAttachment, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_ENTRY_ATTACHMENT);
                ps.setBytes(1, Base64.getDecoder().decode(updatedEntryAttachment));
                ps.setInt(2, entryId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.setInt(4, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Deletes an entry from a specific subcategory for a given user.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be deleted.
     * @param loggedInUser             The logged-in user.
     */
    public void deleteEntry(int subcategoryId, int entryId, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(DELETE_ENTRY);
                ps.setInt(1, entryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.setInt(3, subcategoryId);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
