package at.htlhl.usermanagement;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usermanagement")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all users")
    public List<Users> getUsers() {
        return (List<Users>) userRepository.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a user")
    public int insertUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) throws SQLException {
        return userRepository.insertUser(username, password, email);
    }

    @GetMapping("/publickey")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns the public key so users can transfer their key encrypted")
    public String getPublicKey() throws NoSuchAlgorithmException {
        return userRepository.getPublicKey().toString();
    }

    @PostMapping("/getusername")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "insert the encrypted password to get back your username")
    public String getUsername(@RequestParam("encryptedPassword") String encryptedPassword) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return userRepository.getUsername(encryptedPassword);
    }
}
