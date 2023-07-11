package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

/**
 * The {@code EntryLabel} class represents the association between an Entry and a Label in a many-to-many relationship.
 * It is an entity class that maps to the 'entry_labels' table in the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains two fields:
 * <ul>
 * <li>{@code entry}: Represents the Entry associated with this EntryLabel.</li>
 * <li>{@code label}: Represents the Label associated with this EntryLabel.</li>
 * </ul>
 * </p>
 *
 * @author Fischer
 * @version 1.1
 * @since 11.07.2023 (version 1.1)
 *
 * @see Entry
 * @see Label
 */
@Entity
@Table(name = "entry_labels", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_entry_id", "fk_label_id"}))
@SecondaryTable(name = "entries", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_entry_id"))
@SecondaryTable(name = "labels", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_label_id"))
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "fk_user_id"))
public class EntryLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_entry_label_id")
    private int entryLabelId;

    /** The Entry associated with this EntryLabel. */
    @ManyToOne
    @JoinColumn(name = "fk_entry_id", referencedColumnName = "pk_entry_id", table = "entries", insertable=false, updatable=false)
    private Entry entry;

    /** The Label associated with this EntryLabel. */
    @ManyToOne
    @JoinColumn(name = "fk_label_id", referencedColumnName = "pk_label_id", table = "labels", insertable=false, updatable=false)
    private Label label;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_user_id", table = "users", insertable=false, updatable=false)
    private User userId;
}
