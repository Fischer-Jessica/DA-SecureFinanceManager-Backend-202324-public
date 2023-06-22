package at.htlhl.usermanagement;

/**
 * @author Fischer
 * @version 0
 *
 * This is the POJO, which has the same parameters as the table in the postgresql-database.
 * It is used to store the data from the database into a compact object and transfer it easily between the methods.
 *
 * <b>Note: </b> The encrypted columns have the datatype String, although in the database itself they are stored as bytea.
 *          The data gets decrypted in UserRepository.java, but is still encrypted, because the key is owned by the user only.
 */

public class Users {
    private int id;
    private String username;
    private String password;
    private String email;

    public Users(){}

    public Users(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
