package my.project.userservicemain.controllers;

import my.project.userservicemain.controller.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class TestControllerTest {

    private TestController underTest;

    @BeforeEach
    void setUp() {
        underTest = new TestController();
    }


    @Test
    void allAccess() {
        assertThat(underTest.allAccess()).isEqualTo("public API");
    }

    @Test
    void userAccess() {
        assertThat(underTest.userAccess()).isEqualTo("user API");
    }

    @Test
    void adminAccess() {
        assertThat(underTest.adminAccess()).isEqualTo("admin API");
    }
}