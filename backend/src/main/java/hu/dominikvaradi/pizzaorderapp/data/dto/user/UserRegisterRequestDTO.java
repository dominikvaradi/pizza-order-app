package hu.dominikvaradi.pizzaorderapp.data.dto.user;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class UserRegisterRequestDTO implements Serializable {
    @Length(max = 50)
    @NotBlank
    private final String username;

    @NotBlank
    @Length(max = 100)
    private final String password;

    @Length(max = 255)
    @NotBlank
    @Pattern(message = "Invalid email!", regexp = User.REGEX_EMAIL)
    private final String email;

    @Length(max = 15)
    @NotBlank
    @Pattern(message = "Invalid phone number!", regexp = User.REGEX_PHONE_NUMBER)
    private final String phoneNumber;

    @Length(max = 50)
    @NotBlank
    private final String fullName;

    public UserRegisterRequestDTO(String username, String password, String email, String phoneNumber, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegisterRequestDTO entity = (UserRegisterRequestDTO) o;
        return Objects.equals(this.username, entity.username) &&
                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.fullName, entity.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, phoneNumber, fullName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "password = " + password + ", " +
                "email = " + email + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "fullName = " + fullName + ")";
    }
}
