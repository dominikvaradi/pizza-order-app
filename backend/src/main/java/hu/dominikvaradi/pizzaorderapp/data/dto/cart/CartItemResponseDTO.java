package hu.dominikvaradi.pizzaorderapp.data.dto.cart;

import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;

import java.io.Serializable;
import java.util.Objects;

public class CartItemResponseDTO implements Serializable {
    private final long id;

    private final PizzaResponseDTO pizza;

    private final long cartId;

    private final int amount;

    public CartItemResponseDTO(long id, PizzaResponseDTO pizza, long cartId, int amount) {
        this.id = id;
        this.pizza = pizza;
        this.cartId = cartId;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public PizzaResponseDTO getPizza() {
        return pizza;
    }

    public long getCartId() {
        return cartId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemResponseDTO entity = (CartItemResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.pizza, entity.pizza) &&
                Objects.equals(this.cartId, entity.cartId) &&
                Objects.equals(this.amount, entity.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pizza, cartId, amount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "pizza = " + pizza + ", " +
                "cartId = " + cartId + ", " +
                "amount = " + amount + ")";
    }

    public static CartItemResponseDTO build(CartItem cartItem) {
        return new CartItemResponseDTO(
            cartItem.getId(),
            PizzaResponseDTO.build(cartItem.getPizza()),
            cartItem.getCart().getId(),
            cartItem.getAmount()
        );
    }
}
