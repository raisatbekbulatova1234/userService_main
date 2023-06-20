package my.project.userservicemain.controller;

import lombok.RequiredArgsConstructor;
import my.project.userservicemain.entities.pojo.LoginRequest;
import my.project.userservicemain.entities.pojo.TokenResponse;
import my.project.userservicemain.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
//	public ResponseEntity<TokenResponse> authUser(@ModelAttribute LoginRequest loginRequest) {
    public ResponseEntity<TokenResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

}

