package ee.andu.server.dto;

import lombok.Data;

@Data
public class PersonLoginDto {
    private String email;
    private String password;
}
