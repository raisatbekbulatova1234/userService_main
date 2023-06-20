package my.project.userservicemain.services;

import my.project.userservicemain.entities.Role;
import my.project.userservicemain.entities.User;
import my.project.userservicemain.repository.UserRepository;
import my.project.userservicemain.service.UserService;
import my.project.userservicemain.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void canFindAllUserDTO() {
        underTest.findAllUserDTO();
        verify(userRepository).findAllUserDTO();
    }

    @Test
    void canFindAll() {
        underTest.findAll();
        verify(userRepository).findAll();
    }

    @Test
    void canUpdate() {
        User user = new User(1L, "admin", "pass", null, null);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        underTest.update(user);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(user.getId());
    }

    @Test
    void canSave() {
        User user = new User(null, "admin", "pass", null, null);
        underTest.save(user);

        ArgumentCaptor<User> roleArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(roleArgumentCaptor.capture());
        assertThat(roleArgumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void canDeleteUser() {
        Long id = 1L;
        underTest.deleteUser(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).deleteById(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void canGetById() {
        Long id = 1L;
        underTest.getById(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void canFindByUsername() {
        String username = "admin";
        underTest.findByUsername(username);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findOneByUsername(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(username);
    }

    @Test
    void loadUserByUsername_success() {
        String username = "admin";
        Collection<Role> roles = new ArrayList<>(List.of(new Role(1L, "ROLE_ADMIN")));
        User user = new User(1L, username, "pass", null, roles);
        given(userRepository.findOneByUsername(username)).willReturn(user);
        //when
        underTest.loadUserByUsername(username);
        //then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findOneByUsername(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(username);
    }

    @Test
    void loadUserByUsername_throwWhenUserTaken() {
        String username = "admin";
        given(userRepository.findOneByUsername(username)).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Invalid username: " + username);
    }
}