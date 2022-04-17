package hu.dominikvaradi.pizzaorderapp.data.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Digits(integer = 4, fraction = 0)
    @Column(name = "zip_code", nullable = false)
    private short zipCode;

    @Length(max = 40)
    @NotBlank
    @Column(name = "city", nullable = false, length = 40)
    private String city;

    @Length(max = 50)
    @NotBlank
    @Column(name = "street", nullable = false, length = 50)
    private String street;

    @Length(max = 10)
    @NotBlank
    @Column(name = "house_number", nullable = false, length = 10)
    private String houseNumber;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address() {
    }

    public Address(short zipCode, String city, String street, String houseNumber, User user) {
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public short getZipCode() {
        return zipCode;
    }

    public void setZipCode(short zipCode) {
        this.zipCode = zipCode;
    }

    public long getId() {
        return id;
    }
}