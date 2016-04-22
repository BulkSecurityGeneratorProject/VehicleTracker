package com.zuehlke.camp.vehicletracker.service.scheduled.data;

import com.zuehlke.camp.vehicletracker.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@Component
public class LocationDataCleanup {

    private final Logger log = LoggerFactory.getLogger(LocationDataCleanup.class);

    @Inject
    LocationRepository locationRepository;

    @Scheduled(fixedRate = 600000)
    public void cleanLocationData() {
        ZonedDateTime time = ZonedDateTime.now().minusDays(7);
        log.debug("Cleaning Locations older than {}", time);
        locationRepository.deleteByTimeBefore(time);
    }
}
