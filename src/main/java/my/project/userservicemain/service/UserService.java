package my.project.userservicemain.service;

import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    List<User> findAll();

    void deleteUser(Long id);

    User getById(Long id);

    User save(User user);

    User update(User user);

    List<UserDTO> findAllUserDTO();

}