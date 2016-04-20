package com.zuehlke.camp.vehicletracker.repository;

import com.zuehlke.camp.vehicletracker.domain.Order;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Order entity.
 */
public interface OrderRepository extends MongoRepository<Order,String> {
    Order findOneByDeviceIdOrderByDeadlineDesc(String deviceId);
}
