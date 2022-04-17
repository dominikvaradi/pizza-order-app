package hu.dominikvaradi.pizzaorderapp.data.model.dto.user;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class UserSaveRequestDTO implements Serializable {
    private final long id;

    @Length(max = 50)
    private final String username;

    @Length(max = 100)
    private final String password;

    @Length(max = 255)
    @Email(regexp = User.REGEX_EMAIL)
    private final String email;

    @Length(max = 15)
    @Pattern(message = "Invalid phone number!", regexp = User.REGEX_PHONE_NUMBER)
    private final String phoneNumber;

    @Length(max = 50)
    private final String fullName;

    private final String roleName;

    public UserSaveRequestDTO(long id, String username, String password, String email, String phoneNumber, String fullName, String roleName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.roleName = roleName;
    }

    public long getId() {
        return id;
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

    public String getRoleName() {
        return roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSaveRequestDTO entity = (UserSaveRequestDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.fullName, entity.fullName) &&
                Objects.equals(this.roleName, entity.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, phoneNumber, fullName, roleName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "password = " + password + ", " +
                "email = " + email + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "fullName = " + fullName + ", " +
                "roleName = " + roleName + ")";
    }
}
