package ru.javacourse.eventmanagement.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javacourse.eventmanagement.locations.model.LocationEntity;
@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {

}
