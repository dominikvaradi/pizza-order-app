package hu.dominikvaradi.pizzaorderapp.data.dto.enums;

import hu.dominikvaradi.pizzaorderapp.data.model.PaymentMethod;

import java.io.Serializable;
import java.util.Objects;

public class PaymentMethodDTO implements Serializable {
    private final long id;

    private final String name;

    public PaymentMethodDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PaymentMethodDTO build(PaymentMethod paymentMethod) {
        return new PaymentMethodDTO(paymentMethod.getId(), paymentMethod.getName().toString());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethodDTO entity = (PaymentMethodDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}
