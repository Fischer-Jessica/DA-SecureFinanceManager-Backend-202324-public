package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private int entryId;

    @Column(name = "entry_name")
    private byte[] entryName;

    @Column(name = "entry_description")
    private byte[] entryDescription;

    @Column(name = "entry_amount")
    private byte[] entryAmount;

    @Column(name = "entry_creation_time")
    private byte[] entryCreationTime;

    @Column(name = "entry_time_of_expense")
    private byte[] entryTimeOfExpense;

    @Column(name = "entry_attachment")
    private byte[] entryAttachment;

    @ManyToOne
    @JoinColumn(name = "entry_label_id")
    private Label entryLabel;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}