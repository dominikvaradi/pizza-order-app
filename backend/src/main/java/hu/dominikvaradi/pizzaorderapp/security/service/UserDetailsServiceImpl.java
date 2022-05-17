package hu.dominikvaradi.pizzaorderapp.security.service;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.service.UserService;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            User user = userService.findUserByUsername(username);

            return new UserDetailsImpl(user);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
