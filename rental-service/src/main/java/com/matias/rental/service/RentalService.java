package com.matias.rental.service;

import com.matias.rental.dto.Rental;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.domain.RentalEntity;
import com.matias.rental.service.mapper.RentalMapper;
import com.matias.rental.service.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository repository;

    public Rental postRental(CreateRentalRequest createRequest) {
        RentalEntity rental = RentalMapper.dtoToEntity(createRequest);
        RentalEntity savedRental = repository.save(rental);
        return RentalMapper.entityToDto(savedRental);
    }

    public Rental getRental(String rentalId) {
        Optional<RentalEntity> rental =  repository.findById(rentalId);
        if (!rental.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rental not found");
        }
        return RentalMapper.entityToDto(rental.get());
    }

}
