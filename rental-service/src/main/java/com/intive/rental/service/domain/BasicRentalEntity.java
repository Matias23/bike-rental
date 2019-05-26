package com.intive.rental.service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasicRentalEntity {

    @Id
    @Field(value = "rental_id")
    protected String rentalId;
}
