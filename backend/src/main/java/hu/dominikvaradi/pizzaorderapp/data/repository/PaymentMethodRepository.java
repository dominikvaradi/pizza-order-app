package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.PaymentMethod;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByName(EPaymentMethod name);
}