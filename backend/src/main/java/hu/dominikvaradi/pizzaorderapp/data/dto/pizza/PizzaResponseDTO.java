package hu.dominikvaradi.pizzaorderapp.data.dto.pizza;

import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;

import java.io.Serializable;
import java.util.Objects;

public class PizzaResponseDTO implements Serializable {
    private final long id;

    private final String name;

    private final String description;

    private final int price;

    private final String imageAPILocation;

    public PizzaResponseDTO(long id, String name, String description, int price, String imageAPILocation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageAPILocation = imageAPILocation;
    }

    public static PizzaResponseDTO build(Pizza pizza) {
        return new PizzaResponseDTO(pizza.getId(),
            pizza.getName(),
            pizza.getDescription(),
            pizza.getPrice(),
            "/api/pizza-images/" + pizza.getImageName());
    }

    public long getId() {
        return id;
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

    public String getImageAPILocation() {
        return imageAPILocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaResponseDTO entity = (PizzaResponseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.imageAPILocation, entity.imageAPILocation) &&
                Objects.equals(this.price, entity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, imageAPILocation);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "imageAPILocation = " + imageAPILocation + ", " +
                "price = " + price + ")";
    }
}
