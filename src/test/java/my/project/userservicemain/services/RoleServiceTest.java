package my.project.userservicemain.services;

import my.project.userservicemain.entities.Role;
import my.project.userservicemain.repository.RoleRepository;
import my.project.userservicemain.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;
    private RoleService underTest;

    @BeforeEach
    void setUp(){
        underTest = new RoleService(roleRepository);
    }

    @Test
    void canGetAll() {
        //when
        underTest.getAll();
        //then
        verify(roleRepository).findAll();
    }

    @Test
    void canSave() {
        Role role = new Role(null, "ROLE");
        underTest.save(role);

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgumentCaptor.capture());
        assertThat(roleArgumentCaptor.getValue()).isEqualTo(role);
    }

    @Test
    void canUpdate() {
        Role role = new Role(1L, "ROLE");
        given(roleRepository.findById(role.getId())).willReturn(Optional.of(role));

        underTest.update(role);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleRepository).findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(role.getId());

    }

    @Test
    void canDeleteById() {
        Long id = 1L;
        underTest.deleteById(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleRepository).deleteById(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void canGetById() {
        Long id = 1L;
        underTest.getById(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleRepository).findById(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void canFindByName() {
        String name = "ROLE";
        underTest.findByName(name);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(roleRepository).findOneByName(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(name);
    }
}