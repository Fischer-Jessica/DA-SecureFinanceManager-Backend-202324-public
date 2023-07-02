package at.htlhl.financialoverview.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * The {@code User} class represents a user entity in the 'users' table of the 'financial_overview' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is annotated with the {@link Entity} annotation, indicating that it is an entity class to be managed and persisted in the database.
 * The {@link Table} annotation specifies the name of the table in the database that corresponds to this entity.
 * </p>
 *
 * <p>
 * This class contains several fields representing user properties, such as username, password, email address, first name, and last name.
 * It also includes a one-to-many relationship with the {@link Category} class, where a user can have multiple categories associated with them.
 * </p>
 *
 * @author Fischer
 * @version 1
 * @since 02.07.2023 (version 1)
 *
 * @see Category this class (Category) for the explanations of the annotations
 */

@Entity
@Table(name = "users")
public class User {
    /** the id of the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_user_id")
    private int userId;

    /** the username of the user */
    @Column(name = "username")
    private String username;

    /** the password of the user */
    @Column(name = "password")
    private byte[] password;

    /** the email address of the user */
    @Column(unique = true, name = "email_address")
    private String eMailAddress;

    /** the first name of the user */
    @Column(name = "first_name")
    private String firstName;

    /** the last name of the user */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The categories associated with the user.
     * Represents a one-to-many relationship between User and Category.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Category> categories;
}
