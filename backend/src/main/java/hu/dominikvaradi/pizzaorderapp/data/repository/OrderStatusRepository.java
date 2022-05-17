package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.OrderStatus;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByName(EOrderStatus name);
}