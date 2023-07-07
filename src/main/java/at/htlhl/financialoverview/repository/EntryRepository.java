package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Entry;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EntryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Entry> getEntries(int categoryId, int subcategoryId, User loggedInUser) {
        return null;
    }

    public Entry getEntry(int categoryId, int subcategoryId, int entryId, User loggedInUser) {
        return null;
    }

    public int addEntry(int categoryId, int subcategoryId, byte[] entryName, byte[] entryDescription, byte[] entryAmount, byte[] entryCreationTime, byte[] entryTimeOfExpense, byte[] entryAmount1, int entryLabelId, User loggedInUser) {
        return 0;
    }

    public void updateEntry(int categoryId, int subcategoryId, Entry updatedEntry, User loggedInUser) {
    }

    public void updateEntryName(int categoryId, int subcategoryId, byte[] updatedEntryName, User loggedInUser) {
    }

    public void updateEntryDescription(int categoryId, int subcategoryId, byte[] updatedEntryDescription, User loggedInUser) {
    }

    public void updateEntryAmount(int categoryId, int subcategoryId, byte[] updatedEntryAmount, User loggedInUser) {
    }

    public void updateEntryTimeOfExpense(int categoryId, int subcategoryId, byte[] entryTimeOfExpense, User loggedInUser) {
    }

    public void updateEntryAttachment(int categoryId, int subcategoryId, byte[] updatedEntryAttachment, User loggedInUser) {
    }

    public void updateEntryLabelId(int categoryId, int subcategoryId, int updatedEntryLabelId, User loggedInUser) {
    }

    public void deleteEntry(int categoryId, int subcategoryId, int entryId, User loggedInUser) {
    }
}
