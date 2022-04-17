package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatus_NameIsIn(Collection<EOrderStatus> names);

    Optional<Order> findByUser_IdAndOrderStatus_NameIsIn(long id, Collection<EOrderStatus> names);
}