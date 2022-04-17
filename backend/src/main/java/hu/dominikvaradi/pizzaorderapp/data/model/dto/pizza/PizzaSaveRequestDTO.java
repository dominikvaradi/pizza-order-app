package hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Objects;

public class PizzaSaveRequestDTO implements Serializable {
    private final long id;

    @NotBlank
    private final String name;

    private final String description;

    @PositiveOrZero
    private final int price;

    private final MultipartFile image;

    public PizzaSaveRequestDTO(long id, String name, String description, int price, MultipartFile image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
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

    public MultipartFile getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaSaveRequestDTO entity = (PizzaSaveRequestDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.image, entity.image) &&
                Objects.equals(this.price, entity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, image);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "image = " + image + ", " +
                "price = " + price + ")";
    }
}
