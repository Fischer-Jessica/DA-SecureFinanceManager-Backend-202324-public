package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all users")
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @GetMapping("/users/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one user")
    public User getUser(@PathVariable("userid") int userId) {
        return userRepository.getUser(userId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new user")
    public int addUser(@RequestParam("username") String username, @RequestParam("password") byte[] password, @RequestParam("email") String eMailAddress, String firstName, String lastName) {
        return userRepository.addUser(username, password, eMailAddress, firstName, lastName);
    }

    @PatchMapping("/users/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing user")
    public void updateUser(@PathVariable("userid") int userId, @RequestBody User user) {
        userRepository.updateUser(userId, user);
    }

    @DeleteMapping("/users/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "deletes an user")
    public void deleteUser(@PathVariable("userid") int userId, @RequestParam byte[] password) {
        userRepository.deleteUser(userId, password);
    }
}
