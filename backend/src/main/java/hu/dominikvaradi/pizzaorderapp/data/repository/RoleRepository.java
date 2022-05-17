package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.Role;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}