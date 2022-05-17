package hu.dominikvaradi.pizzaorderapp.data.dto.enums;

import hu.dominikvaradi.pizzaorderapp.data.model.Role;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class RoleResponseDTO implements Serializable {
    private final long id;

    @NotNull
    private final String name;

    public RoleResponseDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleResponseDTO build(Role role) {
        return new RoleResponseDTO(role.getId(), role.getName().toString());
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
        RoleResponseDTO entity = (RoleResponseDTO) o;
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
