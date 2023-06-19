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
    public List<Users> getUsers() throws SQLException {
        return (List<Users>) userRepository.getUsers();
    }
}
