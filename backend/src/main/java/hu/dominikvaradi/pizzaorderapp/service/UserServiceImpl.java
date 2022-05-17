package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserRegisterRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Cart;
import hu.dominikvaradi.pizzaorderapp.data.model.Role;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.ERole;
import hu.dominikvaradi.pizzaorderapp.data.repository.CartRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.RoleRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.UserRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.ConflictException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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
    public User createUser(UserRegisterRequestDTO user, PasswordEncoder passwordEncoder) throws ConflictException {
        if (userRepository.existsByUsername(user.getUsername().trim())) {
            throw new ConflictException("Given username has already taken by someone else!");
        }

        if (userRepository.existsByEmail(user.getEmail().trim())) {
            throw new ConflictException("Given email has already taken by someone else!");
        }

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).orElse(new Role(ERole.ROLE_USER));

        User newUser = new User(user.getUsername().trim(),
            passwordEncoder.encode(user.getPassword()),
            user.getEmail().trim(),
            user.getPhoneNumber(),
            user.getFullName().trim(),
            roleUser
        );
        Cart usersCart = new Cart(newUser);
        cartRepository.save(usersCart);

        return newUser;
    }

    @Override
    public User findUserByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User with given username has not found! (username = " + username + ")"));
    }

    @Override
    public Page<User> getAllUsers(String term, Pageable pageable) {
        if (term == null || term.isBlank()) {
            return userRepository.findAll(pageable);
        }

        return userRepository.findByUsernameOrFullNameContainsAllIgnoreCase(term, pageable);
    }

    @Override
    public User getUserById(long userId) throws NotFoundException {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (id = " + userId + ")"));
    }

    @Override
    public User editUserById(long userId, UserEditRequestDTO user, PasswordEncoder passwordEncoder) throws BadRequestException, NotFoundException {
        if (userId != user.getId()) {
            throw new BadRequestException("User's id is not the same as the path id!");
        }

        User userToEdit = getUserById(userId);

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

        return userRepository.save(userToEdit);
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
