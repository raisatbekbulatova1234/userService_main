package my.project.userservicemain.controllers;

import my.project.userservicemain.controller.AuthController;
import my.project.userservicemain.entities.pojo.LoginRequest;
import my.project.userservicemain.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    private AuthController underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthController(authService);
    }

    @Test
    void authUser() {
        LoginRequest loginRequest = new LoginRequest("user","pass");
        //when
        underTest.authUser(loginRequest);
        //then
        ArgumentCaptor<LoginRequest> roleArgumentCaptor = ArgumentCaptor.forClass(LoginRequest.class);
        verify(authService).authenticate(roleArgumentCaptor.capture());
        assertThat(roleArgumentCaptor.getValue()).isEqualTo(loginRequest);
    }
}