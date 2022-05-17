package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    @Query("select o from Order o where upper(o.user.username) like upper(concat('%', ?1, '%')) or upper(o.user.fullName) like upper(concat('%', ?1, '%'))")
    Page<Order> findByUserUsernameOrUserFullNameContainsAllIgnoreCase(String term, Pageable pageable);

    Page<Order> findByUserId(long id, Pageable pageable);

    Page<Order> findByOrderStatusNameIsIn(Collection<EOrderStatus> names, Pageable pageable);

    Optional<Order> findByUserIdAndOrderStatusNameIsIn(long id, Collection<EOrderStatus> names);
}