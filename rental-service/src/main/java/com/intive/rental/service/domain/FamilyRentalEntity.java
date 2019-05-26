package com.intive.rental.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "family_rentals")
public class FamilyRentalEntity extends  BasicRentalEntity{

    @Field(value = "rentals")
    private List<SimpleRentalData> rentals;

    @Builder
    public FamilyRentalEntity(String rentalId, List<SimpleRentalData> rentals) {
        super(rentalId);
        this.rentals = rentals;
    }

}
