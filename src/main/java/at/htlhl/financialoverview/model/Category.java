package at.htlhl.financialoverview.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "category_name")
    private byte[] categoryName;

    @Column(name = "category_description")
    private byte[] categoryDescription;

    @ManyToOne
    @JoinColumn(name = "category_colour_id")
    private Colour colour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Subcategory> subcategories;
}