package at.htlhl.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Repository
public class UserRepository {

    private static final String SELECT_USERS = "SELECT id,username,password,email FROM users;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Users> getUsers() throws SQLException {
        return jdbcTemplate.query(SELECT_USERS, (rs, rowNum) -> {
            int id = rs.getInt("id");
            byte[] username = rs.getBytes("username");
            byte[] password = rs.getBytes("password");
            String email = rs.getString("email");
            username = Base64.getDecoder().decode(username);
            password = Base64.getDecoder().decode(password);
            System.out.println(new Users(id, new String(username), new String(password), email));
            return new Users(id, new String(username), new String(password), email);
        });
    }
}
