package ru.javacourse.eventmanagement.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javacourse.eventmanagement.locations.model.EventLocationEntity;
@Repository
public interface LocationRepository extends JpaRepository<EventLocationEntity, Integer> {

}
