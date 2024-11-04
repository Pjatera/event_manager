package ru.javacourse.eventmanagement.web.kafka;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Builder
public record EventChangeKafkaMessage(List<Long> usersId, Long eventId, Long ownerId, Long changedById,
                                      FieldChangeDto<String> name, FieldChangeDto<Integer> maxPlaces,
                                      FieldChangeDto<LocalDateTime> date, FieldChangeDto<BigDecimal> cost,
                                      FieldChangeDto<Integer> duration, FieldChangeDto<Long> locationId,
                                      FieldChangeDto<String> status) {

}
