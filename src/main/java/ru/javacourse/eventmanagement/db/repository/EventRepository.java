package ru.javacourse.eventmanagement.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.domain.event.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {


    @Query("""
            SELECT e FROM EventEntity e LEFT JOIN FETCH e.ownerUser u 
            LEFT JOIN FETCH e.eventsRegistrationEntities r
            WHERE u.login=:login
            """)
    List<EventEntity> findEventsByOwnerUserLogin(String login);

    @Query("""
            SELECT e FROM EventEntity e 
            WHERE (:name IS NULL OR e.name LIKE %:name%)
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
            AND (CAST(:dateStartAfter as date ) IS NULL OR  e.date >= :dateStartAfter) 
            AND (CAST(:dateStartBefore as date) IS NULL OR  e.date <= :dateStartBefore)
            AND (:costMin IS NULL OR e.cost >= :costMin)
            AND (:costMax IS NULL OR e.cost <= :costMax) 
            AND (:durationMin IS NULL OR e.duration >= :durationMin) 
            AND (:durationMax IS NULL OR e.duration <= :durationMax)
            AND (:location IS NULL OR e.location = :location)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            """)
    List<EventEntity> findByFilters(String name,
                                    Integer placesMin,
                                    Integer placesMax,
                                    LocalDateTime dateStartAfter,
                                    LocalDateTime dateStartBefore,
                                    BigDecimal costMin,
                                    BigDecimal costMax,
                                    Integer durationMin,
                                    Integer durationMax,
                                    LocationEntity location,
                                    EventStatus eventStatus
    );

    @Query("""
            SELECT e FROM EventEntity e        
            LEFT  JOIN FETCH  e.eventsRegistrationEntities r
            LEFT JOIN r.user us
            WHERE us.login=:login      
            """)
    List<EventEntity> findRegistrationEventsByUser(String login);


    @Modifying
    @Transactional
    @Query("update EventEntity e set e.status = :status where e.id in :id")
    void changeEventStatus(EventStatus status, Long... id);

    @Query("""
              SELECT e FROM EventEntity e 
              LEFT JOIN FETCH e.ownerUser u 
              LEFT JOIN FETCH e.eventsRegistrationEntities r
              LEFT JOIN r.user us
              WHERE e.date <= CURRENT_TIMESTAMP
              AND e.status =:eventStart
            """)
    List<EventEntity> findEventWithStatusOfStartedEvents(EventStatus eventStart);

    @Query(value = """
                SELECT e.id FROM events e
                WHERE e.date + INTERVAL '1 minute' * e.duration < CURRENT_TIMESTAMP
                AND e.status = :eventStart
            """, nativeQuery = true)
    List<Long> findEventWithStatusOfFinishedEvents(String eventStart);

    @Query("""
            select e from EventEntity e
            LEFT JOIN FETCH e.ownerUser u 
            LEFT JOIN FETCH e.eventsRegistrationEntities r
            LEFT JOIN r.user us
            where e.id in :ids
            """)
    List<EventEntity> findAllEntityWithId(Long... ids);


    @Query(value = """
            SELECT (COUNT(e) > 0) 
            FROM events e 
            WHERE (e.date <= :date  AND (e.date + INTERVAL '1 minute' * e.duration) >= :date) 
               OR ( e.date <= CAST(:date as date) + INTERVAL '1 minute' * :duration 
                        AND e.date + INTERVAL '1 minute' * e.duration >= CAST(:date as date) + INTERVAL '1 minute' * :duration )
            """, nativeQuery = true)
    boolean hasEventInTimeFrame(LocalDateTime date, int duration);


}
