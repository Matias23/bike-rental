package com.intive.rental.service.mapper;

import com.intive.rental.dto.Rental;
import com.intive.rental.dto.request.CreateRentalRequest;
import com.intive.rental.service.domain.RentalEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class RentalMapper {

    public Rental entityToDto(RentalEntity entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, Rental.class);
    }

    public RentalEntity dtoToEntity(CreateRentalRequest request) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(request, RentalEntity.class);
    }

}
