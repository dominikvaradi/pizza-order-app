package hu.dominikvaradi.pizzaorderapp.data.dto.security;

import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserResponseDTO;

import java.io.Serializable;

public class JwtResponseDTO implements Serializable {
    private String token;

    private String refreshToken;

    private String tokenType;

    private UserResponseDTO user;

    public JwtResponseDTO(String token, String refreshToken, String tokenType, UserResponseDTO user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public UserResponseDTO getUser() {
        return user;
    }
}
