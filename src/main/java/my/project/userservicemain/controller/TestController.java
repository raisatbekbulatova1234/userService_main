package my.project.userservicemain.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @GetMapping
    public String allAccess() {
        return "public API";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "user API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "admin API";
    }
}