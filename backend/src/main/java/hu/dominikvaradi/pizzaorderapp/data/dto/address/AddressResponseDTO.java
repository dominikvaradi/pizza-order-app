package hu.dominikvaradi.pizzaorderapp.data.dto.address;

import hu.dominikvaradi.pizzaorderapp.data.model.Address;

import java.io.Serializable;
import java.util.Objects;

public class AddressResponseDTO implements Serializable {
    private final Long id;

    private final short zipCode;

    private final String city;

    private final String street;

    private final String houseNumber;

    private final Long userId;

    public AddressResponseDTO(Long id, short zipCode, String city, String street, String houseNumber, Long userId) {
        this.id = id;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.userId = userId;
    }

    public static AddressResponseDTO build(Address address) {
        return new AddressResponseDTO(address.getId(),
            address.getZipCode(),
            address.getCity(),
            address.getStreet(),
            address.getHouseNumber(),
            address.getUser().getId());
    }

    public Long getId() {
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

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressResponseDTO entity = (AddressResponseDTO) o;
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
