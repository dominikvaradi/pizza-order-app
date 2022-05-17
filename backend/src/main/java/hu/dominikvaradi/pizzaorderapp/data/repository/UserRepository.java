package hu.dominikvaradi.pizzaorderapp.data.repository;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdIsNot(String username, long id);

    boolean existsByEmailAndIdIsNot(String email, long id);

    @Query("select u from User u where upper(u.username) like upper(concat('%', ?1, '%')) or upper(u.fullName) like upper(concat('%', ?1, '%'))")
    Page<User> findByUsernameOrFullNameContainsAllIgnoreCase(String term, Pageable pageable);
}