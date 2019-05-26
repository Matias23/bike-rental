package com.intive.rental.service.repository;

import com.intive.rental.service.domain.RentalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleRentalRepository extends MongoRepository<RentalEntity, String> {
}
