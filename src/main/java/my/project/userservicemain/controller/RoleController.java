package my.project.userservicemain.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import my.project.userservicemain.configuration.exceptions.InvalidRequestException;
import my.project.userservicemain.entities.Role;
import my.project.userservicemain.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(roleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        if (role == null || role.getName() == null){
            throw new InvalidRequestException("Role or Name must not be null!");
        }
        if (role.getId() != null){
            throw new InvalidRequestException("ID must be null!");
        }
        if (roleService.findByName(role.getName()) != null) {
            throw new InvalidRequestException(role.getName() + " already exist.");
        }
        return ResponseEntity.ok().body(roleService.save(role));
    }

    @PutMapping
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws NotFoundException {
        if (role == null || role.getId() == null || role.getName() == null){
            throw new InvalidRequestException("Role or ID & Name must not be null!");
        }
        if (roleService.getById(role.getId()) == null) {
            throw new NotFoundException("Role with ID " + role.getId() + " does not exist.");
        }
        if (roleService.findByName(role.getName()) != null) {
            throw new InvalidRequestException(role.getName() + " already exist.");
        }
        return ResponseEntity.ok().body(roleService.update(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (roleService.getById(id) == null) {
            throw new NotFoundException("Role with ID " + id + " does not exist.");
        }
        roleService.deleteById(id);
        return ResponseEntity.ok().body(true);
    }

}