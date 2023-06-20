package my.project.userservicemain.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import my.project.userservicemain.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            try {
                String username = tokenUtils.getUsernameFromToken(token);
                if (username != null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        filterChain.doFilter(request, response);
    }
}

