package hu.dominikvaradi.pizzaorderapp.data.dto.order;

import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.OrderItem;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemResponseDTO implements Serializable {
    private final long id;

    private final PizzaResponseDTO pizza;

    private final long orderId;

    private final int amount;

    public OrderItemResponseDTO(long id, PizzaResponseDTO pizza, long orderId, int amount) {
        this.id = id;
        this.pizza = pizza;
        this.orderId = orderId;
        this.amount = amount;
    }

    public static OrderItemResponseDTO build(OrderItem orderItem) {
        return new OrderItemResponseDTO(
            orderItem.getId(),
            PizzaResponseDTO.build(orderItem.getPizza()),
            orderItem.getOrder().getId(),
            orderItem.getAmount()
        );
    }

    public long getId() {
        return id;
    }

    public PizzaResponseDTO getPizza() {
        return pizza;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemResponseDTO entity = (OrderItemResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.pizza, entity.pizza) &&
                Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.amount, entity.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pizza, orderId, amount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "pizza = " + pizza + ", " +
                "orderId = " + orderId + ", " +
                "amount = " + amount + ")";
    }
}
