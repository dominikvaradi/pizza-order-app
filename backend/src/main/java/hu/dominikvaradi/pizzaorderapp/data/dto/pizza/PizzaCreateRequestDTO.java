package hu.dominikvaradi.pizzaorderapp.data.dto.pizza;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

public class PizzaCreateRequestDTO implements Serializable {
    @NotBlank
    private final String name;

    private final String description;

    @Positive
    private final int price;

    public PizzaCreateRequestDTO(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaCreateRequestDTO entity = (PizzaCreateRequestDTO) o;
        return Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.price, entity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "price = " + price + ")";
    }
}
