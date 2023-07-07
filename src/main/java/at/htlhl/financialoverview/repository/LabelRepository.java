package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Label;
import at.htlhl.financialoverview.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LabelRepository {
    public List<Label> getLabels(User loggedInUser) {
        return null;
    }

    public Label getLabel(int labelId, User loggedInUser) {
        return null;
    }

    public int addLabel(byte[] labelName, byte[] labelDescription, int labelColourId, User loggedInUser) {
        return 0;
    }

    public void updateLabel(Label updatedLabel, User loggedInUser) {
    }

    public void updateLabelName(int labelId, byte[] labelName, User loggedInUser) {
    }

    public void updateLabelDescription(int labelId, byte[] labelDescription, User loggedInUser) {
    }

    public void updateLabelColourId(int labelId, int labelColourId, User loggedInUser) {
    }

    public void deleteLabel(int labelId, User loggedInUser) {
    }
}