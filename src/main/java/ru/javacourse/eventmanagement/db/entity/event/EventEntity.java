package ru.javacourse.eventmanagement.db.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.domain.event.EventStatus;

import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotNull
    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @NotNull
    @Column(name = "occupied_places", nullable = false)
    private Integer occupiedPlaces;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hProxyThis ? hProxyThis.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EventEntity that = (EventEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hProxyThis ? hProxyThis.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
