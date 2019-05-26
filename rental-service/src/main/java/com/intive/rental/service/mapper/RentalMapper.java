package com.intive.rental.service.mapper;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import com.intive.rental.dto.request.CreateFamilyRentalRequest;
import com.intive.rental.dto.request.CreateSimpleRentalRequest;
import com.intive.rental.service.domain.FamilyRentalEntity;
import com.intive.rental.service.domain.RentalEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class RentalMapper {

    private static final ModelMapper MAPPER = new ModelMapper();

    public SimpleRental entityToDto(RentalEntity entity) {
        SimpleRental mapping = MAPPER.map(entity, SimpleRental.class);
        return mapping;
    }

    public RentalEntity dtoToEntity(CreateSimpleRentalRequest request) {
        return MAPPER.map(request, RentalEntity.class);
    }

    public FamilyRental entityToDto(FamilyRentalEntity entity) {
        FamilyRental mapping = MAPPER.map(entity, FamilyRental.class);
        return mapping;
    }

    public FamilyRentalEntity dtoToEntity(CreateFamilyRentalRequest request) {
        return MAPPER.map(request, FamilyRentalEntity.class);
    }

}
