package hu.dominikvaradi.pizzaorderapp.data.model.dto.address;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class AddressSaveRequestDTO implements Serializable {
    private final long id;

    @Digits(integer = 4, fraction = 0)
    private final short zipCode;

    @Length(max = 40)
    @NotBlank
    private final String city;

    @Length(max = 50)
    @NotBlank
    private final String street;

    @Length(max = 10)
    @NotBlank
    private final String houseNumber;

    private final long userId;

    public AddressSaveRequestDTO(long id, short zipCode, String city, String street, String houseNumber, long userId) {
        this.id = id;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public short getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressSaveRequestDTO entity = (AddressSaveRequestDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.zipCode, entity.zipCode) &&
                Objects.equals(this.city, entity.city) &&
                Objects.equals(this.street, entity.street) &&
                Objects.equals(this.houseNumber, entity.houseNumber) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zipCode, city, street, houseNumber, userId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "zipCode = " + zipCode + ", " +
                "city = " + city + ", " +
                "street = " + street + ", " +
                "houseNumber = " + houseNumber + ", " +
                "userId = " + userId + ")";
    }
}
