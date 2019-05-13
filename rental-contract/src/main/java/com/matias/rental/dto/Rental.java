package com.matias.rental.dto;

import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Rental extends BasicRental{

    private Integer amount;

    @Builder(builderMethodName = "createRental")
    public Rental(String rentalId, RentalType type, Integer amount) {
        super(rentalId, type);
        this.amount = amount;
    }
}
