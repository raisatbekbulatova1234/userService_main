package my.project.userservicemain.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import my.project.userservicemain.configuration.exceptions.InvalidRequestException;
import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.UserDTO;
import my.project.userservicemain.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/dto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUserDTO() {
        return ResponseEntity.ok().body(userService.findAllUserDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user == null || user.getPassword() == null || user.getUsername() == null){
            throw new InvalidRequestException("User or Username & Password must not be null!");
        }
        if (user.getId() != null){
            throw new InvalidRequestException("ID must be null!");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new InvalidRequestException(user.getUsername() + " already exist.");
        }
        return ResponseEntity.ok().body(userService.save(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) throws NotFoundException {
        if (user == null || user.getId() == null || user.getPassword() == null || user.getUsername() == null){
            throw new InvalidRequestException("User or ID & Username & Password must not be null!");
        }
        if (userService.getById(user.getId()) == null) {
            throw new NotFoundException("User with ID " + user.getId() + " does not exist.");
        }
        return ResponseEntity.ok().body(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (userService.getById(id) == null) {
            throw new NotFoundException("User with ID " + id + " does not exist.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body(true);
    }

}