package hu.dominikvaradi.entities;

import hu.dominikvaradi.entities.enums.EPaymentMethod;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private EPaymentMethod name;

    public EPaymentMethod getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}