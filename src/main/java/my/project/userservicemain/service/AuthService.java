package my.project.userservicemain.service;

import lombok.RequiredArgsConstructor;
import my.project.userservicemain.configuration.jwt.TokenUtils;
import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.Role;
import my.project.userservicemain.entities.pojo.LoginRequest;
import my.project.userservicemain.entities.pojo.TokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenUtils tokenUtils;

    public TokenResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String access_token = tokenUtils.generateToken(authentication);

        User user = userService.findByUsername(loginRequest.getUsername());
        List<String> authorities = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new TokenResponse(access_token,
                user.getId(),
                user.getUsername(),
                user.getName(),
                authorities);
    }
}