package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

@Entity
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private int labelId;

    @Column(name = "label_name")
    private byte[] labelName;

    @Column(name = "label_description")
    private byte[] labelDescription;

    @ManyToOne
    @JoinColumn(name = "label_colour")
    private Colour labelColour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}