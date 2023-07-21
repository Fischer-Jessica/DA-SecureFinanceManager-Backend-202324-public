package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.model.Label;
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
 * The EntryLabelRepository interface serves as a Spring Data JPA repository for the EntryLabel entity.
 * It provides methods to access and perform database operations on the 'entry_labels' table in the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This repository interfaces with the EntryLabel entity class, which is a POJO (Plain Old Java Object) or entity class that maps to the 'entry_labels' table in the database.
 * It extends the JpaRepository interface, which provides generic CRUD (Create, Read, Update, Delete) operations for the EntryLabel entity, eliminating the need for boilerplate code.
 * </p>
 *
 * <p>
 * By extending the JpaRepository, this interface inherits all the standard database operations like saving, updating, finding, and deleting EntryLabel entities.
 * It allows for customization and addition of custom queries through method naming conventions or the use of the @Query annotation.
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
 * @version 1.0
 * @since 21.07.2023 (version 1.0)
 */
@Repository
public class EntryLabelRepository {
    private static final String SELECT_LABELS_FOR_ENTRY = "SELECT pk_label_id, label_name, label_description, fk_label_colour_id " +
            "FROM labels " +
            "JOIN entry_labels ON labels.pk_label_id = entry_labels.fk_label_id " +
            "WHERE entry_labels.fk_entry_id = ? AND entry_labels.fk_user_id = ? AND entry_labels.fk_user_id = ?;";

    private static final String ADD_LABEL_TO_ENTRY = "INSERT INTO entry_labels " +
            "(fk_entry_id, fk_label_id, fk_user_id) " +
            "VALUES (?, ?, ?);";

    private static final String REMOVE_LABEL_FROM_ENTRY = "DELETE FROM entry_labels " +
            "WHERE fk_entry_id = ? AND fk_label_id = ? AND fk_user_id = ?;";

    public List<Label> getLabelsForEntry(int entryId, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_LABELS_FOR_ENTRY);
                ps.setInt(1, entryId);
                ps.setInt(2, loggedInUser.getUserId());
                ps.setInt(3, loggedInUser.getUserId());
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

    public int addLabelToEntry(int entryId, int labelId, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

                UserRepository.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = conn.prepareStatement(ADD_LABEL_TO_ENTRY, new String[]{"pk_entry_label_id"});
                    ps.setInt(1, entryId);
                    ps.setInt(2, labelId);
                    ps.setInt(3, loggedInUser.getUserId());
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

    public void removeLabelFromEntry(int entryId, int labelId, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = UserRepository.jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(REMOVE_LABEL_FROM_ENTRY);
                ps.setInt(1, entryId);
                ps.setInt(2, labelId);
                ps.setInt(3, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
