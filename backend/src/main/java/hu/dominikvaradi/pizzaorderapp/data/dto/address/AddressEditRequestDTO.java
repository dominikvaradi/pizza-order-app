package hu.dominikvaradi.pizzaorderapp.data.dto.address;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.util.Objects;

public class AddressEditRequestDTO implements Serializable {
    private final long id;

    @Digits(integer = 4, fraction = 0)
    private final short zipCode;

    @Length(max = 40)
    private final String city;

    @Length(max = 50)
    private final String street;

    @Length(max = 10)
    private final String houseNumber;

    public AddressEditRequestDTO(long id, short zipCode, String city, String street, String houseNumber) {
        this.id = id;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEditRequestDTO entity = (AddressEditRequestDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.zipCode, entity.zipCode) &&
                Objects.equals(this.city, entity.city) &&
                Objects.equals(this.street, entity.street) &&
                Objects.equals(this.houseNumber, entity.houseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zipCode, city, street, houseNumber);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "zipCode = " + zipCode + ", " +
                "city = " + city + ", " +
                "street = " + street + ", " +
                "houseNumber = " + houseNumber + ")";
    }
}
