package at.htlhl.financialoverview.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

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
 * @version 1.2
 * @since 17.07.2023 (version 1.2)
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
    private String password;

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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_category_id")
    private List<Category> categories;

    public User() {
    }

    public User(int userId, String username, String password, String eMailAddress, String firstName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.eMailAddress = eMailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (!username.equals(user.username)) return false;
        if (!password.equals(user.password)) return false;
        if (!eMailAddress.equals(user.eMailAddress)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        return Objects.equals(categories, user.categories);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + eMailAddress.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }
}
