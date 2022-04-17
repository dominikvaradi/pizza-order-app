package hu.dominikvaradi.pizzaorderapp.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item")
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

    public OrderItem() {
    }

    public OrderItem(Pizza pizza, Order order) {
        this.pizza = pizza;
        this.order = order;
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