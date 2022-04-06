package hu.dominikvaradi.entities;

import hu.dominikvaradi.entities.enums.EAuthority;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "name", nullable = false, unique = true)
    private EAuthority name;

    public EAuthority getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}