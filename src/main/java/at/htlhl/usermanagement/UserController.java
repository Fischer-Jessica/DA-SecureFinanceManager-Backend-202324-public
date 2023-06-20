package at.htlhl.usermanagement;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
