package ru.javacourse.eventmanagement.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final EventService eventService;


    @Scheduled(cron = "${event.status.cron}")
    @Async
    public void performTaskWithUpdateEventStatus() {
        eventService.performTaskWithUpdateEventStatus();
    }

}
