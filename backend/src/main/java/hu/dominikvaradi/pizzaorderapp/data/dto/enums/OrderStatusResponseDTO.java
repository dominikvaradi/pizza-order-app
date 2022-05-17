package hu.dominikvaradi.pizzaorderapp.data.dto.enums;

import hu.dominikvaradi.pizzaorderapp.data.model.OrderStatus;

import java.io.Serializable;
import java.util.Objects;

public class OrderStatusResponseDTO implements Serializable {
    private final long id;

    private final String name;

    public OrderStatusResponseDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OrderStatusResponseDTO build(OrderStatus orderStatus) {
        return new OrderStatusResponseDTO(orderStatus.getId(), orderStatus.getName().toString());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusResponseDTO entity = (OrderStatusResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}
