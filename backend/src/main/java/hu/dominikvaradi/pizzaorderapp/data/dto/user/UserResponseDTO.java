package hu.dominikvaradi.pizzaorderapp.data.dto.user;

import hu.dominikvaradi.pizzaorderapp.data.model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserResponseDTO implements Serializable {
    private final long id;

    private final String username;

    private final String email;

    private final String phoneNumber;

    private final String fullName;

    private final String role;

    public UserResponseDTO(long id, String username, String email, String phoneNumber, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.role = role;
    }

    public static UserResponseDTO build(User user) {
        return new UserResponseDTO(user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getFullName(),
            user.getRole().getName().toString());
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDTO entity = (UserResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.fullName, entity.fullName) &&
                Objects.equals(this.role, entity.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, phoneNumber, fullName, role);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "email = " + email + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "fullName = " + fullName + ", " +
                "role = " + role + ")";
    }
}
