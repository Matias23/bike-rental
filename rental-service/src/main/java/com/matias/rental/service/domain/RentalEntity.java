package com.matias.rental.service.domain;

import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rentals")
public class RentalEntity {

    @Id
    @Field(value = "rental_id")
    protected String rentalId;

    @Field(value = "type")
    protected RentalType type;

    @Field(value = "amount")
    private Integer amount;

    @Field(value = "included_rentals")
    private List<RentalEntity> rentals;

}
