package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Label;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EntryLabelRepository {
    public List<Label> getLabelsForEntry(int entryId) {
        return null;
    }

    public void addLabelToEntry(int entryId, int labelId) {
    }

    public void removeLabelFromEntry(int entryId, int labelId) {
    }
}
