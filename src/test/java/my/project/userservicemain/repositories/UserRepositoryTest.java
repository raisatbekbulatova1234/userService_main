package my.project.userservicemain.repositories;

import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.UserDTO;
import my.project.userservicemain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;


    @Test
    void findOneByUsername_exists() {
        String username = "admin";
        User user = new User(null, username, "pass", null, null);
        underTest.save(user);

        User userExp = underTest.findOneByUsername(username);

        assertThat(userExp).isEqualTo(user);
    }
    @Test
    void findOneByUsername_notExists() {
        String username = "admin";
        User user = new User(null, username, "pass", null, null);
        underTest.save(user);

        User userExp = underTest.findOneByUsername("name");

        assertThat(userExp).isNull();
    }

    @Test
    void findAllUserDTO() {
        User user1 = new User(null, "admin", "pass", null, null);
        User user2 = new User(null, "user", "pass", null, null);
        underTest.save(user1);
        underTest.save(user2);

        List<UserDTO> users = underTest.findAllUserDTO();

        assertThat(users.size()).isEqualTo(2);
    }
}