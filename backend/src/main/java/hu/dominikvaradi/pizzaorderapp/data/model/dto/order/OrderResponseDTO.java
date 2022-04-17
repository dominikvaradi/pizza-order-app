package hu.dominikvaradi.pizzaorderapp.data.model.dto.order;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressResponseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderResponseDTO implements Serializable {
    private final Long id;

    private final LocalDateTime createdAt;

    private final String orderStatusName;

    private final Long userId;

    private final AddressResponseDTO address;

    private final String paymentMethodName;

    public OrderResponseDTO(Long id, LocalDateTime createdAt, String orderStatusName, Long userId, AddressResponseDTO address, String paymentMethodName) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderStatusName = orderStatusName;
        this.userId = userId;
        this.address = address;
        this.paymentMethodName = paymentMethodName;
    }

    public static OrderResponseDTO build(Order order) {
        return new OrderResponseDTO(order.getId(),
            order.getCreatedAt(),
            order.getOrderStatus().getName().toString(),
            order.getUser().getId(),
            AddressResponseDTO.build(order.getAddress()),
            order.getPaymentMethod().getName().toString());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public Long getUserId() {
        return userId;
    }

    public AddressResponseDTO getAddress() {
        return address;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponseDTO entity = (OrderResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.orderStatusName, entity.orderStatusName) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.paymentMethodName, entity.paymentMethodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderStatusName, userId, address, paymentMethodName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "createdAt = " + createdAt + ", " +
                "orderStatusName = " + orderStatusName + ", " +
                "userId = " + userId + ", " +
                "address = " + address + ", " +
                "paymentMethodName = " + paymentMethodName + ")";
    }
}
