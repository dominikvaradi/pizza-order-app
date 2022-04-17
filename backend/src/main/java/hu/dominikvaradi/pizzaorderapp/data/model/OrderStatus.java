package hu.dominikvaradi.pizzaorderapp.data.model;

import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private EOrderStatus name;

    public OrderStatus() {
    }

    public OrderStatus(EOrderStatus name) {
        this.name = name;
    }

    public EOrderStatus getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}