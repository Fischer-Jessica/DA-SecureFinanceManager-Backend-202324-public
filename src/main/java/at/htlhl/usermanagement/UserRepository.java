package at.htlhl.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Repository
public class UserRepository {

    private static final String SELECT_USERS = "SELECT id,username,password,email FROM users;";

    private static final String INSERT_USER = "INSERT INTO users (username, password, email) VALUES (?,?,?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Users> getUsers() {
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

    public int insertUser(String username, String password, String email) throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"id"});
            ps.setBytes(1, Base64.getEncoder().encode(username.getBytes()));
            ps.setBytes(2, Base64.getEncoder().encode(password.getBytes()));
            ps.setString(3, email);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
}
