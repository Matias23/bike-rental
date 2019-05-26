package com.intive.rental.service;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import com.intive.rental.dto.request.CreateFamilyRentalRequest;
import com.intive.rental.dto.request.CreateSimpleRentalRequest;
import com.intive.rental.service.domain.FamilyRentalEntity;
import com.intive.rental.service.domain.RentalEntity;
import com.intive.rental.service.mapper.RentalMapper;
import com.intive.rental.service.repository.FamilyRentalRepository;
import com.intive.rental.service.repository.SimpleRentalRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private SimpleRentalRepository simpleRentalRepository;

    @Autowired
    private FamilyRentalRepository familyRentalRepository;

    @Autowired
    private KafkaProducer<String, SimpleRental> createdSimpleRentalMessageProducer;

    @Autowired
    private KafkaProducer<String, FamilyRental> createdFamilyRentalMessageProducer;

    @Autowired
    private ChargingStrategy chargingStrategy;

    public SimpleRental postSimpleRental(CreateSimpleRentalRequest createRequest) {
        RentalEntity rentalEntity = RentalMapper.dtoToEntity(createRequest);
        RentalEntity savedRental = simpleRentalRepository.save(rentalEntity);
        SimpleRental rental = RentalMapper.entityToDto(savedRental);
        rental.setCost(chargingStrategy.getCost(rental));
        sendKafkaMessage(rental);
        return rental;
    }

    public FamilyRental postFamilyRental(CreateFamilyRentalRequest createRequest) {
        FamilyRentalEntity rentalEntity = RentalMapper.dtoToEntity(createRequest);
        FamilyRentalEntity savedRental = familyRentalRepository.save(rentalEntity);
        FamilyRental rental = RentalMapper.entityToDto(savedRental);
        rental.setCost(chargingStrategy.getCost(rental));
        sendKafkaMessage(rental);
        return rental;
    }

    private void sendKafkaMessage(SimpleRental rental) {
        ProducerRecord<String, SimpleRental> record =
                new ProducerRecord<>("created-simple-rental-message", rental);
        createdSimpleRentalMessageProducer.send(record);
    }

    private void sendKafkaMessage(FamilyRental rental) {
        ProducerRecord<String, FamilyRental> record =
                new ProducerRecord<>("created-family-rental-message", rental);
        createdFamilyRentalMessageProducer.send(record);
    }

    public SimpleRental getRental(String rentalId) {
        Optional<RentalEntity> singleRental =  simpleRentalRepository.findById(rentalId);
        if (singleRental.isPresent()) {
            return RentalMapper.entityToDto(singleRental.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rental not found");
        }
    }

    public FamilyRental getFamilyRental(String rentalId) {
        Optional<FamilyRentalEntity> familyRental =  familyRentalRepository.findById(rentalId);
        if (familyRental.isPresent()) {
            return RentalMapper.entityToDto(familyRental.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "family rental not found");
        }
    }

}
