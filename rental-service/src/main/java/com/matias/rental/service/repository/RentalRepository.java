package com.matias.rental.service.repository;

import com.matias.rental.service.domain.RentalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends MongoRepository<RentalEntity, String> {
}
