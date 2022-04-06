package hu.dominikvaradi.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Digits;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Digits(integer = 4, fraction = 0)
    @Column(name = "zip_code", nullable = false)
    private short zipCode;

    @Column(name = "city", nullable = false, length = 40)
    private String city;

    @Column(name = "street", nullable = false, length = 50)
    private String street;

    @Column(name = "house_number", nullable = false, length = 10)
    private String houseNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Long getId() {
        return id;
    }
}