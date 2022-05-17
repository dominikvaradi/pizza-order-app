package hu.dominikvaradi.pizzaorderapp.data.dto.order;

import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponseDTO implements Serializable {
    private final long id;

    private final LocalDateTime createdAt;

    private final String orderStatusName;

    private final long userId;

    private final String username;

    private final AddressResponseDTO address;

    private final String paymentMethodName;

    private final List<OrderItemResponseDTO> items;

    private int totalSum;

    public OrderResponseDTO(long id, LocalDateTime createdAt, String orderStatusName, long userId, String username, AddressResponseDTO address, String paymentMethodName, List<OrderItemResponseDTO> items, int totalSum) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderStatusName = orderStatusName;
        this.userId = userId;
        this.username = username;
        this.address = address;
        this.paymentMethodName = paymentMethodName;
        this.items = items;
        this.totalSum = totalSum;
    }

    public static OrderResponseDTO build(Order order, boolean withItems) {
        return new OrderResponseDTO(
            order.getId(),
            order.getCreatedAt(),
            order.getOrderStatus().getName().toString(),
            order.getUser().getId(),
            order.getUser().getUsername(),
            AddressResponseDTO.build(order.getAddress()),
            order.getPaymentMethod().getName().toString(),
            withItems ? order.getItems()
                    .stream()
                    .map(OrderItemResponseDTO::build)
                    .collect(Collectors.toList()) : null,
            order.getItems()
                    .stream()
                    .mapToInt(orderItem -> orderItem.getAmount() * orderItem.getPizza().getPrice())
                    .sum()
        );
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public AddressResponseDTO getAddress() {
        return address;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public int getTotalSum() {
        return totalSum;
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
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.paymentMethodName, entity.paymentMethodName) &&
                Objects.equals(this.totalSum, entity.totalSum) &&
                Objects.equals(this.items, entity.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderStatusName, userId, username, address, paymentMethodName, items, totalSum);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "createdAt = " + createdAt + ", " +
                "orderStatusName = " + orderStatusName + ", " +
                "userId = " + userId + ", " +
                "username = " + username + ", " +
                "address = " + address + ", " +
                "paymentMethodName = " + paymentMethodName + ", " +
                "totalSum = " + totalSum + ", " +
                "items = " + items + ")";
    }
}
