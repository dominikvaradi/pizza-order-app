package hu.dominikvaradi.pizzaorderapp.data.dto.order;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class OrderCreateRequestDTO implements Serializable {
    private final long userId;

    private final long addressId;

    @NotBlank
    private final String paymentMethodName;

    public OrderCreateRequestDTO(long userId, long addressId, String paymentMethodName) {
        this.userId = userId;
        this.addressId = addressId;
        this.paymentMethodName = paymentMethodName;
    }

    public long getUserId() {
        return userId;
    }

    public long getAddressId() {
        return addressId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCreateRequestDTO entity = (OrderCreateRequestDTO) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.addressId, entity.addressId) &&
                Objects.equals(this.paymentMethodName, entity.paymentMethodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, addressId, paymentMethodName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "userId = " + userId + ", " +
                "addressId = " + addressId + ", " +
                "paymentMethodName = " + paymentMethodName + ")";
    }
}
