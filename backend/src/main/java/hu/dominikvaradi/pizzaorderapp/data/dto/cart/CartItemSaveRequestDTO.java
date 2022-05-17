package hu.dominikvaradi.pizzaorderapp.data.dto.cart;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

public class CartItemSaveRequestDTO implements Serializable {
    private final long pizzaId;

    private final long userId;

    @Positive
    private final int amount;

    public CartItemSaveRequestDTO(long pizzaId, long userId, int amount) {
        this.pizzaId = pizzaId;
        this.userId = userId;
        this.amount = amount;
    }

    public long getPizzaId() {
        return pizzaId;
    }

    public long getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemSaveRequestDTO entity = (CartItemSaveRequestDTO) o;
        return Objects.equals(this.pizzaId, entity.pizzaId) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.amount, entity.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizzaId, userId, amount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "pizzaId = " + pizzaId + ", " +
                "cartId = " + userId + ", " +
                "amount = " + amount + ")";
    }
}
