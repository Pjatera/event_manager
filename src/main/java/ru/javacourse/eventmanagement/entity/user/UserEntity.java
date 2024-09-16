package ru.javacourse.eventmanagement.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import ru.javacourse.eventmanagement.domain.users.Role;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    @Min(0)
    private int age;
    @Enumerated(EnumType.STRING)
    private Role role;
}