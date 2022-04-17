package hu.dominikvaradi.pizzaorderapp.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cart_item")
public class CartItem {
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
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    private Cart cart;

    public CartItem(Pizza pizza, Cart cart) {
        this.pizza = pizza;
        this.cart = cart;
    }

    public CartItem() {

    }

    public Cart getCart() {
        return cart;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public long getId() {
        return id;
    }
}