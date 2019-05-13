package com.matias.rental.service.domain;

import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicRentalEntity {

    @Id
    @Field(value = "rental_id")
    protected String rentalId;

    @Field(value = "type")
    protected RentalType type;
}
