package com.zuehlke.camp.vehicletracker.repository;

import com.zuehlke.camp.vehicletracker.domain.Location;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Location entity.
 */
public interface LocationRepository extends MongoRepository<Location,String> {
    Location findOneByDeviceIdOrderByTimeDesc(String deviceId);
}
