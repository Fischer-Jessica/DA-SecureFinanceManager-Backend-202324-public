package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new user")
    public int addUser(@RequestParam User newUser) {
        return userRepository.addUser(newUser);
    }

    @PatchMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing user")
    public void updateUser(@RequestBody User updatedUser, @RequestBody User loggedInUser) {
        userRepository.updateUser(updatedUser, loggedInUser);
    }

    @PatchMapping("/users/username")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the username of an existing user")
    public void updateUsername(@RequestParam String updatedUsername, @RequestBody User loggedInUser) {
        userRepository.updateUsername(updatedUsername, loggedInUser);
    }

    @PatchMapping("/users/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the password of an existing user")
    public void updatePassword(@RequestParam String updatedPassword, @RequestBody User loggedInUser) {
        userRepository.updatePassword(updatedPassword, loggedInUser);
    }

    @PatchMapping("/users/eMailAddress")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the e-mail-address of an existing user")
    public void updateEMailAddress(@RequestParam String updatedEMailAddress, @RequestBody User loggedInUser) {
        userRepository.updateEMailAddress(updatedEMailAddress, loggedInUser);
    }

    @PatchMapping("/users/firstName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the first name of an existing user")
    public void updateFirstName(@RequestParam String updatedFirstName, @RequestBody User loggedInUser) {
        userRepository.updateFirstName(updatedFirstName, loggedInUser);
    }

    @PatchMapping("/users/lastName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the last name of an existing user")
    public void updateLastName(@RequestParam String updatedLastName, @RequestBody User loggedInUser) {
        userRepository.updateLastName(updatedLastName, loggedInUser);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "deletes an user")
    public void deleteUser(@RequestBody User loggedInUser) {
        userRepository.deleteUser(loggedInUser);
    }
}
