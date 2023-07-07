package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getUsers() {
        return null;
    }

    public int addUser(String username, byte[] password, String eMailAddress, String firstName, String lastName) {
        return 0;
    }

    public void updateUser(User updatedUser, User oldUser) {

    }

    public void updateUsername(String username, User loggedInUser) {
    }

    public void updatePassword(String updatedPassword, User loggedInUser) {
    }

    public void updateEMailAddress(String updatedEMailAddress, User loggedInUser) {
    }

    public void updateFirstName(String updatedFirstName, User loggedInUser) {
    }

    public void updateLastName(String updatedLastName, User loggedInUser) {
    }

    public void deleteUser(User loggedInUser) {

    }
}
