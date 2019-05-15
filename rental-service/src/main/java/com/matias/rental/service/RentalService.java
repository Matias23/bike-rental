package com.matias.rental.service;

import com.matias.rental.dto.Rental;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.domain.RentalEntity;
import com.matias.rental.service.mapper.RentalMapper;
import com.matias.rental.service.repository.RentalRepository;
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
    private RentalRepository repository;

    @Autowired
    private KafkaProducer<String, Rental> createdRentalMessageProducer;

    public Rental postRental(CreateRentalRequest createRequest) {
        RentalEntity rental = RentalMapper.dtoToEntity(createRequest);
        RentalEntity savedRental = repository.save(rental);
        sendKafkaMessage(rental);
        return RentalMapper.entityToDto(savedRental);
    }

    public Rental getRental(String rentalId) {
        Optional<RentalEntity> rental =  repository.findById(rentalId);
        if (!rental.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rental not found");
        }
        return RentalMapper.entityToDto(rental.get());
    }

    private void sendKafkaMessage(RentalEntity entity) {
        Rental rental = RentalMapper.entityToDto(entity);
        ProducerRecord<String, Rental> record =
                new ProducerRecord<>("created-rental-message", rental);
        createdRentalMessageProducer.send(record);
    }

}
