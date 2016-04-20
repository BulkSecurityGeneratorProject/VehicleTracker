package com.zuehlke.camp.vehicletracker.service.scheduled.simulator;

import com.zuehlke.camp.vehicletracker.config.JHipsterProperties;
import com.zuehlke.camp.vehicletracker.domain.Location;
import com.zuehlke.camp.vehicletracker.domain.Order;
import com.zuehlke.camp.vehicletracker.domain.Stammdaten;
import com.zuehlke.camp.vehicletracker.repository.LocationRepository;
import com.zuehlke.camp.vehicletracker.repository.OrderRepository;
import com.zuehlke.camp.vehicletracker.repository.StammdatenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Random;

@Component
public class LocationSimulator {

    private static final String SIMULATOR_ID = "Simulator-1";

    private final Logger log = LoggerFactory.getLogger(LocationSimulator.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    StammdatenRepository stammdatenRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    LocationRepository locationRepository;

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() throws IOException {
        if (jHipsterProperties.getSimulator().isEnabled()) {

            // check stammdaten exist:
            Stammdaten stammdaten = stammdatenRepository.findOneByDeviceId(SIMULATOR_ID);
            if (stammdaten == null) {
                stammdaten = new Stammdaten();
                stammdaten.setDeviceId(SIMULATOR_ID);
                stammdaten.setDriver("Simu Lator");
                stammdaten.setVehicle("XYZ123");
                stammdatenRepository.save(stammdaten);
            }

            // check at least one order exists
            Order order = orderRepository.findOneByDeviceIdOrderByDeadlineDesc(SIMULATOR_ID);
            if (order == null) {
                order = new Order();
                order.setDeviceId(SIMULATOR_ID);
                order.setDeadline(ZonedDateTime.now());
                order.setFromLongitude(16.22);
                order.setFromLatitude(48.12);
                order.setToLongitude(15.6038);
                order.setToLatitude(48.41);
                order.setOrderNr("SIMU-1");
                order = orderRepository.save(order);
            }

            // location, location, location

            Location location = new Location();
            location.setDeviceId("Simulator-1");

            double longitudeMin = Math.min(order.getFromLongitude(), order.getToLongitude());
            double longitudeMax = Math.max(order.getFromLongitude(), order.getToLongitude());
            double randomLongitude = randomRange(longitudeMin, longitudeMax);

            double lattitudeMin = Math.min(order.getFromLatitude(), order.getToLatitude());
            double latitudeMax = Math.max(order.getFromLatitude(), order.getToLatitude());
            double randomLatitude = randomRange(lattitudeMin, latitudeMax);

            location.setLongitude(randomLongitude);
            location.setLatitude(randomLatitude);
            location.setTime(ZonedDateTime.now());

            locationRepository.save(location);

            log.debug("Simulated new location: {}", location);
        }
    }

    private double randomRange(double rangeMin, double rangeMax) {
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }

}
