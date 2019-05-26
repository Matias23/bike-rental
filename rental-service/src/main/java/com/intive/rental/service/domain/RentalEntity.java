package com.intive.rental.service.domain;

import com.intive.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "single_rentals")
public class RentalEntity extends BasicRentalEntity{

    @Field(value = "amount")
    private Integer amount;

    @Field(value = "rental_type")
    private RentalType rentalType;

    @Builder
    public RentalEntity(String rentalId, Integer amount, RentalType rentalType) {
        super(rentalId);
        this.amount = amount;
        this.rentalType = rentalType;
    }

}
