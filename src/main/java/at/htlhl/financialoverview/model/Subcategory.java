package at.htlhl.financialoverview.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "subcategories")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id")
    private int subcategoryId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "subcategory_name")
    private byte[] subcategoryName;

    @Column(name = "subcategory_description")
    private byte[] subcategoryDescription;

    @ManyToOne
    @JoinColumn(name = "subcategory_colour")
    private Colour subcategoryColour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
    private List<Entry> entries;
}