package hu.dominikvaradi.pizzaorderapp.data.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull
    @Length(max = 255)
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @NotNull
    @Column(name = "expires_at", nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String token, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getToken() {
        return token;
    }

    public long getId() {
        return id;
    }
}