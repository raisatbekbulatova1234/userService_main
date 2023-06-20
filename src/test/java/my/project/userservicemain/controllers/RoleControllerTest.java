package my.project.userservicemain.controllers;

import javassist.NotFoundException;
import my.project.userservicemain.configuration.exceptions.InvalidRequestException;
import my.project.userservicemain.controller.RoleController;
import my.project.userservicemain.entities.Role;
import my.project.userservicemain.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    @Mock private RoleService roleService;
    private RoleController underTest;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        underTest = new RoleController(roleService);
        roles = new ArrayList<>(List.of(
                new Role(1L, "ROLE_ADMIN"),
                new Role(2L, "ROLE_USER")));
    }

    @Test
    void getRoles() {
        given(roleService.getAll()).willReturn(roles);
        //when
        ResponseEntity<List<Role>> entity = underTest.getRoles();
        List<Role> role = entity.getBody();
        //then
        assertThat(role).isNotNull();
        assertThat(role.size()).isEqualTo(2);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getRole() {
        Role role = roles.get(0);
        given(roleService.getById(anyLong())).willReturn(role);
        //when
        ResponseEntity<Role> entity = underTest.getRole(role.getId());
        Role roleExp = entity.getBody();
        //then
        assertThat(roleExp).isNotNull();
        assertThat(roleExp).isEqualTo(role);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService).getById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(role.getId());
    }

    @Test
    void createRole_success() {
        Role role = new Role(null, "ROLE");
        given(roleService.findByName(role.getName())).willReturn(null);
        given(roleService.save(role)).willReturn(role);
        //when
        ResponseEntity<Role> entity = underTest.createRole(role);
        Role roleExp = entity.getBody();
        //then
        assertThat(roleExp).isNotNull();
        assertThat(roleExp).isEqualTo(role);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleService).save(roleArgumentCaptor.capture());
        assertThat(roleArgumentCaptor.getValue()).isEqualTo(role);
    }

    @Test
    void createRole_throwMustNotBeNull() {
        Role role_null = null;
        Role role_name_null = new Role(1L,null);
        //when
        //then
        assertThatThrownBy(() -> underTest.createRole(role_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Role or Name must not be null!");

        assertThatThrownBy(() -> underTest.createRole(role_name_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Role or Name must not be null!");

        verify(roleService, never()).save(any());
    }

    @Test
    void createRole_throwIdMustBeNull() {
        Role role = new Role(1L, "ROLE");
        //when
        //then
        assertThatThrownBy(() -> underTest.createRole(role))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("ID must be null!");

        verify(roleService, never()).save(any());
    }

    @Test
    void createRole_throwAlreadyExist() {
        Role role = new Role(null, "ROLE");
        given(roleService.findByName(role.getName())).willReturn(role);
        //when
        //then
        assertThatThrownBy(() -> underTest.createRole(role))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(role.getName() + " already exist.");

        verify(roleService, never()).save(any());
    }

    @Test
    void updateRole_success() throws NotFoundException {
        Role role = roles.get(0);
        given(roleService.update(role)).willReturn(role);
        given(roleService.getById(role.getId())).willReturn(role);
        given(roleService.findByName(role.getName())).willReturn(null);
        //when
        ResponseEntity<Role> entity = underTest.updateRole(role);
        Role roleExp = entity.getBody();
        //then
        assertThat(roleExp).isNotNull();
        assertThat(roleExp).isEqualTo(role);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleService).update(roleArgumentCaptor.capture());
        assertThat(roleArgumentCaptor.getValue()).isEqualTo(role);
    }

    @Test
    void updateRole_throwMustNotBeNull() {
        Role role_null = null;
        Role role_id_null = new Role(null, "ROLE");
        Role role_name_null = new Role(1L,null);
        //when
        //then
        assertThatThrownBy(() -> underTest.updateRole(role_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Role or ID & Name must not be null!");

        assertThatThrownBy(() -> underTest.updateRole(role_id_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Role or ID & Name must not be null!");

        assertThatThrownBy(() -> underTest.updateRole(role_name_null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Role or ID & Name must not be null!");

        verify(roleService, never()).update(any());
    }

    @Test
    void updateRole_throwDoesNotExist() {
        Role role = roles.get(0);
        given(roleService.getById(role.getId())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.updateRole(role))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role with ID " + role.getId() + " does not exist.");

        verify(roleService, never()).update(any());
    }

    @Test
    void updateRole_throwAlreadyExist() {
        Role role = roles.get(0);
        given(roleService.getById(role.getId())).willReturn(role);
        given(roleService.findByName(role.getName())).willReturn(role);
        //when
        //then
        assertThatThrownBy(() -> underTest.updateRole(role))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(role.getName() + " already exist.");

        verify(roleService, never()).update(any());
    }


    @Test
    void deleteRole_success() throws NotFoundException {
        Role role = roles.get(0);
        given(roleService.getById(role.getId())).willReturn(role);
        //when
        ResponseEntity<Boolean> entity = underTest.deleteRole(role.getId());
        Boolean result = entity.getBody();
        //then
        assertThat(result).isNotNull();
        assertThat(result).isTrue();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService).deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(role.getId());
    }

    @Test
    void deleteRole_throwDoesNotExist() {
        Long id = 1L;
        given(roleService.getById(id)).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRole(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role with ID " + id + " does not exist.");

        verify(roleService, never()).deleteById(any());
    }
}