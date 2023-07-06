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

    public User getUser(int userId) {
        return null;
    }

    public int addUser(String username, byte[] password, String eMailAdresse, String firstName, String lastName) {
        return 0;
    }

    public void updateUser(int userId, User user) {

    }

    public void deleteUser(int userId, byte[] password) {

    }
}
