package com.matias.rental.service;

import com.matias.rental.dto.FamilyRental;
import com.matias.rental.dto.Rental;
import com.matias.rental.dto.request.CreateFamilyRentalRequest;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.domain.FamilyRentalEntity;
import com.matias.rental.service.domain.RentalEntity;
import com.matias.rental.service.mapper.RentalMapper;
import com.matias.rental.service.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalService {

    @Autowired
    private RentalRepository repository;

/*    @Autowired
    private Producer<String, BasicRental> createdRentalMessageProducer;*/

    public Rental postSingleRental(CreateRentalRequest createRentalRequest) {
        RentalEntity rental = RentalMapper.dtoToEntity(createRentalRequest);
        RentalEntity savedRental = repository.save(rental);
        //sendKafkaMessage(rental);
        return RentalMapper.entityToDto(savedRental);
    }

    public FamilyRental postFamilyRental(CreateFamilyRentalRequest createFamilyRentalRequest) {
        FamilyRentalEntity rental = RentalMapper.dtoToEntity(createFamilyRentalRequest);
        FamilyRentalEntity savedRental = repository.save(rental);
        //sendKafkaMessage(rental);
        return RentalMapper.entityToDto(savedRental);
    }

/*    private void sendKafkaMessage(RentalEntity entity) {
        Rental rental = RentalMapper.entityToDto(entity);
        ProducerRecord<String, BasicRental> record =
                new ProducerRecord<>("created-single-rental", rental);
        createdRentalMessageProducer.send(record);
    }

    private void sendKafkaMessage(FamilyRentalEntity entity) {
        FamilyRental rental = RentalMapper.entityToDto(entity);
        ProducerRecord<String, BasicRental> record =
                new ProducerRecord<>("created-family-rental", rental);
        createdRentalMessageProducer.send(record);
    }*/
}
