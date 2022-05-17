package hu.dominikvaradi.pizzaorderapp.data.dto.order;

import java.io.Serializable;
import java.util.Objects;

public class OrderStatusEditRequestDTO implements Serializable {
    private final long id;

    private final long orderStatusId;

    public OrderStatusEditRequestDTO(long id, long orderStatusId) {
        this.id = id;
        this.orderStatusId = orderStatusId;
    }

    public long getId() {
        return id;
    }

    public long getOrderStatusId() {
        return orderStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusEditRequestDTO entity = (OrderStatusEditRequestDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.orderStatusId, entity.orderStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderStatusId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "orderStatusId = " + orderStatusId + ")";
    }
}
