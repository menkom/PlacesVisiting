package info.mastera.userserviceapi.service;

import info.mastera.userserviceapi.producer.EmailProducer;
import info.mastera.userserviceapi.repository.TripRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SchedulerService {

    private final TripRepository tripRepository;
    private final EmailProducer emailCalendarEventProducer;

    public SchedulerService(TripRepository tripRepository,
                            EmailProducer emailCalendarEventProducer
    ) {
        this.tripRepository = tripRepository;
        this.emailCalendarEventProducer = emailCalendarEventProducer;
    }

    @Scheduled(cron = "0 0 9 * * *") //second, minute, hour, day of month, month, day(s) of week
    public void collectInformation() {
        tripRepository.findByDate(LocalDate.now().plusDays(1))
                .forEach(emailCalendarEventProducer::sendTripReminder);
    }
}
