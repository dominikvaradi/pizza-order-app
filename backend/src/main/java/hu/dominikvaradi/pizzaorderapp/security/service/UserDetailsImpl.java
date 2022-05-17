package hu.dominikvaradi.pizzaorderapp.security.service;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    private final Set<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.user = user;

        this.authorities = Stream.of(user.getRole())
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public User getUser() { return this.user; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserDetailsImpl other = (UserDetailsImpl) o;
        return Objects.equals(this.user, other.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
