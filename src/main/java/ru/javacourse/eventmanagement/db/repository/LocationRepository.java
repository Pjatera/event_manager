package ru.javacourse.eventmanagement.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

}
