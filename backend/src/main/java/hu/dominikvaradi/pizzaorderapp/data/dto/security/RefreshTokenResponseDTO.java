package hu.dominikvaradi.pizzaorderapp.data.dto.security;

import java.io.Serializable;

public class RefreshTokenResponseDTO implements Serializable {
    private String accessToken;

    private String refreshToken;

    private String tokenType;

    public RefreshTokenResponseDTO(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
