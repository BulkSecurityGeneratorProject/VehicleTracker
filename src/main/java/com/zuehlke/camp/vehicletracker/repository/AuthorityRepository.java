package com.zuehlke.camp.vehicletracker.repository;

import com.zuehlke.camp.vehicletracker.domain.Authority;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
