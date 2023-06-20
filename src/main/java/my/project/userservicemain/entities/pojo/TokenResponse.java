package my.project.userservicemain.entities.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String name;
    private List<String> roles;

    public TokenResponse(String token, Long id, String username, String name, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.name = name;
        this.roles = roles;
    }
}
