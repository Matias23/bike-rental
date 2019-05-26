package com.intive.rental.service.repository;

import com.intive.rental.service.domain.FamilyRentalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRentalRepository extends MongoRepository<FamilyRentalEntity, String> {
}
