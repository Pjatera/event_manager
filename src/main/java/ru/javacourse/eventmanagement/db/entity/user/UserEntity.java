package ru.javacourse.eventmanagement.db.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import ru.javacourse.eventmanagement.domain.users.Role;

import java.util.Objects;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    @ToString.Exclude
    private String password;
    @Column(name = "age")
    @Min(0)
    private Integer age;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hProxy ? hProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hProxyThis ? hProxyThis.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hProxyThis ? hProxyThis.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
