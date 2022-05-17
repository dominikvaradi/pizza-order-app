package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteByUserId(long userId);

    Optional<Cart> findByUserId(long userId);
}