package com.matias.rental.service.mapper;

import com.matias.rental.dto.FamilyRental;
import com.matias.rental.dto.Rental;
import com.matias.rental.dto.request.CreateFamilyRentalRequest;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.domain.FamilyRentalEntity;
import com.matias.rental.service.domain.RentalEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class RentalMapper {

    public Rental entityToDto(RentalEntity entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, Rental.class);
    }

    public FamilyRental entityToDto(FamilyRentalEntity entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, FamilyRental.class);
    }

    public RentalEntity dtoToEntity(CreateRentalRequest request) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(request, RentalEntity.class);
    }

    public FamilyRentalEntity dtoToEntity(CreateFamilyRentalRequest request) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(request, FamilyRentalEntity.class);
    }

}
