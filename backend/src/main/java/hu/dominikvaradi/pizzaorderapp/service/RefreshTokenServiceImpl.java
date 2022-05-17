package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.RefreshToken;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.repository.RefreshTokenRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.UserRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.RefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.jwt.refreshTokenExpirationInMillis}")
    private Long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public RefreshToken createRefreshToken(long userId) throws RefreshTokenException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RefreshTokenException("User with given id has not found!"));

        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserId(userId);
        if (existingRefreshToken.isPresent()) {
            return existingRefreshToken.get();
        }

        RefreshToken refreshToken = new RefreshToken(
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000),
            user);

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    @Override
    public void verifyExpiration(RefreshToken refreshToken) throws RefreshTokenException {
        if (refreshToken.getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);

            throw new RefreshTokenException("Refresh token was expired. Please make a new login request!");
        }
    }
}
