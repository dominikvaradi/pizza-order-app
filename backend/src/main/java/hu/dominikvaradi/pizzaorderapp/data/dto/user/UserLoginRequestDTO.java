package hu.dominikvaradi.pizzaorderapp.data.dto.user;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class UserLoginRequestDTO implements Serializable {
    @NotBlank
    private final String username;

    private final String password;

    public UserLoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginRequestDTO entity = (UserLoginRequestDTO) o;
        return Objects.equals(this.username, entity.username) &&
                Objects.equals(this.password, entity.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "password = " + password + ")";
    }
}
