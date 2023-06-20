package my.project.userservicemain.repositories;

import my.project.userservicemain.entities.Role;
import my.project.userservicemain.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository underTest;

    @Test
    void findOneByName_exists() {
        //given
        String name = "ROLE_ADMIN";
        Role role = new Role(null, name);
        underTest.save(role);
        //when
        Role roleExp = underTest.findOneByName(name);
        //then
        assertThat(roleExp).isEqualTo(role);
    }

    @Test
    void findOneByName_notExists() {
        Role role = new Role(null, "ROLE_ADMIN");
        underTest.save(role);

        Role roleExp = underTest.findOneByName("ADMIN");

        assertThat(roleExp).isNull();
    }
}