package hu.dominikvaradi.pizzaorderapp.data.model.dto.cart;

import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaResponseDTO;

import java.io.Serializable;
import java.util.Objects;

public class CartItemResponseDTO implements Serializable {
    private final long id;

    private final PizzaResponseDTO pizza;

    public CartItemResponseDTO(long id, PizzaResponseDTO pizza) {
        this.id = id;
        this.pizza = pizza;
    }

    public static CartItemResponseDTO build(CartItem cartItem) {
        return new CartItemResponseDTO(cartItem.getId(),
            PizzaResponseDTO.build(cartItem.getPizza()));
    }

    public long getId() {
        return id;
    }

    public PizzaResponseDTO getPizza() {
        return pizza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemResponseDTO entity = (CartItemResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.pizza, entity.pizza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pizza);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "pizza = " + pizza + ")";
    }
}
