package at.htlhl.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Repository
public class UserRepository {

    public KeyPair pair;

    private static final String SELECT_USERS = " SELECT id,encode(username,'base64') AS username,encode(password,'base64') AS password,email FROM users;";

    private static final String INSERT_USER = "INSERT INTO users (username, password, email) VALUES (?,?,?);";

    private static final String SELECT_USERNAME = "SELECT pgp_sym_decrypt(username, '?') AS text FROM users WHERE password=?;";

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
            return new Users(id, new String(username), new String(password), email);
        });
    }

    public int insertUser(String username, String password, String email) throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"id"});
            ps.setBytes(1, username.getBytes());
            ps.setBytes(2, password.getBytes());
            ps.setString(3, email);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public PublicKey getPublicKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        pair = generator.generateKeyPair();
        return pair.getPublic();
    }

    public String getUsername(String encryptedPassword) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] encryptedPasswordByte = Base64.getDecoder().decode(encryptedPassword.getBytes());

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

        byte[] decryptedPasswordByte = decryptCipher.doFinal(encryptedPasswordByte);

        return new String(decryptedPasswordByte, StandardCharsets.UTF_8);
    }
}
