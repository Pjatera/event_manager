package ru.javacourse.eventmanagement.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.entity.event.RegistrationEntity;
import ru.javacourse.eventmanagement.db.entity.user.UserEntity;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByEventAndUser(EventEntity event, UserEntity user);

    @Query("""
            SELECT r FROM RegistrationEntity r
             JOIN FETCH r.user u
             JOIN FETCH r.event e 
            WHERE e.id = :eventId AND 
            u.id = :userId 
            """)
    Optional<RegistrationEntity> findByEventAndUser(Long eventId, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RegistrationEntity r WHERE r.id = :id")
    void deleteRegistrationById(@Param("id") Long id);
}
