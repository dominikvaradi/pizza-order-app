package hu.dominikvaradi.pizzaorderapp.data.model.dto.order;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaResponseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderWithPizzasResponseDTO implements Serializable {
    private final long id;

    private final LocalDateTime createdAt;

    private final String orderStatusName;

    private final long userId;

    private final AddressResponseDTO address;

    private final String paymentMethodName;

    private final List<PizzaResponseDTO> itemPizzas;

    public OrderWithPizzasResponseDTO(long id, LocalDateTime createdAt, String orderStatusName, long userId, AddressResponseDTO address, String paymentMethodName, List<PizzaResponseDTO> itemPizzas) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderStatusName = orderStatusName;
        this.userId = userId;
        this.address = address;
        this.paymentMethodName = paymentMethodName;
        this.itemPizzas = itemPizzas;
    }

    public static OrderWithPizzasResponseDTO build(Order order) {
        return new OrderWithPizzasResponseDTO(order.getId(),
            order.getCreatedAt(),
            order.getOrderStatus().getName().toString(),
            order.getUser().getId(),
            AddressResponseDTO.build(order.getAddress()),
            order.getPaymentMethod().getName().toString(),
            order.getItems()
                .stream()
                .map(orderItem -> PizzaResponseDTO.build(orderItem.getPizza()))
                .collect(Collectors.toList()));
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

    public AddressResponseDTO getAddress() {
        return address;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public List<PizzaResponseDTO> getItemPizzas() {
        return itemPizzas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderWithPizzasResponseDTO entity = (OrderWithPizzasResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.orderStatusName, entity.orderStatusName) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.paymentMethodName, entity.paymentMethodName) &&
                Objects.equals(this.itemPizzas, entity.itemPizzas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderStatusName, userId, address, paymentMethodName, itemPizzas);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "createdAt = " + createdAt + ", " +
                "orderStatusName = " + orderStatusName + ", " +
                "userId = " + userId + ", " +
                "address = " + address + ", " +
                "paymentMethodName = " + paymentMethodName + ", " +
                "itemPizzas = " + itemPizzas + ")";
    }
}
