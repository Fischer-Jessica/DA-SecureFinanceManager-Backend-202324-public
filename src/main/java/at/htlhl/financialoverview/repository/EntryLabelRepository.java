package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Label;
import org.springframework.stereotype.Repository;

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
    public List<Label> getLabelsForEntry(int entryId) {
        return null;
    }

    public void addLabelToEntry(int entryId, int labelId) {
    }

    public void removeLabelFromEntry(int entryId, int labelId) {
    }
}
