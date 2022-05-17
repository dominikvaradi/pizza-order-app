package hu.dominikvaradi.pizzaorderapp.data.dto.security;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RefreshTokenRequestDTO implements Serializable {
    @NotBlank
    private String refreshToken;

    public RefreshTokenRequestDTO() {
    }

    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
