package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserRegisterRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.ConflictException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User createUser(UserRegisterRequestDTO user, PasswordEncoder passwordEncoder) throws ConflictException;

    User findUserByUsername(String username) throws NotFoundException;

    Page<User> getAllUsers(String term, Pageable pageable);

    User getUserById(long userId) throws NotFoundException;

    User editUserById(long userId, UserEditRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException, NotFoundException;

    void deleteUserById(long userId) throws NotFoundException;
}
