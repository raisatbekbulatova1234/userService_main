package my.project.userservicemain.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;


    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User user = (User) authentication.getPrincipal();
        return JWT.create()
                .withSubject((user.getUsername()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }

    public String getUsernameFromToken(String token) throws Exception {
        String username;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret.getBytes())).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            username = decodedJWT.getSubject();
        } catch (IllegalArgumentException | JWTVerificationException e) {
            throw new Exception(e);
        }
        return username;
    }

}
