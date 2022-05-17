package hu.dominikvaradi.pizzaorderapp.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "order_item", uniqueConstraints = {
        @UniqueConstraint(name = "uc_orderitem_pizza_id_order_id", columnNames = {"pizza_id", "order_id"})
})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "pizza_id", nullable = false, updatable = false)
    private Pizza pizza;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @Positive
    @Column(name = "amount", nullable = false)
    private int amount;

    public OrderItem() {
    }

    public OrderItem(Pizza pizza, Order order, int amount) {
        this.pizza = pizza;
        this.order = order;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public long getId() {
        return id;
    }
}