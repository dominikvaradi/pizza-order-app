package hu.dominikvaradi.pizzaorderapp.data.model.dto.cart;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

public class CartAddPizzaRequestDTO implements Serializable {
    private final long pizzaId;

    @Positive
    private final int count;

    public CartAddPizzaRequestDTO(long pizzaId, int count) {
        this.pizzaId = pizzaId;
        this.count = count;
    }

    public long getPizzaId() {
        return pizzaId;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartAddPizzaRequestDTO entity = (CartAddPizzaRequestDTO) o;
        return Objects.equals(this.pizzaId, entity.pizzaId) && Objects.equals(this.count, entity.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizzaId, count);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "pizzaId = " + pizzaId + ", " +
                "count = " + count + ")";
    }
}
