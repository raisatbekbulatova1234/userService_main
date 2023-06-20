package my.project.userservicemain.repository;

import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);

    @Query("SELECT u FROM User u ORDER BY u.id")
    List<UserDTO> findAllUserDTO();
}
