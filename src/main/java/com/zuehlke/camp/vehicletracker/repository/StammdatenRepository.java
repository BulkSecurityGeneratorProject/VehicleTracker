package com.zuehlke.camp.vehicletracker.repository;

import com.zuehlke.camp.vehicletracker.domain.Stammdaten;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Stammdaten entity.
 */
public interface StammdatenRepository extends MongoRepository<Stammdaten,String> {
    Stammdaten findOneByDeviceId(String deviceId);
}
