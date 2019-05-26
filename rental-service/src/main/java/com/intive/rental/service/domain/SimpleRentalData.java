package com.intive.rental.service.domain;

import com.intive.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRentalData {

    @Field(value = "amount")
    private Integer amount;

    @Field(value = "rental_type")
    private RentalType rentalType;

}
