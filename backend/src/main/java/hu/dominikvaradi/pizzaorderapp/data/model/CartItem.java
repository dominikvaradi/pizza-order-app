package hu.dominikvaradi.pizzaorderapp.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "cart_item", uniqueConstraints = {
        @UniqueConstraint(name = "uc_cartitem_pizza_id_cart_id", columnNames = {"pizza_id", "cart_id"})
})
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

    @Positive
    @Column(name = "amount", nullable = false)
    private int amount;

    public CartItem(Pizza pizza, Cart cart, int amount) {
        this.pizza = pizza;
        this.cart = cart;
        this.amount = amount;
    }

    public CartItem() {

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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