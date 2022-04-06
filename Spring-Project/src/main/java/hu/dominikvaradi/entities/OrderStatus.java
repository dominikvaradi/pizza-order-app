package hu.dominikvaradi.entities;

import hu.dominikvaradi.entities.enums.EOrderStatus;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private EOrderStatus name;

    public EOrderStatus getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}