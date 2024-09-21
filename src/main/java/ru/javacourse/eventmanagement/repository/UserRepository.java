package ru.javacourse.eventmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacourse.eventmanagement.entity.user.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    boolean existsByLogin(String login);
}