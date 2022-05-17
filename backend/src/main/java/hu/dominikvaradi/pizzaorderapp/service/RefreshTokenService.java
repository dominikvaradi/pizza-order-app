package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.RefreshToken;
import hu.dominikvaradi.pizzaorderapp.service.exception.RefreshTokenException;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(long userId) throws RefreshTokenException;

    Optional<RefreshToken> findByToken(String token);

    void verifyExpiration(RefreshToken refreshToken) throws RefreshTokenException;
}
