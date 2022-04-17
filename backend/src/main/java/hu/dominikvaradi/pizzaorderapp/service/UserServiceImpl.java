package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Cart;
import hu.dominikvaradi.pizzaorderapp.data.model.Role;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.ERole;
import hu.dominikvaradi.pizzaorderapp.data.repository.CartRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.RoleRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.UserRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CartRepository cartRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public User createUser(UserSaveRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Given username has already taken by someone else!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Given email has already taken by someone else!");
        }

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).orElse(new Role(ERole.ROLE_USER));
        Cart usersCart = new Cart();
        User newUser = new User(user.getUsername(),
            passwordEncoder.encode(user.getPassword()),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getFullName(),
            roleUser,
            usersCart
        );

        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User findUserByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User with given username has not found! (username = " + username + ")"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long userId) throws NotFoundException {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (id = " + userId + ")"));
    }

    @Override
    public void editUserById(long userId, UserSaveRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException, NotFoundException {
        if (userId != user.getId()) {
            throw new BadRequestException("User's id is not the same as the path id!");
        }

        User userToEdit = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (id = " + userId + ")"));

        // Only request with ADMIN role can modify a user's role and username.
        Collection<? extends GrantedAuthority> requestAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (requestAuthorities.contains(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.toString()))) {
            if (user.getRoleName() != null) {
                userToEdit.setRole(roleRepository.findByName(ERole.valueOf(user.getRoleName()))
                    .orElseThrow(() -> new NotFoundException("Role with given name has not found! (roleName = " + user.getRoleName() + ")")));
            }

            if (user.getUsername() != null) {
                if (userRepository.existsByUsernameAndIdIsNot(user.getUsername(), userToEdit.getId())) {
                    throw new BadRequestException("Given username has already taken by someone else!");
                }

                userToEdit.setUsername(user.getUsername());
            }
        }

        if (user.getEmail() != null) {
            if (userRepository.existsByEmailAndIdIsNot(user.getEmail(), userToEdit.getId())) {
                throw new BadRequestException("Given email has already taken by someone else!");
            }

            userToEdit.setEmail(user.getEmail());
        }

        if (user.getFullName() != null) {
            userToEdit.setFullName(user.getFullName());
        }

        if (user.getPhoneNumber() != null) {
            userToEdit.setPhoneNumber(user.getPhoneNumber());
        }

        if (user.getPassword() != null) {
            userToEdit.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(userToEdit);
    }

    @Override
    public void deleteUserById(long userId) throws NotFoundException {
        if (cartRepository.existsById(userId)) {
            cartRepository.deleteByUserId(userId);
        } else {
            throw new NotFoundException("User with given id has not found! (id = " + userId + ")");
        }
    }
}
