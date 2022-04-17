package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService {
    User createUser(UserSaveRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException;

    User findUserByUsername(String username) throws NotFoundException;

    List<User> getAllUsers();

    User getUserById(long userId) throws NotFoundException;

    void editUserById(long userId, UserSaveRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException, NotFoundException;

    void deleteUserById(long userId) throws NotFoundException;
}
