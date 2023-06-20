package my.project.userservicemain.controllers;

import javassist.NotFoundException;
import my.project.userservicemain.configuration.exceptions.InvalidRequestException;
import my.project.userservicemain.controller.UserController;
import my.project.userservicemain.entities.Role;
import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.RoleDTO;
import my.project.userservicemain.entities.dto.UserDTO;
import my.project.userservicemain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock private UserService userService;
    private UserController underTest;
    private List<User> users;

    @BeforeEach
    void setUp() {
        underTest = new UserController(userService);
        Role role_1 = new Role(1L, "ROLE_ADMIN");
        Role role_2 = new Role(2L, "ROLE_USER");
        users = new ArrayList<>(List.of(
            new User(1L, "admin", "pass", "max", List.of(role_1, role_2)),
            new User(2L, "user", "pass", "bob", List.of(role_1))));
    }

    @Test
    void getAllUsers() {
        given(userService.findAll()).willReturn(users);
        //when
        ResponseEntity<List<User>> entity = underTest.getAllUsers();
        List<User> user = entity.getBody();
        //then
        assertThat(user).isNotNull();
        assertThat(user.size()).isEqualTo(2);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllUserDTO() {
        given(userService.findAllUserDTO()).willReturn(users.stream()
                .map(user -> new UserDTO() {
                            @Override
                            public Long getId() {
                                return user.getId();
                            }
                            @Override
                            public String getUsername() {
                                return user.getUsername();
                            }
                            @Override
                            public String getName() {
                                return user.getName();
                            }
                            @Override
                            public Collection<RoleDTO> getRoles() {
                                return user.getRoles().stream()
                                        .map(role -> (RoleDTO) role::getName)
                                        .collect(Collectors.toList());
                            }
                        })
                .collect(Collectors.toList()));
        //when
        ResponseEntity<List<UserDTO>> entity = underTest.getAllUserDTO();
        List<UserDTO> user = entity.getBody();
        //then
        assertThat(user).isNotNull();
        assertThat(user.size()).isEqualTo(2);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getUser() {
        User user = users.get(0);
        given(userService.getById(anyLong())).willReturn(user);
        //when
        ResponseEntity<User> entity = underTest.getUser(user.getId());
        User userExp = entity.getBody();
        //then
        assertThat(userExp).isNotNull();
        assertThat(userExp).isEqualTo(user);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService).getById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(user.getId());
    }

    @Test
    void createUser_success() {
        User user = new User(null, "admin", "pass", null, null);
        given(userService.findByUsername(user.getUsername())).willReturn(null);
        given(userService.save(user)).willReturn(user);
        //when
        ResponseEntity<User> entity = underTest.createUser(user);
        User userExp = entity.getBody();
        //then
        assertThat(userExp).isNotNull();
        assertThat(userExp).isEqualTo(user);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void createUser_throwMustNotBeNull() {
        User user_null = null;
        User user_name_null = new User(null, null, "pass", null, null);
        User user_pass_null = new User(null, "admin", null, null, null);
        //when
        //then
        assertThatThrownBy(() -> underTest.createUser(user_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or Username & Password must not be null!");

        assertThatThrownBy(() -> underTest.createUser(user_name_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or Username & Password must not be null!");

        assertThatThrownBy(() -> underTest.createUser(user_pass_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or Username & Password must not be null!");

        verify(userService, never()).save(any());
    }

    @Test
    void createUser_throwIdMustBeNull() {
        User user = new User(1L, "admin", "pass", null, null);
        //when
        //then
        assertThatThrownBy(() -> underTest.createUser(user))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("ID must be null!");

        verify(userService, never()).save(any());
    }

    @Test
    void createUser_throwAlreadyExist() {
        User user = new User(null, "admin", "pass", null, null);
        given(userService.findByUsername(user.getUsername())).willReturn(user);
        //when
        //then
        assertThatThrownBy(() -> underTest.createUser(user))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(user.getUsername() + " already exist.");

        verify(userService, never()).save(any());
    }

    @Test
    void updateUser_success() throws NotFoundException {
        User user = users.get(0);
        given(userService.getById(user.getId())).willReturn(user);
        given(userService.update(user)).willReturn(user);
        //when
        ResponseEntity<User> entity = underTest.updateUser(user);
        User userExp = entity.getBody();
        //then
        assertThat(userExp).isNotNull();
        assertThat(userExp).isEqualTo(user);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).update(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void updateUser_throwMustNotBeNull() {
        User user_null = null;
        User user_id_null = new User(null, "admin", "pass", null, null);
        User user_name_null = new User(1L, null, "pass", null, null);
        User user_pass_null = new User(1L, "admin", null, null, null);
        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(user_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or ID & Username & Password must not be null!");
        assertThatThrownBy(() -> underTest.updateUser(user_id_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or ID & Username & Password must not be null!");
        assertThatThrownBy(() -> underTest.updateUser(user_name_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or ID & Username & Password must not be null!");
        assertThatThrownBy(() -> underTest.updateUser(user_pass_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User or ID & Username & Password must not be null!");

        verify(userService, never()).update(any());
    }

    @Test
    void updateUser_throwDoesNotExist() {
        User user = users.get(0);
        given(userService.getById(user.getId())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID " + user.getId() + " does not exist.");

        verify(userService, never()).update(any());
    }

    @Test
    void deleteUser_success() throws NotFoundException {
        User user = users.get(0);
        given(userService.getById(user.getId())).willReturn(user);
        //when
        ResponseEntity<Boolean> entity = underTest.deleteUser(user.getId());
        Boolean result = entity.getBody();
        //then
        assertThat(result).isNotNull();
        assertThat(result).isTrue();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService).deleteUser(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(user.getId());
    }

    @Test
    void deleteUser_throwDoesNotExist() {
        Long id = 1L;
        given(userService.getById(id)).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID " + id + " does not exist.");

        verify(userService, never()).deleteUser(any());
    }
}