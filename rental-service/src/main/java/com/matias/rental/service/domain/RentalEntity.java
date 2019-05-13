package com.matias.rental.service.domain;

import com.matias.rental.dto.constant.RentalType;
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
@Document(collection = "rentals")
public class RentalEntity extends BasicRentalEntity {

    @Field(value = "amount")
    private Integer amount;

    @Builder(builderMethodName = "rentalEntityBuilder")
    public RentalEntity(String rentalId, RentalType type, Integer amount) {
        super(rentalId, type);
        this.amount = amount;
    }

}
