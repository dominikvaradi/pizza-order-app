package hu.dominikvaradi.pizzaorderapp.data.model;

import hu.dominikvaradi.pizzaorderapp.data.model.enums.EPaymentMethod;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private EPaymentMethod name;

    public PaymentMethod() {
    }

    public PaymentMethod(EPaymentMethod name) {
        this.name = name;
    }

    public EPaymentMethod getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}