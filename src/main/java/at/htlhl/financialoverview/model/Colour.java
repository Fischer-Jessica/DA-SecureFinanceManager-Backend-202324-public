package at.htlhl.financialoverview.model;

import jakarta.persistence.*;

@Entity
@Table(name = "colours")
public class Colour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colour_id")
    private Integer colourId;

    @Column(name = "colour_name")
    private String colourName;
}