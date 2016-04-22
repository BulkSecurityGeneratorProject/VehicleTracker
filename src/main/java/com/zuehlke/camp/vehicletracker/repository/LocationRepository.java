package com.zuehlke.camp.vehicletracker.repository;

import com.zuehlke.camp.vehicletracker.domain.Location;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Spring Data MongoDB repository for the Location entity.
 */
public interface LocationRepository extends MongoRepository<Location,String> {
    Location findOneByDeviceIdOrderByTimeDesc(String deviceId);
    void deleteByTimeBefore(ZonedDateTime expiryDate);
}
