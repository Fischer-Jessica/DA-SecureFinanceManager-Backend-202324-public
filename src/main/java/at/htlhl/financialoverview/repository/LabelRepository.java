package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The LabelRepository class handles the persistence operations for label data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class LabelRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of labels for the logged-in user.
     *
     * @param loggedInUser The logged-in user.
     * @return A list of Label objects representing the labels.
     */
    public List<Label> getLabels(User loggedInUser) {
        return null;
    }

    /**
     * Retrieves a specific label for the logged-in user.
     *
     * @param labelId      The ID of the label to retrieve.
     * @param loggedInUser The logged-in user.
     * @return The requested Label object.
     */
    public Label getLabel(int labelId, User loggedInUser) {
        return null;
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
    public int addLabel(byte[] labelName, byte[] labelDescription, int labelColourId, User loggedInUser) {
        return 0;
    }

    /**
     * Updates an existing label for the logged-in user.
     *
     * @param updatedLabel  The updated Label object.
     * @param loggedInUser The logged-in user.
     */
    public void updateLabel(Label updatedLabel, User loggedInUser) {
    }

    /**
     * Updates the name of an existing label for the logged-in user.
     *
     * @param labelId       The ID of the label to update.
     * @param labelName     The updated label name.
     * @param loggedInUser The logged-in user.
     */
    public void updateLabelName(int labelId, byte[] labelName, User loggedInUser) {
    }

    /**
     * Updates the description of an existing label for the logged-in user.
     *
     * @param labelId          The ID of the label to update.
     * @param labelDescription The updated label description.
     * @param loggedInUser    The logged-in user.
     */
    public void updateLabelDescription(int labelId, byte[] labelDescription, User loggedInUser) {
    }

    /**
     * Updates the color of an existing label for the logged-in user.
     *
     * @param labelId         The ID of the label to update.
     * @param labelColourId   The updated label color ID.
     * @param loggedInUser The logged-in user.
     */
    public void updateLabelColourId(int labelId, int labelColourId, User loggedInUser) {
    }

    /**
     * Deletes a label for the logged-in user.
     *
     * @param labelId       The ID of the label to delete.
     * @param loggedInUser The logged-in user.
     */
    public void deleteLabel(int labelId, User loggedInUser) {
    }
}