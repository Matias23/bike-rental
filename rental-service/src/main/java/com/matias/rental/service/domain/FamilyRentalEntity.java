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

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rentals")
public class FamilyRentalEntity extends BasicRentalEntity {

    @Field(value = "included_rentals")
    private List<RentalEntity> rentals;

    @Builder(builderMethodName = "familyRentalEntityBuilder")
    public FamilyRentalEntity(String rentalId, RentalType type, List<RentalEntity> rentals) {
        super(rentalId, type);
        this.rentals = rentals;
    }
}
